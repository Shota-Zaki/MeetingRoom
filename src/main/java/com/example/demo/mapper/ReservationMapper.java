package com.example.demo.mapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Reservation;

@Mapper
public interface ReservationMapper {
    List<Reservation> getAllReservations();

    List<Reservation> getReservationsByDate(@Param("date") LocalDate date);

    Reservation getReservationById(int id);

    Reservation getReservationBySlot(@Param("roomId") String roomId,
            @Param("date") LocalDate date,
            @Param("start") LocalTime start);

    int countReservation(@Param("roomId") String roomId,
            @Param("date") LocalDate date,
            @Param("start") LocalTime start);

    void insertReservation(Reservation reserve);

    void updateReservation(Reservation reserve);

    void deleteReservationById(int id);
}
