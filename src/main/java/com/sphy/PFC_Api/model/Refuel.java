package com.sphy.PFC_Api.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "refuels")
public class Refuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    /*@NotNull(message = "station name can´t be NULL")
    @NotBlank(message = "station name can´t be empty")*/
    private String nameStation;
    @Column
    /*@NotNull(message = "vehicleLicense can´t be NULL")
    @NotBlank(message = "vehicleLicense can´t be empty")*/
    private String nameVehicle;
    @Column
    @NotNull(message = "Fuel can´t be NULL")
    @NotBlank(message = "Fuel can´t be empty")
    private String fuel;
    @Column
    @Positive(message = "Amount can´t be negative")
    private float amount;
    @Column
    @Positive(message = "Price can´t be negative")
    private float price;
    @Column
    private int kmTotal;
    @Column
    private int kmTraveled;
    @Column
    private boolean fulled;
    @Column(name = "creation_date")
    private String creationDate;

    @Column
    private float refuelConsumption;
    @Column
    private float refueledLiters;
    @Column
    private float medConsumption;



    /*@Column
    @NotNull(message = "Station can´t be NULL")
    private Long stationId;*/


    @JsonBackReference("refuel_vehicle")
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @JsonBackReference("refuel_station")
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;


    /*@JsonBackReference("refuel_vehicle")
    @ManyToMany
    @JoinTable(name = "vehicle_refuels",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "refuel_id"))
    private Refuel refuel;*/





}