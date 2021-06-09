package com.projectx.ProjectX.model;

import javax.persistence.*;

import com.projectx.ProjectX.enums.EstateType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "estate")
@Getter
@Setter
public class Estate extends BaseEntity {
    @Id
    @SequenceGenerator(name = "estate_seq", sequenceName = "estate_seq")
    @GeneratedValue(generator = "estate_seq")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "num_of_bedrooms")
    private Integer numOfBedrooms;

    @Column(name = "area")
    private Double area;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EstateType type;

    @Column(name = "rent_price")
    private Double rentPrice;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @Column(name = "description")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "estate_picture",
            joinColumns = {
            @JoinColumn(
                    name = "estate_id",
                    referencedColumnName = "id")
    })
    private Set<String> pictures;
}
