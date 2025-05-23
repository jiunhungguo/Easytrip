package com.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="Airports")
public class Airports {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	
    private Integer airportsId;

    
    private String iataCode;

   
    private String airportName;

   
    private String countryRegion;

    
    private String city;

    
    private Double latitude;

   
    private Double longitude;
	
	
}
