package com.mistervoiture.auth.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetaData {
    private Device device;
    private Location location;
}
