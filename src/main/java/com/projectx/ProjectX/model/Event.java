package com.projectx.ProjectX.model;

import com.projectx.ProjectX.enums.PegiRestrictions;
import com.projectx.ProjectX.enums.EventPlaceType;
import com.projectx.ProjectX.enums.EventStatus;
import com.projectx.ProjectX.enums.EventType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "event")
@Getter
@Setter
public class Event extends BaseEntity {
    @Id
    @SequenceGenerator(name = "event_seq", sequenceName = "event_seq")
    @GeneratedValue(generator = "event_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "available_seats")
    private int availableSeats;

    @Column(name = "age_restrictions")
    @Enumerated(EnumType.STRING)
    private PegiRestrictions ageRestrictions;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne()
    @JoinColumn(name = "organizer_id", referencedColumnName = "id")
    private User organizer;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EventType type;

    @Column(name = "place_type")
    @Enumerated(EnumType.STRING)
    private EventPlaceType placeType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(name = "thumbnail")
    private String thumbnail;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "event_picture",
            joinColumns = {
            @JoinColumn(
                    name = "event_id",
                    referencedColumnName = "id")
    })
    private Set<String> pictures;
}

