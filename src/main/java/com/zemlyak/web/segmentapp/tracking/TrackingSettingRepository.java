package com.zemlyak.web.segmentapp.tracking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface TrackingSettingRepository extends JpaRepository<TrackingSetting, Integer> {
    List<TrackingSetting> findByMetricIdIn(Set<Integer> metricIds);
}
