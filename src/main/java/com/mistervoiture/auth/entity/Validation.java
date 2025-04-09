package com.mistervoiture.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Validation {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "_id", unique = true, nullable = false, length = 36)
    private String _id;
    private String type;
    private String username;
    private String code;
    private boolean activation;
    private boolean experationStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private Date experationDate;

    public static Date AddExperationDate(int expiration){
        // Set a specific expiration date, e.g., one year from the creation date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, expiration); // Add MINUTE to the creation date
        return calendar.getTime();
    }

}