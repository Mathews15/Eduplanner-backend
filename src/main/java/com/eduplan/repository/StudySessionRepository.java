package com.eduplan.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.eduplan.model.StudySession;

public interface StudySessionRepository extends JpaRepository<StudySession, Long>{

    // ✅ by plan id (fixed)
    List<StudySession> findByStudyPlan_Id(Long planId);

    // ✅ by user id (BEST approach
    
    List<StudySession> findByStudyPlan_User_Id(Long userId);
    
    
}