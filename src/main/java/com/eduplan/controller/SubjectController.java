package com.eduplan.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eduplan.model.Subject;
import com.eduplan.service.SubjectService;

@RestController
@RequestMapping("/subjects")
@CrossOrigin(origins = {"https://eduplanner-frontend.onrender.com", "http://localhost:5173", "http://localhost:3000"})
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping("/{userId}")
    public Subject addSubject(@PathVariable Long userId,
                              @RequestBody Subject subject) {
        return subjectService.addSubject(userId, subject);
    }

    // FIX 3: Return only this user's subjects
    @GetMapping("/user/{userId}")
    public List<Subject> getSubjectsByUser(@PathVariable Long userId) {
        return subjectService.getSubjectsByUser(userId);
    }

    @GetMapping
    public List<Subject> getSubjects() {
        return subjectService.getSubjects();
    }
}