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
import com.eduplan.repository.SubjectRepository;
import com.eduplan.service.SubjectService;

@RestController
@RequestMapping("/subjects")
@CrossOrigin(origins = "http://localhost:5173")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectRepository subjectRepository;

    // ✅ FIXED constructor
    public SubjectController(SubjectService subjectService,
                             SubjectRepository subjectRepository) {
        this.subjectService = subjectService;
        this.subjectRepository = subjectRepository;
    }

    @PostMapping("/{userId}")
    public Subject addSubject(@PathVariable Long userId,
                              @RequestBody Subject subject) {

        return subjectService.addSubject(userId, subject);
    }
    
    @GetMapping
    public List<Subject> getSubjects(){
        return subjectService.getSubjects();
    }
    
    @GetMapping("/user/{userId}")
    public List<Subject> getSubjectsByUser(@PathVariable Long userId){
        return subjectRepository.findByUserId(userId);
    }
}