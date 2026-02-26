package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Reservation;
import com.example.demo.form.UserForm;
import com.example.demo.service.ReservationService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminUserController {
	private final UserService userService;
	private final ReservationService reservationService;
	
	@GetMapping("admin/top")
    public String admin() {
    	return "admin/admin";
    }
	
	@GetMapping("/admin/list")
	public String index(Model model) {
		model.addAttribute("users",userService.getAllUsers());
		return "admin/userlist";
	}
	
	@GetMapping("/admin/detail/{id}")
	public String detail(@PathVariable String id,Model model) {
		model.addAttribute("user",userService.getUserById(id));
		return "admin/detail";
	}
	
	@GetMapping("/admin/useradd")
	public String create(@ModelAttribute UserForm userForm) {
		return "admin/useradd";
	}
	
	@PostMapping("/admin/create")
	public String create(@ModelAttribute UserForm userForm,Model model) {
		userService.addUser(userForm);
		return "admin/userlist";
	}
	
	@GetMapping("/admin/update/{id}")
	public String update(@PathVariable String id,Model model) {
		model.addAttribute("user",userService.getUserById(id));
		return "admin/userupdate";
	}
	
	@PostMapping("/admin/update")
	public String update(@ModelAttribute UserForm userForm,Model model) {
		userService.updateUser(userForm);
		model.addAttribute("users",userService.getAllUsers());
		return "admin/userlist";
	}
	
	/**
	 * 削除して前の画面にもどす。
	 */
	@GetMapping("/admin/delete/{id}")
	public String delete(@PathVariable String id,Model model) {
		userService.deleteUser(id);
		model.addAttribute("users",userService.getAllUsers());
		return "admin/userlist";
	}
	
	
	//---------------予約管理関連--------------------
	
	/**
	 * 予約リスト表示用。
	 * 完成次第でAdminReserveControllerに移動予定。
	 */
	@GetMapping("/admin/reservelist")
	public String reserveList(Model model) {
	    List<Reservation> reserves = reservationService.getAllReservations();
	    model.addAttribute("reserves", reserves);
	    return "admin/reservelist";
	}
}
