package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Room;
import com.example.demo.form.RoomForm;
import com.example.demo.service.RoomService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminRoomController {
	private final RoomService roomService;
	
	
	/**
	 * 会議室リスト表示用。
	 * 完成次第でAdminRoomControllerに移動予定。
	 */
	@GetMapping("/admin/roomlist")
	public String roomList(Model model) {
	    List<Room> rooms = roomService.getAllRooms();
	    model.addAttribute("rooms", rooms);
	    return "admin/roomlist";
	}
	
	@GetMapping("/admin/roomadd")
	public String showaddRoom(@ModelAttribute RoomForm roomForm) {
	    return "admin/roomcreate"; 
	}
	
	@GetMapping("/admin/roomdetail/{id}")
	public String detail(@PathVariable String id,Model model) {
		model.addAttribute("room",roomService.getRoomById(id));
		return "admin/roomdetail";
	}
	
	/**
	 * 会議室追加
	 * 完成次第でAdminRoomControllerに移動予定。
	 */
	@PostMapping("/admin/roomcreate")
	public String createRoom(@ModelAttribute RoomForm roomForm) {
		roomService.addRoom(roomForm);
	    return "admin/roomlist"; 
	}
	
	@GetMapping("/admin/roomupdate")
	public String showupdateRoom(@ModelAttribute String id,Model model) {
		model.addAttribute("room", roomService.getRoomById(id));
	    return "admin/roomupdate"; 
	}
	
	/**
	 * 会議室追加
	 * 完成次第でAdminRoomControllerに移動予定。
	 */
	@PostMapping("/admin/roomupdate")
	public String updateRoom(@ModelAttribute RoomForm roomForm) {
		roomService.updateRoom(roomForm);
	    return "admin/roomlist"; 
	}
	
	/**
	 * 削除して前の画面にもどす。
	 */
	@GetMapping("/admin/roomdelete/{id}")
	public String delete(@PathVariable String id,Model model) {
		roomService.deleteRoom(id);
		model.addAttribute("rooms",roomService.getAllRooms());
		return "admin/roomlist";
	}
	
	
}
