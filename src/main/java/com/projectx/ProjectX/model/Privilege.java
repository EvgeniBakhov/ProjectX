package com.projectx.ProjectX.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "privilege")
@Getter
@Setter
public class Privilege extends BaseEntity{

    @Id
    @SequenceGenerator(name = "privilege_seq", sequenceName = "privilege_seq")
    @GeneratedValue(generator = "privilege_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;
}
