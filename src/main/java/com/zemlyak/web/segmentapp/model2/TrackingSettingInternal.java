package com.zemlyak.web.segmentapp.model2;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "tracking_settings_internal")
@PrimaryKeyJoinColumn(name = "tracking_setting_id")
@Polymorphism(type = PolymorphismType.EXPLICIT)
public class TrackingSettingInternal extends TrackingSetting {

    @Column(name = "distinction_type")
    private Integer distinctionType;

}
