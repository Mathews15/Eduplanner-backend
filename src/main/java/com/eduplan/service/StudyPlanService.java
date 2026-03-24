package com.eduplan.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eduplan.dto.StudyPlanRequest;
import com.eduplan.model.StudyPlan;
import com.eduplan.model.StudySession;
import com.eduplan.model.Topic;
import com.eduplan.model.User;
import com.eduplan.repository.StudyPlanRepository;
import com.eduplan.repository.StudySessionRepository;
import com.eduplan.repository.TopicRepository;
import com.eduplan.repository.UserRepository;

@Service
public class StudyPlanService {

    private final StudyPlanRepository planRepository;
    private final StudySessionRepository sessionRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public StudyPlanService(
            StudyPlanRepository planRepository,
            StudySessionRepository sessionRepository,
            TopicRepository topicRepository,
            UserRepository userRepository){

        this.planRepository = planRepository;
        this.sessionRepository = sessionRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    // 🔥 👉 PUT YOUR METHOD HERE
    public StudyPlan generatePlan(StudyPlanRequest request){

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        StudyPlan plan = new StudyPlan();
        plan.setUser(user);
        plan = planRepository.save(plan);

        // ✅ only current user's topics
        List<Topic> topics = topicRepository.findByUserId(user.getId());

        if(topics.isEmpty()){
            throw new RuntimeException("No topics found for user");
        }

        // priority sort
        topics.sort((t1, t2) ->
                (t2.getDifficultyLevel() - t2.getProficiencyLevel()) -
                (t1.getDifficultyLevel() - t1.getProficiencyLevel())
        );

        LocalDate startDate = LocalDate.parse(request.getStartDate());

        for(int i = 0; i < request.getDays(); i++){
            Topic topic = topics.get(i % topics.size());

            StudySession session = new StudySession();
            session.setTopic(topic);
            session.setStudyPlan(plan);
            session.setStudyDate(startDate.plusDays(i));
            session.setAllocatedHours(request.getHoursPerDay());
            session.setCompleted(false);

            sessionRepository.save(session);
        }

        return plan;
    }
}