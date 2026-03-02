package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Room;
import com.example.demo.form.RoomForm;
import com.example.demo.mapper.RoomMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {
	private final RoomMapper roomMapper;
	
	
	public List<Room> getAllRooms() {
		 return roomMapper.getAllRooms();
	}
	
	public Room getRoomById(String id) {
		return roomMapper.getRoomById(id);
	}
	
	public void addRoom(RoomForm form) {
		Room room = new Room();
		room.setId(form.getId());
		room.setName(form.getName());
		roomMapper.insertRoom(room);
	}
	
	public void updateRoom(RoomForm form) {
		Room room = new Room();
		room.setId(form.getId());
		room.setName(form.getName());
	    roomMapper.updateRoom(room);
	}
	
	public void deleteRoom(String id) {
		roomMapper.deleteRoomById(id);
	}

	public int roomIndex(String roomId) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}
}
