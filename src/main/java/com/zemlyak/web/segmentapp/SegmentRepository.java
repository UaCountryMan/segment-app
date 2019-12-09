package com.zemlyak.web.segmentapp;

import com.zemlyak.web.segmentapp.model.Segment;
import com.zemlyak.web.segmentapp.model.SegmentType_;
import com.zemlyak.web.segmentapp.model.Segment_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SegmentRepository extends JpaRepository<Segment, Integer>, JpaSpecificationExecutor<Segment> {

    @Query(
        "SELECT s FROM Segment s " +
            "JOIN FETCH s.dataProvider " +
            "JOIN FETCH s.segmentType " +
            "WHERE s.segmentType.id = ?1")
    List<Segment> findByType(Integer typeId);

    static Specification<Segment> hasType(Integer typeId) {
        return (segmentRoot, cq, cb) -> {
            segmentRoot.fetch(Segment_.segmentType);
            segmentRoot.fetch(Segment_.dataProvider);
            return cb.equal(segmentRoot.get(Segment_.segmentType).get(SegmentType_.id), typeId);
        };
    }

}
