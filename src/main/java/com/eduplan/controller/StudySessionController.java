package com.eduplan.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.eduplan.model.StudySession;
import com.eduplan.service.StudySessionService;

@RestController
@RequestMapping("/sessions")
@CrossOrigin(origins = "https://eduplanner-frontend.onrender.com/")
public class StudySessionController {

    private final StudySessionService sessionService;

    public StudySessionController(StudySessionService sessionService){
        this.sessionService = sessionService;
    }

    // ✅ BEST API (use this in frontend)
    @GetMapping("/user/{userId}")
    public List<StudySession> getByUser(@PathVariable Long userId){
        return sessionService.getSessionsByUser(userId);
    }

    // optional (if needed)
    @GetMapping("/plan/{planId}")
    public List<StudySession> getByPlan(@PathVariable Long planId){
        return sessionService.getSessionsByPlan(planId);
    }

    // mark complete
    @PutMapping("/complete/{sessionId}")
    public StudySession markCompleted(@PathVariable Long sessionId){
        return sessionService.markCompleted(sessionId);
    }
    
    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable Long id){
        sessionService.deleteSession(id);
    }
}