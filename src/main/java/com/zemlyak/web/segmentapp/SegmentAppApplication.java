package com.zemlyak.web.segmentapp;

import com.zemlyak.web.segmentapp.model.CountryStat;
import com.zemlyak.web.segmentapp.model.CountryStat_;
import com.zemlyak.web.segmentapp.model.SegmentProjection;
import com.zemlyak.web.segmentapp.model.Segment_;
import com.zemlyak.web.segmentapp.model2.TrackingSetting;
import com.zemlyak.web.segmentapp.model2.Segment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@SpringBootApplication
public class SegmentAppApplication  {
	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String SUM_STAT_BY_ALL_COUNTRIES_KEY = "";
	private static final Random RAND = new Random(System.nanoTime());

	@Autowired
	private SegmentRepository segmentRepository;

	@Autowired
	private CountryStatsRepository statsRepository;

	@Autowired
	private TrackingSettingRepository trackingSettingRepository;

	@Autowired
	private CountryStatsUpdateRepository statsUpdateRepository;

	@GetMapping(path = "/settings/tracking")
	public List<TrackingSetting> findAll() {
		return trackingSettingRepository.findAll();
	}

	@GetMapping(path = "/settings/tracking/metric/{metricId}")
	public List<TrackingSetting> findByMetricId(@PathVariable("metricId") Integer metricId) {
		return trackingSettingRepository.findByMetricIdIn(Collections.singleton(metricId));
	}


	@GetMapping(path = "/segment/{id}")
	public Segment findSegmentById(@PathVariable("id") Integer id) {
		return segmentRepository.findById(id).orElse(null);
	}

	@GetMapping(path = "/segments/name/{name}")
	public List<SegmentProjection> findSegmentById(@PathVariable("name") String name) {
		return segmentRepository.findByNameLike("%" + name + "%");
	}

	@Transactional
	@GetMapping(path = "/segment/{id}/stats/generate")
	public Segment generateStats(@PathVariable("id") Integer id) {
		Set<com.zemlyak.web.segmentapp.model2.CountryStat.Key> existedKeys = statsUpdateRepository.findAll().stream().map(com.zemlyak.web.segmentapp.model2.CountryStat::getId).collect(Collectors.toSet());
		List<com.zemlyak.web.segmentapp.model2.CountryStat> generatedCountries = new ArrayList<>(200);
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

	@GetMapping(path = "/segments")
	public List<Segment> findSegment() {
		return segmentRepository
			.findAll(SegmentRepository
				.withCountryStat("")
				.and(SegmentRepository.hasType(1)));
    }

    @GetMapping(path = "/segments/type2/{id}")
	public List<Segment> findSegmentByType2Id(@PathVariable("id") Integer typeId) {
		return segmentRepository.findAll(SegmentRepository.hasType(typeId));
	}


	@GetMapping(path = "/segments/stats")
	public List<CountryStat> segmentStats(@RequestParam(value = "dataProviderId", required = false) Integer dataProviderId,
										  @RequestParam(value = "typeId", required = false) Integer typeId,
										  @RequestParam(value = "searchType", required = false) String searchType,
										  @RequestParam(value = "search", required = false) String search) {

		boolean hasFilter = false;
		CountryStatsRepository.SpecificationBuilder conditionBuilder = conditionBuilder();
		if (dataProviderId != null) {
			hasFilter = true;
			conditionBuilder.hasDataProvider(dataProviderId);
		}
		if (typeId != null) {
			hasFilter = true;
			conditionBuilder.hasType(typeId);
		}
		if (searchType != null) {
			hasFilter = true;
			conditionBuilder.likeType(searchType);
		}
		if (search != null) {
			hasFilter = true;
			conditionBuilder.hasSubstrInAnyName(search);
		}

		if (hasFilter) {
			return statsRepository.findAll(conditionBuilder.build());
		} else {
			return statsRepository
				.findAll(conditionBuilder.build(), PageRequest.of(0, 2, JpaSort.of(Sort.Direction.ASC, JpaSort.path(CountryStat_.segment).dot(Segment_.id))))
				.getContent();
		}
	}


	private static CountryStatsRepository.SpecificationBuilder conditionBuilder() {
		return conditionBuilder(SUM_STAT_BY_ALL_COUNTRIES_KEY);
	}

	private static CountryStatsRepository.SpecificationBuilder conditionBuilder(String country) {
		return CountryStatsRepository.SpecificationBuilder.forCountry(country);
	}

	private static com.zemlyak.web.segmentapp.model2.CountryStat statForCountryWithRandomValues(Integer id, String country, Set<com.zemlyak.web.segmentapp.model2.CountryStat.Key> existedKeys) {
		com.zemlyak.web.segmentapp.model2.CountryStat countryStat = new com.zemlyak.web.segmentapp.model2.CountryStat();
		countryStat.setSegmentId(id);
		countryStat.setCountryCode(country);
		countryStat.setActiveProfilesAmount((long)RAND.nextInt(1000));
		countryStat.setSleepingProfilesAmount((long)RAND.nextInt(1000));
		countryStat.setNew(!existedKeys.contains(countryStat.getId()));
		return countryStat;
	}


	public static void main(String[] args) {
		SpringApplication.run(SegmentAppApplication.class, args);
	}

}
