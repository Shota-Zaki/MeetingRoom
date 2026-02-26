package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Reservation;
import com.example.demo.mapper.ReservationMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {
	
	private final ReservationMapper reservationMapper;
	
	@GetMapping("/reserve")
	public String showReserve(Model model) {
		//ReservationService.getReservations(model);
		model.addAttribute("reservations",reservationMapper.getAllReservations()); 
		return "reserve/reserveInput";
	}
	
	@PostMapping("/reserve")
	public String reserve(Reservation reservation,Model model) {
		model.addAttribute("reservation", reservation);
		return "reserve/reserve";
	}
	
	@GetMapping("/cancel")
	public String showCancel(Model model) {
		model.addAttribute("reservation", reservationMapper.getAllReservations());
		return "cancel/cancelInput";
	}
	
	@PostMapping("/cancel")
	public String cancel(Reservation reservation,Model model) {
		model.addAttribute("reservation", reservation);
		return "cancel/cancelInput";
	}
}
