package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Reservation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetingRoomService {
	private final ReservationService reservationService;
	private final RoomService roomService;
	private final UserService userService;
	private final TimeService timeService;
	
	
	public Reservation [][] createReservations(){
		
		List<Reservation> reservations = reservationService.getAllReservations();
		
		int period = reservationService.getAllReservations().size();
		int rooms = roomService.getAllRooms().size();
		
		Reservation [][] reserveArray = new Reservation[period][rooms];
		
		for(Reservation reserve:reservations) {
			
			int periodIndex = timeService.startPeriod(reserve.getStart());
			
			//会議室比較
			int roomsIndex  = roomService.roomIndex(reserve.getRoomId());
			
			//一致した時間、会議室の予約情報を格納
			reserveArray[periodIndex][roomsIndex] = reserve; 
		}
		
		return reserveArray;
	}
}
