package com.sphy.PFC_Api.dto;

import com.sphy.PFC_Api.model.Refuel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationDTO {

    private long id;

    private String name;

    private String address;


    private String registrationDate;

    private boolean favorite = false;

    private boolean glpFuel = false;

    private int refuels = 0;


}
