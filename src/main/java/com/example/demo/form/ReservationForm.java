package com.example.demo.form;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReservationForm {
	int roomId;

	String start;

	String end;

	LocalDate date;
}
