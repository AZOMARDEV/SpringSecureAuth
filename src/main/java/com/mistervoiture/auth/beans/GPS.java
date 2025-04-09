package com.mistervoiture.auth.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GPS {
    private double lat;
    private double lng;
}