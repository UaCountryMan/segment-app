package com.zemlyak.web.segmentapp.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "country_stats")
@IdClass(CountryStat.Key.class)
public class CountryStat {

    @Id
    @Column(name = "segment_id", insertable = false, updatable = false)
    private Integer segmentId;

    @Id
    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "active_profiles_amount")
    private Long activeProfilesAmount;

    @Column(name = "sleeping_profiles_amount")
    private Long sleepingProfilesAmount;

    @ManyToOne
    @JoinColumn(name = "segment_id", referencedColumnName = "id")
    private Segment segment;

    @Data
    public static class Key implements Serializable {
        private Integer segmentId;
        private String countryCode;
    }
}