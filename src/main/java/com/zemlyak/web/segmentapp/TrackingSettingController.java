package com.zemlyak.web.segmentapp;

import com.zemlyak.web.segmentapp.tracking.TrackingSetting;
import com.zemlyak.web.segmentapp.tracking.TrackingSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrackingSettingController {
    private final TrackingSettingRepository trackingSettingRepository;

    @GetMapping(path = "/tracking-settings")
    public List<TrackingSetting> findAll(@RequestParam(value = "metricId", required = false) Integer metricId) {
        if (metricId != null) {
            return trackingSettingRepository.findByMetricIdIn(Collections.singleton(metricId));
        } else {
            return trackingSettingRepository.findAll();
        }
    }
}
