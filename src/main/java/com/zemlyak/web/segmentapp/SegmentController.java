package com.zemlyak.web.segmentapp;

import com.zemlyak.web.segmentapp.segment.CountryStat;
import com.zemlyak.web.segmentapp.segment.CountryStatsUpdateRepository;
import com.zemlyak.web.segmentapp.segment.Segment;
import com.zemlyak.web.segmentapp.segment.SegmentRepository;
import com.zemlyak.web.segmentapp.stat.SegmentProjection;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class SegmentController {
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RAND = new Random(System.nanoTime());

    private final SegmentRepository segmentRepository;
    private final CountryStatsUpdateRepository statsUpdateRepository;

    @GetMapping(path = "/catalogs/segments")
    public List<SegmentProjection> findSegmentById(@RequestParam("name") String name) {
        return segmentRepository.findByNameLike("%" + name + "%");
    }

    @GetMapping(path = "/segments")
    public List<Segment> findSegmentOriginal(@RequestParam(value = "jpa-repo", required = false) Boolean jpaRepo,
                                             @RequestParam(value = "type", required = false, defaultValue = "1") Integer typeId) {
        Specification<Segment> specification = SegmentRepository
                .withCountryStat("")
                .and(SegmentRepository.hasType(typeId));

        List<Segment> segments;
        if (Objects.requireNonNullElse(jpaRepo, Boolean.FALSE)) {
            segments = segmentRepository.findAll(specification);
        } else {
            segments = segmentRepository.findAllOriginal(specification);
        }
        return segments;
    }

    @GetMapping(path = "/segments/{id}")
    public Segment findSegmentById(@PathVariable("id") Integer id) {
        return segmentRepository.findById(id).orElse(null);
    }

    @Transactional
    @GetMapping(path = "/segments/{id}/generate-stats")
    public Segment generateStats(@PathVariable("id") Integer id) {
        Set<CountryStat.Key> existedKeys = statsUpdateRepository.findAll().stream().map(com.zemlyak.web.segmentapp.segment.CountryStat::getId).collect(Collectors.toSet());
        List<com.zemlyak.web.segmentapp.segment.CountryStat> generatedCountries = new ArrayList<>(200);
        for (int i = 0; i < UPPER.length(); i++) {
            for (int j = 0; j < UPPER.length(); j++) {
                if (i == j) {
                    continue;
                }
                generatedCountries.add(statForCountryWithRandomValues(id,UPPER.charAt(i) + "" + UPPER.charAt(j), existedKeys));
            }
        }
        statsUpdateRepository.saveAll(generatedCountries);
        return findSegmentById(id);
    }

    private static com.zemlyak.web.segmentapp.segment.CountryStat statForCountryWithRandomValues(Integer id, String country, Set<com.zemlyak.web.segmentapp.segment.CountryStat.Key> existedKeys) {
        com.zemlyak.web.segmentapp.segment.CountryStat countryStat = new com.zemlyak.web.segmentapp.segment.CountryStat();
        countryStat.setSegmentId(id);
        countryStat.setCountryCode(country);
        countryStat.setActiveProfilesAmount((long)RAND.nextInt(1000));
        countryStat.setSleepingProfilesAmount((long)RAND.nextInt(1000));
        countryStat.setNew(!existedKeys.contains(countryStat.getId()));
        return countryStat;
    }
}
