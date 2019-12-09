package com.zemlyak.web.segmentapp;

import com.zemlyak.web.segmentapp.model.CountryStat;
import com.zemlyak.web.segmentapp.model.CountryStat_;
import com.zemlyak.web.segmentapp.model.DataProvider_;
import com.zemlyak.web.segmentapp.model.SegmentType_;
import com.zemlyak.web.segmentapp.model.Segment_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CountryStatsRepository extends JpaRepository<CountryStat, CountryStat.Key>, JpaSpecificationExecutor<CountryStat> {

    @Override
    @EntityGraph(attributePaths = {"segment.dataProvider", "segment.segmentType"})
    List<CountryStat> findAll(Specification<CountryStat> spec);

    @Override
    @EntityGraph(attributePaths = {"segment.dataProvider", "segment.segmentType"})
    Page<CountryStat> findAll(Specification<CountryStat> spec, Pageable pageable);

    class SpecificationBuilder {
        private Specification<CountryStat> specification;

        private SpecificationBuilder(String country) {
            specification = (statsRoot, cq, cb) -> cb.equal(statsRoot.get(CountryStat_.countryCode), country);
        }

        public static SpecificationBuilder forCountry(String country) {
            return new SpecificationBuilder(country);
        }

        public SpecificationBuilder hasDataProvider(Integer dataProviderId) {
            specification = specification.and((statsRoot, cq, cb) -> cb.equal(statsRoot.get(CountryStat_.segment).get(Segment_.dataProvider).get(DataProvider_.id), dataProviderId));
            return this;
        }

        public SpecificationBuilder hasType(Integer typeId) {
            specification = specification.and((statsRoot, cq, cb) -> cb.equal(statsRoot.get(CountryStat_.segment).get(Segment_.segmentType).get(SegmentType_.id), typeId));
            return this;
        }

        public SpecificationBuilder likeType(String searchedType) {
            specification = specification.and((statsRoot, cq, cb) -> cb.like(statsRoot.get(CountryStat_.segment).get(Segment_.segmentType).get(SegmentType_.name), "%" + searchedType + "%"));
            return this;
        }

        public Specification<CountryStat> build() {
            return specification;
        }
    }
}
