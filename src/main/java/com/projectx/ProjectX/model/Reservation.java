package com.projectx.ProjectX.model;

import com.projectx.ProjectX.enums.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "reservation")
@Getter
@Setter
public class Reservation extends BaseEntity{

    @Id
    @SequenceGenerator(name = "reservation_seq", sequenceName = "reservation_seq")
    @GeneratedValue(generator = "reservation_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
