package com.eduplan.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.eduplan.model.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long>{

    // 🔥 IMPORTANT for user-specific topics
    List<Topic> findBySubjectUserId(Long userId);
}