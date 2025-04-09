package com.mistervoiture.auth.request;

import com.mistervoiture.auth.beans.GPS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceReq {
    private String uuid;
    private String userAgent;
    private boolean isBrowser;
    private String language;
    private String ip;
    private GPS gps;
    private GPS currentGPS;
    private String continent;
    private String continentCode;
    private String countryName;
    private String countryCode;
    private String principalSubdivision;
    private String principalSubdivisionCode;
    private String city;
}