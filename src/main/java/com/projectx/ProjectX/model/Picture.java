package com.projectx.ProjectX.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "picture")
@Getter
@Setter
public class Picture{
    @Id
    @SequenceGenerator(name = "picture_seq", sequenceName = "picture_seq")
    @GeneratedValue(generator = "picture_seq")
    private long id;

    @Column(name = "url")
    private String url;

    @Column(name = "position")
    private int position;
}
