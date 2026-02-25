package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Room;
import com.example.demo.mapper.RoomMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {
	private final RoomMapper roomMapper;
	
	
	public List<Room> getAllRooms() {
		 return roomMapper.getAllRooms();
	}
	
	public void addRoom(Room room) {
		roomMapper.insertRoom(room);
	}
	
	//public void 
}
