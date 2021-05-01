package com.projectx.ProjectX.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "event")
public class Event extends BaseEntity {
    @Id
    @SequenceGenerator(name = "event_seq", sequenceName = "event_seq")
    @GeneratedValue(generator = "event_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @OneToOne()
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne()
    @JoinColumn(name = "organizer_id", referencedColumnName = "id")
    private User user;
}
