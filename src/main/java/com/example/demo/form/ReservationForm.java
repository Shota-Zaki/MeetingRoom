package com.example.demo.form;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReservationForm {
	/*
	 * 会議室ID
	 * */
	String roomId;
	
	/*
	 * 利用開始時間
	 * */
	String start;
	
	/*
	 * 利用日
	 * */
	LocalDate date;
}
