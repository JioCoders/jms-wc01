package com.harry.userservice.entity;

import static com.harry.userservice.utils.DbConstant.TABLE_LOCATION;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = TABLE_LOCATION)
public class Location {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer locationId;

    @Column(name = "name")
    private String locationName;

    private Double latitude;
    private Double longitude;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "company_id")
    private Integer companyId;

}
