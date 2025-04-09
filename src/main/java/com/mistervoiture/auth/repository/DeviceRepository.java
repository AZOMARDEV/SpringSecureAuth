package com.mistervoiture.auth.repository;

import com.mistervoiture.auth.entity.Devices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Devices, String> {
    @Query("SELECT d FROM Devices d WHERE d.uuid = :uuid")
    Optional<Devices> GetDevicesByUuid(@Param("uuid") String uuid);
    @Modifying
    @Query("UPDATE Devices d SET d.language = :language WHERE d.uuid = :uuid")
    void updateLanguageDevice(@Param("language") String language, @Param("uuid") String uuid);
    @Modifying
    @Query("UPDATE Devices d SET d.lat = :lat , d.lng = :lng WHERE d.uuid = :uuid")
    void updateLocationDevice(@Param("lat") double lat, @Param("lng") double lng, @Param("uuid") String uuid);
    @Modifying
    @Query("UPDATE Devices d SET d.theme = :theme WHERE d.uuid = :uuid")
    void updateThemeDevice(@Param("theme") String theme, @Param("uuid") String uuid);
}