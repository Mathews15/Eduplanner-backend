package com.eduplan.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            UserRepository userRepository) {

        this.planRepository = planRepository;
        this.sessionRepository = sessionRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    public StudyPlan generatePlan(StudyPlanRequest request) {

        // Validate days
        if (request.getDays() <= 0) {
            throw new RuntimeException("Days must be greater than 0");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // FIX 2: Only fetch this user's topics (not all topics from all users)
        List<Topic> allUserTopics = topicRepository.findByUser_Id(user.getId());

        if (allUserTopics == null || allUserTopics.isEmpty()) {
            throw new RuntimeException("No topics found. Please add topics before generating a plan.");
        }

        // FIX 5: Exclude topics that are already completed in previous sessions
        List<StudySession> existingSessions = sessionRepository.findByStudyPlan_User_Id(user.getId());
        Set<Long> completedTopicIds = existingSessions.stream()
                .filter(StudySession::isCompleted)
                .map(s -> s.getTopic().getId())
                .collect(Collectors.toSet());

        List<Topic> pendingTopics = allUserTopics.stream()
                .filter(t -> !completedTopicIds.contains(t.getId()))
                .collect(Collectors.toList());

        if (pendingTopics.isEmpty()) {
            throw new RuntimeException("All topics are already completed. Add new topics to generate a plan.");
        }

        // FIX 4: Corrected priority sort — low proficiency = high priority (ascending)
        pendingTopics.sort((t1, t2) -> t1.getProficiencyLevel() - t2.getProficiencyLevel());

        // Create the plan
        StudyPlan plan = new StudyPlan();
        plan.setUser(user);
        plan = planRepository.save(plan);

        LocalDate startDate = LocalDate.parse(request.getStartDate());

        // Use hoursPerDay from request, fallback to user's setting
        int hoursPerDay = request.getHoursPerDay() > 0
                ? request.getHoursPerDay()
                : user.getDailyStudyHours();

        if (hoursPerDay <= 0) hoursPerDay = 2; // safe default

        // Create one session per day cycling through pending topics
        for (int i = 0; i < request.getDays(); i++) {
            Topic topic = pendingTopics.get(i % pendingTopics.size());

            StudySession session = new StudySession();
            session.setTopic(topic);
            session.setStudyPlan(plan);
            session.setStudyDate(startDate.plusDays(i));
            session.setAllocatedHours(hoursPerDay);
            session.setCompleted(false);

            sessionRepository.save(session);
        }

        return plan;
    }
}