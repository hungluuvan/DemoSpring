package com.mor.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String streetName;
    @NotBlank
    private String city;
    @NotBlank
    private String country;
    @NotBlank
    private String town;
    @NotBlank
    private String ward;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "Id")
    @JsonIgnore
    private User user;

    public Address(String streetName, String city, String country, String town, String ward) {
        this.streetName = streetName;
        this.city = city;
        this.country = country;
        this.town = town;
        this.ward = ward;
    }
}
