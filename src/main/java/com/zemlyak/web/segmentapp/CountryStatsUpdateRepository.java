package com.zemlyak.web.segmentapp;

import com.zemlyak.web.segmentapp.model2.CountryStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryStatsUpdateRepository extends JpaRepository<CountryStat, CountryStat.Key> {
}
