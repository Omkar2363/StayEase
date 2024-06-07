package com.org.StayEase.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is mandatory")
    @Column(unique = true)
    private String name;

    @NotNull(message = "Location can not be null")
    private String location;

    @NotNull(message = "Description can not be null")
    private String description;

    @NotNull(message = "No of availableRooms can not be null")
    private int availableRooms;

}


