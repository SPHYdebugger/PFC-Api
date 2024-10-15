package com.sphy.PFC_Api.model;




import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @NotNull(message = "License plate can´t be NULL")
    @NotBlank(message = "License plate can´t be empty")
    private String licensePlate;
    @Column
    @NotNull(message = "Brand can´t be NULL")
    @NotBlank(message = "Brand can´t be empty")
    private String brand;
    @Column
    @NotNull(message = "Model can´t be NULL")
    @NotBlank(message = "Model can´t be empty")
    private String model;
    @Column
    @NotNull(message = "Fuel1 can´t be NULL")
    @NotBlank(message = "Fuel1 can´t be empty")
    private String fuel1;
    @Column
    private String fuel2;
    @Column
    @NotNull(message = "Initial kilometers can´t be NULL")
    @Positive(message = "Initial Kilimeters can´t be negative")
    private int kmInit;



    @OneToMany(mappedBy = "vehicle")
    private List<Refuel> refuels;
}