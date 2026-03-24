package com.eduplan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int difficultyLevel;

    private int proficiencyLevel;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    // 🔥 IMPORTANT: ADD THIS
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // getters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getProficiencyLevel() {
        return proficiencyLevel;
    }

    public Subject getSubject() {
        return subject;
    }

    public User getUser() {   // ✅ MUST EXIST
        return user;
    }

    // setters

    public void setName(String name) {
        this.name = name;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public void setProficiencyLevel(int proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setUser(User user) {   // ✅ MUST EXIST
        this.user = user;
    }
}