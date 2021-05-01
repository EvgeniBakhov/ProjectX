package com.projectx.ProjectX.model;

import com.projectx.ProjectX.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "booking")
@Getter
@Setter
public class Booking extends BaseEntity {

    @Id
    @SequenceGenerator(name = "booking_seq", sequenceName = "booking_seq")
    @GeneratedValue(generator = "booking_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estate_id", referencedColumnName = "id")
    private Estate estate;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "total_price")
    private Double totalPrice;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
