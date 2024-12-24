package com.zemlyak.web.segmentapp;

import com.zemlyak.web.segmentapp.stat.CountryStat;
import com.zemlyak.web.segmentapp.stat.CountryStat_;
import com.zemlyak.web.segmentapp.stat.CountryStatsRepository;
import com.zemlyak.web.segmentapp.stat.Segment_;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CountryStatController {
    private static final String SUM_STAT_BY_ALL_COUNTRIES_KEY = "";

    private final CountryStatsRepository statsRepository;

    @GetMapping(path = "/country-statistics")
    public List<CountryStat> segmentStats(@RequestParam(value = "dataProviderId", required = false) Integer dataProviderId,
                                          @RequestParam(value = "typeId", required = false) Integer typeId,
                                          @RequestParam(value = "searchType", required = false) String searchType,
                                          @RequestParam(value = "search", required = false) String search) {
        CountryStatsRepository.SpecificationBuilder conditionBuilder = CountryStatsRepository.SpecificationBuilder
                .forCountry(SUM_STAT_BY_ALL_COUNTRIES_KEY);
        boolean hasFilter = false;
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
}
