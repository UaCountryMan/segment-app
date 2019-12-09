package com.zemlyak.web.segmentapp;

import com.zemlyak.web.segmentapp.model.CountryStat;
import com.zemlyak.web.segmentapp.model.Segment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SpringBootApplication
public class SegmentAppApplication  {
	private static final String SUM_STAT_BY_ALL_COUNTRIES_KEY = "";

	@Autowired
	private SegmentRepository segmentRepository;

	@Autowired
	private CountryStatsRepository statsRepository;



	@GetMapping(path = "/segment/{id}")
	public Segment findSegmentById(@PathVariable("id") Integer id) {
		return segmentRepository.findById(id).orElse(null);
	}

	@GetMapping(path = "/segments/type/{id}")
	public List<Segment> findSegmentByTypeId(@PathVariable("id") Integer typeId) {
		return segmentRepository.findByType(typeId);
	}

	@GetMapping(path = "/segments/type2/{id}")
	public List<Segment> findSegmentByType2Id(@PathVariable("id") Integer typeId) {
		return segmentRepository.findAll(SegmentRepository.hasType(typeId));
	}


	@GetMapping(path = "/segments/stats")
	public List<CountryStat> segmentStats(@RequestParam(value = "dataProviderId", required = false) Integer dataProviderId,
										  @RequestParam(value = "typeId", required = false) Integer typeId,
										  @RequestParam(value = "searchType", required = false) String searchType) {

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

		if (hasFilter) {
			return statsRepository.findAll(conditionBuilder.build());
		} else {
			return statsRepository
				.findAll(conditionBuilder.build(), PageRequest.of(0, 2))
				.getContent();
		}
	}


	private static CountryStatsRepository.SpecificationBuilder conditionBuilder() {
		return conditionBuilder(SUM_STAT_BY_ALL_COUNTRIES_KEY);
	}

	private static CountryStatsRepository.SpecificationBuilder conditionBuilder(String country) {
		return CountryStatsRepository.SpecificationBuilder.forCountry(country);
	}


	public static void main(String[] args) {
		SpringApplication.run(SegmentAppApplication.class, args);
	}

}
