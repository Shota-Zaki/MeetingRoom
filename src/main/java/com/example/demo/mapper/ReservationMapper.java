package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Reservation;

@Mapper
public interface ReservationMapper {
	List<Reservation> getAllReservations();
	
	Reservation getReservationById(int id);
	
	void insertReservation(Reservation reserve);
	
	void updateReservation(Reservation reserve);
	
	void deleteReservationById(int id);
}
