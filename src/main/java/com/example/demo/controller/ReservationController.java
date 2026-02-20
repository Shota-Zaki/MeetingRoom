package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Reservation;
import com.example.demo.entity.Room;
import com.example.demo.entity.User;
import com.example.demo.mapper.ReservationMapper;
import com.example.demo.mapper.RoomMapper;
import com.example.demo.mapper.UserMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationMapper reservationMapper;
    private final RoomMapper roomMapper;
    private final UserMapper userMapper;

    @GetMapping("/reserve")
    public String showReserve(
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        LocalDate targetDate = (date == null) ? LocalDate.now() : date;

        List<Room> rooms = roomMapper.getAllRooms();
        List<LocalTime> timeSlots = buildTimeSlots();

        List<Reservation> reservations = reservationMapper.getReservationsByDate(targetDate);

        List<ReserveRow> rows = new ArrayList<>();
        for (Room room : rooms) {
            List<ReserveCell> cells = new ArrayList<>();
            for (LocalTime slot : timeSlots) {
                boolean reserved = reservations.stream()
                        .anyMatch(r -> r.getRoomId().equals(room.getId()) && slot.equals(r.getStart()));
                cells.add(new ReserveCell(room.getId(), slot.format(DateTimeFormatter.ofPattern("HH:mm")), !reserved));
            }
            rows.add(new ReserveRow(room.getName(), room.getId(), cells));
        }

        model.addAttribute("targetDate", targetDate);
        model.addAttribute("timeSlots", timeSlots.stream().map(t -> t.format(DateTimeFormatter.ofPattern("H:mm"))).toList());
        model.addAttribute("rows", rows);
        return "reserve/reserveInput";
    }

    @GetMapping("/reserve/confirm")
    public String confirm(
            @RequestParam("roomId") String roomId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("start") @DateTimeFormat(pattern = "H:mm") LocalTime start,
            Principal principal,
            Model model) {
        if (reservationMapper.countReservation(roomId, date, start) > 0) {
            model.addAttribute("errorMessage", "選択した時間帯はすでに予約済みです。");
            return showReserve(date, model);
        }

        Room room = roomMapper.getRoomById(roomId);
        User user = userMapper.getUserById(principal.getName());

        Reservation reservation = new Reservation();
        reservation.setRoomId(roomId);
        reservation.setDate(date);
        reservation.setStart(start);
        reservation.setEnd(start.plusHours(1));
        reservation.setUserId(principal.getName());

        model.addAttribute("reservation", reservation);
        model.addAttribute("room", room);
        model.addAttribute("user", user);
        return "reserve/reserve";
    }

    @PostMapping("/reserve/confirm")
    public String createReservation(Reservation reservation, Model model) {
        if (reservationMapper.countReservation(reservation.getRoomId(), reservation.getDate(), reservation.getStart()) > 0) {
            model.addAttribute("errorMessage", "選択した時間帯はすでに予約済みです。");
            return showReserve(reservation.getDate(), model);
        }

        reservationMapper.insertReservation(reservation);
        return "redirect:/reserve?date=" + reservation.getDate();
    }

    private List<LocalTime> buildTimeSlots() {
        List<LocalTime> timeSlots = new ArrayList<>();
        for (int hour = 9; hour <= 18; hour++) {
            timeSlots.add(LocalTime.of(hour, 0));
        }
        return timeSlots;
    }

    @Getter
    @AllArgsConstructor
    public static class ReserveRow {
        private String roomName;
        private String roomId;
        private List<ReserveCell> cells;
    }

    @Getter
    @AllArgsConstructor
    public static class ReserveCell {
        private String roomId;
        private String start;
        private boolean available;
    }
}
