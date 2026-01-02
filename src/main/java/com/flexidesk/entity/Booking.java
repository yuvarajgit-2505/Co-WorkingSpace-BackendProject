package com.flexidesk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private long id;
    private LocalDate startdate;
    private LocalDate enddate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "desk_id")
    private Desk desk;
    private LocalDateTime bookingTime;

}
