package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Reservation;
import com.example.demo.mapper.ReservationMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ReservationService {
	
	 private final ReservationMapper reservationMapper;
	 
	 
//	 public List<Reserve> findReservationByDate(LocalDate date){
//		 List<Reservation> reservations = reservationMapper.getAllReservationsByDate(date);
//	 } 

	public void reserve() {
		 
	 }
	
	//---------------管理者機能------------------------
	
	/*
	 * ユーザー情報の取得
	* */
	public List<Reservation> getAllReservations(){
		return reservationMapper.getAllReservations();
	}
}
