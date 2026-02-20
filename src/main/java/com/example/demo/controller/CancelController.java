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
import com.example.demo.mapper.ReservationMapper;
import com.example.demo.mapper.RoomMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CancelController {

    private final ReservationMapper reservationMapper;
    private final RoomMapper roomMapper;

    @GetMapping("/cancel")
    public String showCancel(
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Principal principal,
            Model model) {
        LocalDate targetDate = (date == null) ? LocalDate.now() : date;

        List<Room> rooms = roomMapper.getAllRooms();
        List<LocalTime> timeSlots = buildTimeSlots();
        List<Reservation> reservations = reservationMapper.getReservationsByDate(targetDate);

        List<CancelRow> rows = new ArrayList<>();
        for (Room room : rooms) {
            List<CancelCell> cells = new ArrayList<>();
            for (LocalTime slot : timeSlots) {
                Reservation hit = reservations.stream()
                        .filter(r -> r.getRoomId().equals(room.getId()) && slot.equals(r.getStart()))
                        .findFirst()
                        .orElse(null);

                boolean cancellable = hit != null
                        && hit.getUserId().equals(principal.getName())
                        && !targetDate.isBefore(LocalDate.now());

                cells.add(new CancelCell(room.getId(), slot.format(DateTimeFormatter.ofPattern("HH:mm")), cancellable));
            }
            rows.add(new CancelRow(room.getName(), cells));
        }

        model.addAttribute("targetDate", targetDate);
        model.addAttribute("timeSlots", timeSlots.stream().map(t -> t.format(DateTimeFormatter.ofPattern("H:mm"))).toList());
        model.addAttribute("rows", rows);
        return "cancel/cancelInput";
    }

    @GetMapping("/cancel/confirm")
    public String confirmCancel(
            @RequestParam("roomId") String roomId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("start") @DateTimeFormat(pattern = "H:mm") LocalTime start,
            Principal principal,
            Model model) {
        Reservation reservation = reservationMapper.getReservationBySlot(roomId, date, start);
        if (reservation == null || !reservation.getUserId().equals(principal.getName()) || date.isBefore(LocalDate.now())) {
            model.addAttribute("errorMessage", "この予約はキャンセルできません。");
            return showCancel(date, principal, model);
        }

        Room room = roomMapper.getRoomById(roomId);
        model.addAttribute("reservation", reservation);
        model.addAttribute("room", room);
        return "cancel/cancelConfirm";
    }

    @PostMapping("/cancel/confirm")
    public String cancel(@RequestParam("reservationId") int reservationId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Principal principal,
            Model model) {
        Reservation reservation = reservationMapper.getReservationById(reservationId);
        if (reservation == null
                || !reservation.getUserId().equals(principal.getName())
                || reservation.getDate().isBefore(LocalDate.now())) {
            model.addAttribute("errorMessage", "この予約はキャンセルできません。");
            return showCancel(date, principal, model);
        }

        reservationMapper.deleteReservationById(reservationId);
        return "redirect:/cancel?date=" + date;
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
    public static class CancelRow {
        private String roomName;
        private List<CancelCell> cells;
    }

    @Getter
    @AllArgsConstructor
    public static class CancelCell {
        private String roomId;
        private String start;
        private boolean cancellable;
    }
}
