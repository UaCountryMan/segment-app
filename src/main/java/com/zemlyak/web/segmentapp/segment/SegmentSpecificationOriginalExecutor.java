package com.zemlyak.web.segmentapp.segment;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SegmentSpecificationOriginalExecutor {
    List<Segment> findAllOriginal(Specification<Segment> specification);
}
