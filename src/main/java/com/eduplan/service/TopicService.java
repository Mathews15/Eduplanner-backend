package com.eduplan.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.eduplan.model.Topic;
import com.eduplan.model.Subject;
import com.eduplan.model.StudySession;
import com.eduplan.repository.TopicRepository;
import com.eduplan.repository.SubjectRepository;
import com.eduplan.repository.StudySessionRepository;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final SubjectRepository subjectRepository;
    private final StudySessionRepository sessionRepository;

    public TopicService(TopicRepository topicRepository,
                        SubjectRepository subjectRepository,
                        StudySessionRepository sessionRepository) {
        this.topicRepository = topicRepository;
        this.subjectRepository = subjectRepository;
        this.sessionRepository = sessionRepository;
    }

    // ADD TOPIC - also derives user from subject owner
    public Topic addTopic(Topic topic) {

        if (topic.getSubject() == null) {
            throw new RuntimeException("Subject is missing");
        }

        Long subjectId = topic.getSubject().getId();

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id " + subjectId));

        topic.setSubject(subject);

        // FIX 2: Set user from subject so findByUser_Id works in plan generation
        topic.setUser(subject.getUser());

        return topicRepository.save(topic);
    }

    // PRIORITY TOPICS - only this user's incomplete topics, sorted low proficiency first
    public List<Topic> getPriorityTopics(Long userId) {

        // FIX 1: Fetch only this user's topics, not all topics
        List<Topic> userTopics = topicRepository.findByUser_Id(userId);

        // Get all completed session topic IDs for this user
        List<StudySession> sessions = sessionRepository.findByStudyPlan_User_Id(userId);

        Set<Long> completedTopicIds = sessions.stream()
                .filter(StudySession::isCompleted)
                .map(s -> s.getTopic().getId())
                .collect(Collectors.toSet());

        return userTopics.stream()
                // FIX 1: Remove topics that are completed
                .filter(t -> !completedTopicIds.contains(t.getId()))
                // FIX 4: Low proficiency = high priority, so sort ascending
                .sorted((t1, t2) -> t1.getProficiencyLevel() - t2.getProficiencyLevel())
                .collect(Collectors.toList());
    }

    public List<Topic> getTopics() {
        return topicRepository.findAll();
    }

    public List<Topic> getTopicsByUser(Long userId) {
        return topicRepository.findByUser_Id(userId);
    }
}