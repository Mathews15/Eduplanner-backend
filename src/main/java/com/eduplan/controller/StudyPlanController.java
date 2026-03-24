package com.eduplan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eduplan.service.StudyPlanService;
import com.eduplan.dto.StudyPlanRequest;
import com.eduplan.model.StudyPlan;

@RestController
@RequestMapping("/plan")
@CrossOrigin(origins = {"https://eduplanner-frontend.onrender.com", "http://localhost:5173", "http://localhost:3000"})
public class StudyPlanController {

    private final StudyPlanService planService;

    public StudyPlanController(StudyPlanService planService) {
        this.planService = planService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generatePlan(@RequestBody StudyPlanRequest request) {
        try {
            StudyPlan plan = planService.generatePlan(request);
            return ResponseEntity.ok(plan);
        } catch (RuntimeException e) {
            // FIX: Return meaningful error message to frontend
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}