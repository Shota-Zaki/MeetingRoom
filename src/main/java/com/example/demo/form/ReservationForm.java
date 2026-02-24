package com.example.demo.form;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReservationForm {
	String roomId;

	String start;

	LocalDate date;
}
