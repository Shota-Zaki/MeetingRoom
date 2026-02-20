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

/**
 * 予約入力・確認・確定の画面遷移を扱うコントローラー。
 */
@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationMapper reservationMapper;
    private final RoomMapper roomMapper;
    private final UserMapper userMapper;

    /**
     * 指定日の空き状況を、会議室×時間帯(9:00-18:00)の表形式で表示する。
     *
     * @param date  表示対象日。未指定時は当日。
     * @param model 画面描画用モデル
     * @return 予約入力画面
     */
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
                // 同じ会議室・同じ開始時刻の予約が存在するかを判定する。
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

    /**
     * 選択した空き枠の予約確認画面を表示する。
     *
     * @param roomId    会議室ID
     * @param date      予約日
     * @param start     利用開始時刻
     * @param principal ログイン中ユーザー
     * @param model     画面描画用モデル
     * @return 予約確認画面、または入力画面(重複時)
     */
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

    /**
     * 予約を確定して結果画面を表示する。
     *
     * @param reservation 確定対象の予約
     * @param model       画面描画用モデル
     * @return 予約結果画面、または入力画面(重複時)
     */
    @PostMapping("/reserve/confirm")
    public String createReservation(Reservation reservation, Model model) {
        if (reservationMapper.countReservation(reservation.getRoomId(), reservation.getDate(), reservation.getStart()) > 0) {
            model.addAttribute("errorMessage", "選択した時間帯はすでに予約済みです。");
            return showReserve(reservation.getDate(), model);
        }

        reservationMapper.insertReservation(reservation);
        model.addAttribute("resultMessage", "予約が完了しました！");
        model.addAttribute("targetDate", reservation.getDate());
        return "reserve/reserveResult";
    }

    /**
     * 1時間単位(9:00-18:00)の表示用スロットを生成する。
     *
     * @return 時間帯リスト
     */
    private List<LocalTime> buildTimeSlots() {
        List<LocalTime> timeSlots = new ArrayList<>();
        for (int hour = 9; hour <= 18; hour++) {
            timeSlots.add(LocalTime.of(hour, 0));
        }
        return timeSlots;
    }

    /**
     * 予約表の1行分(会議室単位)を表す表示モデル。
     */
    @Getter
    @AllArgsConstructor
    public static class ReserveRow {
        private String roomName;
        private String roomId;
        private List<ReserveCell> cells;
    }

    /**
     * 予約表の1セル分(時間帯単位)を表す表示モデル。
     */
    @Getter
    @AllArgsConstructor
    public static class ReserveCell {
        private String roomId;
        private String start;
        private boolean available;
    }
}
