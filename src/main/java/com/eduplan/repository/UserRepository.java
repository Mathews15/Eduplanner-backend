package com.eduplan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eduplan.model.User;

public interface UserRepository extends JpaRepository<User,Long>{
	User findByEmail(String email);
}