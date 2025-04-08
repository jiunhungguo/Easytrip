package com.demo.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="Seats")
public class Seat {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer seatId;
	@ManyToOne
	@JoinColumn(name = "flight_id", referencedColumnName = "id") // 外鍵 ticket_id
    private Flight flight;
    
    private String seatNumber;
    private String seatClass;
    @ManyToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "ticketId") // 外鍵 ticket_id
    private Ticket ticket;
    private boolean isBooked;
    
 // 構造函數
    public Seat(Flight flight, String seatNumber, String seatClass, boolean isBooked) {
        this.flight = flight;
        this.seatNumber = seatNumber;
        this.seatClass = seatClass;
        this.isBooked = isBooked;
    }
}
