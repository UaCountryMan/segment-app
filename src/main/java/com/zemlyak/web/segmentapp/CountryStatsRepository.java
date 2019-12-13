package com.zemlyak.web.segmentapp;

import com.zemlyak.web.segmentapp.model.CountryStat;
import com.zemlyak.web.segmentapp.model.CountryStat_;
import com.zemlyak.web.segmentapp.model.DataProvider;
import com.zemlyak.web.segmentapp.model.DataProvider_;
import com.zemlyak.web.segmentapp.model.Segment;
import com.zemlyak.web.segmentapp.model.SegmentType;
import com.zemlyak.web.segmentapp.model.SegmentType_;
import com.zemlyak.web.segmentapp.model.Segment_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import java.util.List;

public interface CountryStatsRepository extends JpaRepository<CountryStat, CountryStat.Key>, JpaSpecificationExecutor<CountryStat> {

    @Override
    @EntityGraph(attributePaths = {"segment", "segment.dataProvider", "segment.segmentType"})
    List<CountryStat> findAll(Specification<CountryStat> spec);

    @Override
    @EntityGraph(attributePaths = {"segment", "segment.dataProvider", "segment.segmentType"})
    Page<CountryStat> findAll(Specification<CountryStat> spec, Pageable pageable);

    class SpecificationBuilder {
        private Specification<CountryStat> specification;

        private SpecificationBuilder(String country) {
            specification = (statsRoot, cq, cb) -> {
//                Join<CountryStat, Segment> segmentJoin = statsRoot.join(CountryStat_.segment, JoinType.LEFT);
//                Join<Segment, DataProvider> dataProviderJoin = segmentJoin.join(Segment_.dataProvider, JoinType.LEFT);
//                Join<Segment, SegmentType> segmentTypeJoin = segmentJoin.join(Segment_.segmentType, JoinType.LEFT);
                return cb.equal(statsRoot.get(CountryStat_.countryCode), country);
            };
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
            specification = specification.and((statsRoot, cq, cb) -> {
                Join<CountryStat, Segment> segmentJoin = statsRoot.join(CountryStat_.segment, JoinType.LEFT);
                Join<Segment, SegmentType> segmentTypeJoin = segmentJoin.join(Segment_.segmentType, JoinType.LEFT);
                return cb.like(segmentTypeJoin.get(SegmentType_.name), "%" + searchedType + "%");
            });
            return this;
        }

        public SpecificationBuilder hasSubstrInAnyName(String searchedPart) {
            specification = specification
                .and((statsRoot, cq, cb) -> {
                    Join<CountryStat, Segment> segmentJoin = statsRoot.join(CountryStat_.segment, JoinType.LEFT);
                    Join<Segment, DataProvider> dataProviderJoin = segmentJoin.join(Segment_.dataProvider, JoinType.LEFT);
                    Join<Segment, SegmentType> segmentTypeJoin = segmentJoin.join(Segment_.segmentType, JoinType.LEFT);
                    Expression<String> dataProviderName = cb.concat(dataProviderJoin
                        .get(DataProvider_.name), " > ");
                    Expression<String> typeViewName = cb.concat(segmentTypeJoin
                        .get(SegmentType_.viewName), " > ");
                    Path<String> segmentName = segmentJoin
                        .get(Segment_.name);
                    return cb.like(cb.concat(cb.concat(dataProviderName, typeViewName), segmentName), "%" + searchedPart + "%");
                });
            return this;
        }

        public Specification<CountryStat> build() {
            return specification;
        }
    }
}
