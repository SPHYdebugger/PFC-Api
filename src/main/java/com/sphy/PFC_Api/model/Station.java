package com.sphy.PFC_Api.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "stations")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @NotNull(message = "Name can´t be NULL")
    @NotBlank(message = "Name can´t be empty")
    private String name;
    @Column
    @NotNull(message = "Address can´t be NULL")
    @NotBlank(message = "Address can´t be empty")
    private String address;

    @Column(name = "registration_date")
    private String registrationDate;
    @Column
    private boolean favorite = false;
    @Column
    private boolean glpFuel = false;

    @OneToMany(mappedBy = "station")
    private List<Refuel> refuels;


}