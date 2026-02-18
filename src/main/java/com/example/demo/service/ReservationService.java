package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.demo.mapper.ReservationMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ReservationService {
	
	 private final ReservationMapper reservationMapper;
	 
	 public void getReservations(Model model) {
		 
	 }
	 
	 public void reserve() {
		 
	 }
}
