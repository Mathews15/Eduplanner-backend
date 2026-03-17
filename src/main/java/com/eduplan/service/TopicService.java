package com.eduplan.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eduplan.model.Topic;
import com.eduplan.model.Subject;
import com.eduplan.repository.TopicRepository;
import com.eduplan.repository.SubjectRepository;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final SubjectRepository subjectRepository;

    public TopicService(TopicRepository topicRepository,
                        SubjectRepository subjectRepository) {
        this.topicRepository = topicRepository;
        this.subjectRepository = subjectRepository;
    }

    public Topic addTopic(Topic topic) {

        Long subjectId = topic.getSubject().getId();

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        topic.setSubject(subject);

        return topicRepository.save(topic);
    }
    
    public List<Topic> getPriorityTopics(){

        List<Topic> topics = topicRepository.findAll();

        topics.sort((t1, t2) ->
            (t2.getDifficultyLevel() - t2.getProficiencyLevel()) -
            (t1.getDifficultyLevel() - t1.getProficiencyLevel())
        );

        return topics;
    }
    
    public List<Topic> getTopics() {
        return topicRepository.findAll();
    }
    
    
}