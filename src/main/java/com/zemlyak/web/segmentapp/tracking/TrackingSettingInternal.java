package com.zemlyak.web.segmentapp.tracking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@ToString(callSuper = true)
@Table(name = "tracking_settings_internal")
@PrimaryKeyJoinColumn(name = "tracking_setting_id")
@Polymorphism(type = PolymorphismType.EXPLICIT)
public class TrackingSettingInternal extends TrackingSetting {

    @Column(name = "distinction_type")
    private Integer distinctionType;
}
