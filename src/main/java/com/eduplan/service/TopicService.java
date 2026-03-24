package com.eduplan.service;

import java.util.List;
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

    // ✅ ADD TOPIC
    public Topic addTopic(Topic topic) {

        if(topic.getSubject() == null){
            throw new RuntimeException("Subject is missing");
        }

        Long subjectId = topic.getSubject().getId();

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id " + subjectId));

        topic.setSubject(subject);

        return topicRepository.save(topic);
    }

    // ✅ PRIORITY TOPICS (🔥 FIXED)
    public List<Topic> getPriorityTopics(Long userId){

        List<Topic> topics = topicRepository.findByUserId(userId);

        List<StudySession> sessions =
                sessionRepository.findByStudyPlan_User_Id(userId)
                .stream()
                .sorted((s1, s2) -> s2.getId().compareTo(s1.getId())) // latest first
                .toList();

        List<Long> completedTopicIds = sessions.stream()
                .filter(s -> s.isCompleted())
                .map(s -> s.getTopic().getId())
                .collect(Collectors.toList());

        return topics.stream()
                .filter(t -> !completedTopicIds.contains(t.getId()))
                .sorted((t1, t2) ->
                        (t2.getDifficultyLevel() - t2.getProficiencyLevel()) -
                        (t1.getDifficultyLevel() - t1.getProficiencyLevel())
                )
                .collect(Collectors.toList());
    }

    // ✅ GET ALL TOPICS
    public List<Topic> getTopics() {
        return topicRepository.findAll();
    }
}