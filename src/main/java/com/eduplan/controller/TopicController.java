package com.eduplan.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.eduplan.model.Topic;
import com.eduplan.service.TopicService;

@RestController
@RequestMapping("/topics")
@CrossOrigin(origins = "https://eduplanner-frontend.onrender.com/")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    public Topic addTopic(@RequestBody Topic topic) {
        return topicService.addTopic(topic);
    }

    @GetMapping
    public List<Topic> getTopics() {
        return topicService.getTopics();
    }
    
    @GetMapping("/priority/{userId}")
    public List<Topic> getPriorityTopics(@PathVariable Long userId){
        return topicService.getPriorityTopics(userId);
    }
    
    
}