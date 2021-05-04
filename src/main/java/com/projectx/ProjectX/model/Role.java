package com.projectx.ProjectX.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table("role")
@Getter
@Setter
public class Role extends BaseEntity{

    @Id
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq")
    @GeneratedValue(generator = "role_seq")
    private Long id;

    @Column("name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;
}
