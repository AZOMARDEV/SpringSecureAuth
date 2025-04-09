package com.mistervoiture.auth.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {
    private GPS gps;
    private String continent;
    private String continentCode;
    private String countryName;
    private String countryCode;
    private String city;
    private String locality;
}
