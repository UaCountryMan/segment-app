package com.zemlyak.web.segmentapp.model2;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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