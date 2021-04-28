package com.projectx.ProjectX.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "address")
@Getter
@Setter
public class Address {
    @Id
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
