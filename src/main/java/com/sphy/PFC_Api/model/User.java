package com.sphy.PFC_Api.model;



import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long user_id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column
    private String name;
    @Column
    private String surname;
    @Column(nullable = false)
    private String email;
    @Column
    private String address;
    @Column
    private String city;
    @Column
    private String province;
    @Column
    private String country;

    @Column(name = "creation_date")
    private LocalDate creationDate;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    @Column(name = "active")
    private boolean active;






    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                '}';
    }
}