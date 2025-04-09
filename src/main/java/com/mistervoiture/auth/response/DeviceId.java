package com.mistervoiture.auth.response;

import com.mistervoiture.auth.beans.GPS;
import lombok.Builder;

@Builder
public record DeviceId(
        String _id,
        String uuid,
        String language,
        GPS gps,
        String theme
){ }