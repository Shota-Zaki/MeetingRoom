package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Reservation;
import com.example.demo.service.ReservationService;

import lombok.RequiredArgsConstructor;

/**
 * 予約画面のリクエスト/レスポンスを担当するコントローラー。
 * <p>
 * 業務ロジックやMapper操作は {@link ReservationService} へ委譲する。
 */
@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 予約入力画面を表示する。
     *
     * @param date  表示対象日(未指定時は当日)
     * @param model 画面描画用モデル
     * @return 予約入力画面
     */
    @GetMapping("/reserve")
    public String showReserve(
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        LocalDate targetDate = (date == null) ? LocalDate.now() : date;
        ReservationService.ReserveGridData grid = reservationService.buildReserveGrid(targetDate);

        model.addAttribute("targetDate", grid.getTargetDate());
        model.addAttribute("timeSlots", grid.getTimeSlots());
        model.addAttribute("rows", grid.getRows());
        return "reserve/reserveInput";
    }

    /**
     * 予約確認画面を表示する。
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
        ReservationService.ReserveConfirmData confirmData =
                reservationService.buildReserveConfirm(roomId, date, start, principal.getName());

        if (confirmData == null) {
            model.addAttribute("errorMessage", "選択した時間帯はすでに予約済みです。");
            return showReserve(date, model);
        }

        model.addAttribute("reservation", confirmData.getReservation());
        model.addAttribute("room", confirmData.getRoom());
        model.addAttribute("user", confirmData.getUser());
        return "reserve/reserve";
    }

    /**
     * 予約確定を実行し、結果画面を表示する。
     *
     * @param reservation 予約情報
     * @param model       画面描画用モデル
     * @return 予約結果画面、または入力画面(重複時)
     */
    @PostMapping("/reserve/confirm")
    public String createReservation(Reservation reservation, Model model) {
        boolean created = reservationService.createReservation(reservation);
        if (!created) {
            model.addAttribute("errorMessage", "選択した時間帯はすでに予約済みです。");
            return showReserve(reservation.getDate(), model);
        }

        model.addAttribute("resultMessage", "予約が完了しました！");
        model.addAttribute("targetDate", reservation.getDate());
        return "reserve/reserveResult";
    }
}
