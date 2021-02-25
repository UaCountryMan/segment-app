package com.zemlyak.web.segmentapp.model2;

import lombok.Data;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "country_stats2")
@Table(name = "country_stats")
@IdClass(CountryStat.Key.class)
@SelectBeforeUpdate(value=false)
public class CountryStat implements Persistable<CountryStat.Key> {
    @Transient
    private boolean isNew;

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

    @Override
    public Key getId() {
        return Key.fromStat(this);
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Data
    public static class Key implements Serializable {
        private Integer segmentId;
        private String countryCode;

        public static Key fromStat(CountryStat stat) {
            Key key = new Key();
            key.setSegmentId(stat.getSegmentId());
            key.setCountryCode(stat.getCountryCode());
            return key;
        }
    }
}