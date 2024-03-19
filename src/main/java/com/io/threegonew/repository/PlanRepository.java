package com.io.threegonew.repository;

import com.io.threegonew.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByPlannerId(Long plannerId);
    // 유무 확인 나중에 추가
    List<Plan> findByPlannerIdAndDay(Long plannerId, Integer day);

    Optional<Plan> findTopByPlannerIdOrderByDayDesc(Long plannerId);
}
