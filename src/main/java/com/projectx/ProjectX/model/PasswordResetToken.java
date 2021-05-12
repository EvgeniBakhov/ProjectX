package com.projectx.ProjectX.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pass_reset_token")
@Getter
@Setter
public class PasswordResetToken {

    private static final int EXPIRATION = 24 * 60;

    @Id
    @SequenceGenerator(name = "pass_reset_token_seq", sequenceName = "pass_reset_token_seq")
    @GeneratedValue(generator = "pass_reset_token_seq")
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "expiry_date")
    private Date expiryDate;
}
