package com.projectx.ProjectX.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Getter
@Setter
public class Address extends BaseEntity {
    @Id
    @SequenceGenerator(name = "address_seq", sequenceName = "address_seq")
    @GeneratedValue(generator = "address_seq")
    private Long id;

    @Column(name = "reqion")
    private String region;

    @Column(name = "country")
    private String country;

    @Column(name = "subdivision")
    private String subdivision;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "additional")
    private String additionalDetails;
}
