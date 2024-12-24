package com.zemlyak.web.segmentapp.segment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryStatsUpdateRepository extends JpaRepository<CountryStat, CountryStat.Key> {
}
