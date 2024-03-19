package com.io.threegonew.service;

import com.io.threegonew.domain.Area;
import com.io.threegonew.domain.Plan;
import com.io.threegonew.domain.Sigungu;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.dto.AddPlanRequest;
import com.io.threegonew.repository.AreaRepository;
import com.io.threegonew.repository.PlanRepository;
import com.io.threegonew.repository.SigunguRepository;
import com.io.threegonew.repository.TourItemRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final AreaRepository areaRepository;
    private final SigunguRepository sigunguRepository;
    private final TourItemRepository tourItemRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    public List<Plan> findByPlannerId(Long plannerId) {
        return planRepository.findByPlannerId(plannerId);
    }

    public TreeMap<String, List<Plan>> findByPlannerGroupByDay(Long plannerId) {
        List<Plan> planList = planRepository.findByPlannerId(plannerId);

        int day = 0;
        String key = "";
        TreeMap<String, List<Plan>> planMap = new TreeMap<>();
        List<Plan> dayPlanList = new ArrayList<>();

        for(Plan plan : planList){
            if(day != plan.getDay()){
                if(day > 0){
                    planMap.put(key, dayPlanList);
                }
                day = plan.getDay();
                key = day + "day";
                dayPlanList = new ArrayList<>();
            }
            dayPlanList.add(plan);
        }

        if(!dayPlanList.isEmpty()) {
            planMap.put(key, dayPlanList);
        }

        return planMap;
    }

    public List<Plan> findByPlannerIdAndDay(Long plannerId, Integer day) {
        return planRepository.findByPlannerIdAndDay(plannerId, day);
    }

    public Optional<Plan> findTopByPlannerIdOrderByDayDesc(Long plannerId) {
        return planRepository.findTopByPlannerIdOrderByDayDesc(plannerId);
    }

    public List<Area> findAllAreas() {
        return areaRepository.findAll();
    }

    public List<Sigungu> findSigunguByAreaCode(Integer areaCode) {
        return sigunguRepository.findByAreaCode(areaCode);
    }

    public Plan save(AddPlanRequest dto, String userId) {
        // tourItem을 찾아서 설정
        TourItem tourItem = tourItemRepository.findById(dto.getContentId()).orElse(null);

        Plan plan = planRepository.save(Plan.builder()
                .userId(userId)
                .plannerId(dto.getPlannerId())
                .day(dto.getDay())
                .order(dto.getOrder())
                .tourItem(tourItem)
                .build());


        return plan;
    }
}
