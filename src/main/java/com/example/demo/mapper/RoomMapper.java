package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Room;

@Mapper
public interface RoomMapper {
	List<Room> getAllRooms();
	
	Room getRoomById(String id);
	
	void insertRoom(Room room);
	
	void updateRoom(Room room);
	
	void deleteRoomById(String id);

	List<Room> selectAll();
}
