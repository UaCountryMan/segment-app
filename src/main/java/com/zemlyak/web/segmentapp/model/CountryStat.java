package com.zemlyak.web.segmentapp.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CountryStat that = (CountryStat) o;
        return getSegmentId() != null && Objects.equals(getSegmentId(), that.getSegmentId())
                && getCountryCode() != null && Objects.equals(getCountryCode(), that.getCountryCode());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(segmentId, countryCode);
    }
}