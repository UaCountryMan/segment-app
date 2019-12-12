package com.zemlyak.web.segmentapp;

import com.zemlyak.web.segmentapp.model2.CountryStat;
import com.zemlyak.web.segmentapp.model2.CountryStat_;
import com.zemlyak.web.segmentapp.model2.Segment;
import com.zemlyak.web.segmentapp.model.SegmentType_;
import com.zemlyak.web.segmentapp.model2.Segment_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import java.util.List;

public interface SegmentRepository extends JpaRepository<Segment, Integer>, JpaSpecificationExecutor<Segment> {

    @Query(
        "SELECT s FROM Segment s " +
            "JOIN FETCH s.dataProvider " +
            "JOIN FETCH s.segmentType " +
            "WHERE s.segmentType.id = ?1")
    List<Segment> findByType(Integer typeId);

    @Override
    @EntityGraph(attributePaths = {"dataProvider", "segmentType", "countryStats"})
    List<Segment> findAll(Specification<Segment> specification);

    static Specification<Segment> hasType(Integer typeId) {
        return (segmentRoot, cq, cb) -> {
            segmentRoot.fetch(Segment_.segmentType);
            segmentRoot.fetch(Segment_.dataProvider);
            return cb.equal(segmentRoot.get(Segment_.segmentType).get(SegmentType_.id), typeId);
        };
    }

    static Specification<Segment> withCountryStat(String countryId) {
        return (segmentRoot, cq, cb) -> {
            ListJoin<Segment, CountryStat> join = segmentRoot.join(Segment_.countryStats, JoinType.LEFT);
            join.on(cb.equal(join.get(CountryStat_.countryCode), countryId));
            return cb.isNotNull(segmentRoot.get(Segment_.id));
        };
    }

}
