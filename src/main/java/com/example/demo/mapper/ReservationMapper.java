package com.example.demo.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Reservation;

@Mapper
public interface ReservationMapper {
	
	List<Reservation> getAllReservations();
	
	List<Reservation> getAllReservationsByDate(LocalDate date);
	
	Reservation getReservationById(int id);
	
	void insertReservation(Reservation reservation);
	
	void updateReservation(Reservation reservation);
	
	void deleteReservationById(int id);

}
