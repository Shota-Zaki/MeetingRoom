package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Reservation;
import com.example.demo.mapper.ReservationMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ReservationService {
	
	 private final ReservationMapper reservationMapper;
	 
	 
	 public List<Reservation> findReservationByDate(LocalDate date){
		return reservationMapper.getAllReservationsByDate(date);
	 } 
	 
	public void reserve(Reservation reservation) {
		 reservationMapper.insertReservation(reservation);
	 }
	
	/*
	 * すべての予約情報の取得
	* */
	public List<Reservation> getAllReservations(){
		return reservationMapper.getAllReservations();
	}
	
}
