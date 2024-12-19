package com.zemlyak.web.segmentapp;

import com.zemlyak.web.segmentapp.model2.Segment;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SegmentSpecificationOriginalExecutor {
    List<Segment> findAllOriginal(Specification<Segment> specification);
}
