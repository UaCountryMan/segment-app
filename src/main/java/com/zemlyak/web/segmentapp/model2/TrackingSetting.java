package com.zemlyak.web.segmentapp.model2;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tracking_settings")
@Inheritance(strategy = InheritanceType.JOINED)
public class TrackingSetting {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "metric_id")
    private Integer metricId;

}
