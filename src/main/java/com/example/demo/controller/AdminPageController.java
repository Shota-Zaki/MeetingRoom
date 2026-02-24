package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.mapper.ReservationMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminPageController {

    private final ReservationMapper reservationMapper;

    @GetMapping("/admin/roomlist")
    public String roomList() {
        return "admin/roomlist";
    }

    @GetMapping("/admin/roomcreate")
    public String roomCreate() {
        return "admin/roomcreate";
    }

    @PostMapping("/admin/reservelist")
    public String reserveList(Model model) {
        model.addAttribute("reservations", reservationMapper.getAllReservations());
        return "admin/reservelist";
    }
}
