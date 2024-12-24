package com.zemlyak.web.segmentapp.stat;

import com.zemlyak.web.segmentapp.common.DataProvider;
import com.zemlyak.web.segmentapp.common.DataProvider_;
import com.zemlyak.web.segmentapp.common.SegmentType;
import com.zemlyak.web.segmentapp.common.SegmentType_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Objects;

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
            specification = specification.and((statsRoot, cq, cb) -> {
                Join<CountryStat, Segment> segmentJoin = join(statsRoot, CountryStat_.segment);
                Join<Segment, SegmentType> segmentTypeJoin = join(segmentJoin, Segment_.segmentType);
                return cb.like(segmentTypeJoin.get(SegmentType_.name), "%" + searchedType + "%");
            });
            return this;
        }

        public SpecificationBuilder hasSubstrInAnyName(String searchedPart) {
            specification = specification
                .and((statsRoot, cq, cb) -> {
                    Join<CountryStat, Segment> segmentJoin = join(statsRoot, CountryStat_.segment);
                    Join<Segment, DataProvider> dataProviderJoin = join(segmentJoin, Segment_.dataProvider);
                    Join<Segment, SegmentType> segmentTypeJoin = join(segmentJoin, Segment_.segmentType);
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

        @SuppressWarnings("unchecked")
        private static <J,T> Join<J, T> join(From<?, J> from, SingularAttribute<J, T> attribute) {
            return from
                .getJoins()
                .stream()
                .filter(join -> Objects.equals(join.getAttribute(), attribute))
                .map(o -> (Join<J, T>)o)
                .findFirst()
                .orElseGet(() -> from.join(attribute, JoinType.LEFT));
        }
    }
}
