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

import com.example.demo.service.ReservationService;

import lombok.RequiredArgsConstructor;

/**
 * キャンセル画面のリクエスト/レスポンスを担当するコントローラー。
 * <p>
 * 業務ロジックやMapper操作は {@link ReservationService} へ委譲する。
 */
@Controller
@RequiredArgsConstructor
public class CancelController {

    private final ReservationService reservationService;

    /**
     * キャンセル入力画面を表示する。
     *
     * @param date      表示対象日(未指定時は当日)
     * @param principal ログイン中ユーザー
     * @param model     画面描画用モデル
     * @return キャンセル入力画面
     */
    @GetMapping("/cancel")
    public String showCancel(
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Principal principal,
            Model model) {
        LocalDate targetDate = (date == null) ? LocalDate.now() : date;
        ReservationService.CancelGridData grid = reservationService.buildCancelGrid(targetDate, principal.getName());

        model.addAttribute("targetDate", grid.getTargetDate());
        model.addAttribute("timeSlots", grid.getTimeSlots());
        model.addAttribute("rows", grid.getRows());
        return "cancel/cancelInput";
    }

    /**
     * キャンセル確認画面を表示する。
     *
     * @param roomId    会議室ID
     * @param date      予約日
     * @param start     利用開始時刻
     * @param principal ログイン中ユーザー
     * @param model     画面描画用モデル
     * @return キャンセル確認画面、または入力画面(非許可時)
     */
    @GetMapping("/cancel/confirm")
    public String confirmCancel(
            @RequestParam("roomId") String roomId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("start") @DateTimeFormat(pattern = "H:mm") LocalTime start,
            Principal principal,
            Model model) {
        ReservationService.CancelConfirmData confirmData =
                reservationService.buildCancelConfirm(roomId, date, start, principal.getName());

        if (confirmData == null) {
            model.addAttribute("errorMessage", "この予約はキャンセルできません。");
            return showCancel(date, principal, model);
        }

        model.addAttribute("reservation", confirmData.getReservation());
        model.addAttribute("room", confirmData.getRoom());
        return "cancel/cancelConfirm";
    }

    /**
     * キャンセル確定を実行し、結果画面を表示する。
     *
     * @param reservationId 対象予約ID
     * @param date          入力画面に戻るための対象日
     * @param principal     ログイン中ユーザー
     * @param model         画面描画用モデル
     * @return キャンセル結果画面、または入力画面(非許可時)
     */
    @PostMapping("/cancel/confirm")
    public String cancel(@RequestParam("reservationId") int reservationId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Principal principal,
            Model model) {
        boolean canceled = reservationService.cancelReservation(reservationId, principal.getName());
        if (!canceled) {
            model.addAttribute("errorMessage", "この予約はキャンセルできません。");
            return showCancel(date, principal, model);
        }

        model.addAttribute("resultMessage", "予約をキャンセルしました。");
        model.addAttribute("targetDate", date);
        return "cancel/cancelResult";
    }
}
