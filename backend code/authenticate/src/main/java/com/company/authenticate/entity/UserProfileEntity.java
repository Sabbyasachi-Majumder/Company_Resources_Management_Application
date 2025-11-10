package com.company.authenticate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UserProfileTable")
public class UserProfileEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId", nullable = false)
    private long userId;

    @Column(name = "UserName" , unique = true, nullable = false)
    private String userName;

    @Column(name = "Password" , nullable = false)
    private String password;

    @Column(name = "Enabled" , nullable = false)
    private boolean enabled;

    @Column(name = "Role" , nullable = false)
    private String role;
}
