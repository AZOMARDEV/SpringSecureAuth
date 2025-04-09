package com.mistervoiture.auth.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Device {
    private String userAgent;
    private String language;
    private String ipAddress;
    private String ipType;
    private boolean isBrowser;
    private String browserVersion;
    private String browserName;
    private String osName;
    private String osVersion;
    private boolean isMobileOnly;
    private String engineName;
    private String uuid;

}
