package com.zemlyak.web.segmentapp.segment;

import com.zemlyak.web.segmentapp.common.DataProvider;
import com.zemlyak.web.segmentapp.common.SegmentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
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
    @ToString.Exclude
    private List<CountryStat> countryStats;

    protected Segment(Integer id, String name, Boolean active, Integer dataProviderId, String dataProviderName,
                      Integer segmentTypeId, String segmentTypeName, String segmentTypeViewName, String countryCode,
                      Long activeProfilesAmount, Long sleepingProfilesAmount) {
        this();
        this.id = id;
        this.name = name;
        this.active = active;
        this.dataProvider = new DataProvider(dataProviderId, dataProviderName);
        this.segmentType = new SegmentType(segmentTypeId, segmentTypeName, segmentTypeViewName);
        if (countryCode != null) {
            this.countryStats = new ArrayList<>(1);
            this.countryStats.add(new CountryStat(
                    id,
                    countryCode,
                    activeProfilesAmount,
                    sleepingProfilesAmount));
        } else {
            this.countryStats = new ArrayList<>(0);
        }


    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Segment segment = (Segment) o;
        return getId() != null && Objects.equals(getId(), segment.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}