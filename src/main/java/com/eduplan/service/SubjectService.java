package com.eduplan.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.eduplan.model.Subject;
import com.eduplan.model.User;
import com.eduplan.repository.SubjectRepository;
import com.eduplan.repository.UserRepository;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public SubjectService(SubjectRepository subjectRepository,
                          UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }

    public Subject addSubject(Long userId, Subject subject) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        subject.setUser(user);

        return subjectRepository.save(subject);
    }
    
    public List<Subject> getSubjects(){
        return subjectRepository.findAll();
    }
}