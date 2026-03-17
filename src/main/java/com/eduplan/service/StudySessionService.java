package com.eduplan.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.eduplan.model.StudyPlan;
import com.eduplan.model.StudySession;
import com.eduplan.model.Topic;
import com.eduplan.repository.StudyPlanRepository;
import com.eduplan.repository.StudySessionRepository;
import com.eduplan.repository.TopicRepository;

@Service
public class StudySessionService {

    private final StudySessionRepository sessionRepository;
    private final TopicRepository topicRepository;
    private final StudyPlanRepository planRepository;

    public StudySessionService(
            StudySessionRepository sessionRepository,
            TopicRepository topicRepository,
            StudyPlanRepository planRepository) {

        this.sessionRepository = sessionRepository;
        this.topicRepository = topicRepository;
        this.planRepository = planRepository;
    }

    // create session
    public StudySession createSession(StudySession session){

        Long topicId = session.getTopic().getId();
        Long planId = session.getStudyPlan().getId();

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        StudyPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("StudyPlan not found"));

        session.setTopic(topic);
        session.setStudyPlan(plan);

        return sessionRepository.save(session);
    }

    // get sessions by plan
    public List<StudySession> getSessionsByPlan(Long planId){
        return sessionRepository.findByStudyPlan_Id(planId);
    }

    // get sessions by user (🔥 main API)
    public List<StudySession> getSessionsByUser(Long userId){
        return sessionRepository.findByStudyPlan_User_Id(userId);
    }

    // mark completed
    public StudySession markCompleted(Long sessionId){

        StudySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setCompleted(true);

        return sessionRepository.save(session);
    }
    
    public void deleteSession(Long id){
        sessionRepository.deleteById(id);
    }
}