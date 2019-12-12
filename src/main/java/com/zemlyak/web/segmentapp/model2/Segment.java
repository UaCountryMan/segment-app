package com.zemlyak.web.segmentapp.model2;

import com.zemlyak.web.segmentapp.model.DataProvider;
import com.zemlyak.web.segmentapp.model.SegmentType;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "segments2")
@Table(name = "segments")
public class Segment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "data_provider")
    private DataProvider dataProvider;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private SegmentType segmentType;

    @OneToMany
    @JoinColumn(name="segment_id")
    private List<CountryStat> countryStats;

}