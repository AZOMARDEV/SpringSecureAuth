package com.mistervoiture.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Devices {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "_id", unique = true, nullable = false, length = 36)
    private String _id;
    private String uuid;
    private String userAgent;
    private boolean isBrowser;
    private String language;
    private String ip;
    private double lat;
    private double lng;
    private String continent;
    private String continentCode;
    private String countryName;
    private String countryCode;
    private String principalSubdivision;
    private String principalSubdivisionCode;
    private String city;
    private String theme;
}
