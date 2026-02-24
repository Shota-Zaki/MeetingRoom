package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

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
 * 予約/キャンセルに関する業務ロジックを扱うサービス。
 * <p>
 * Controller は画面遷移と Model への詰め替えに専念し、
 * DBアクセス(MyBatis Mapper操作)や判定ロジックは本クラスに集約する。
 * ※ コントローラーからの利用窓口を一本化し、マージ時の競合を最小化する。
 */
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final RoomMapper roomMapper;
    private final UserMapper userMapper;

    /**
     * 予約入力画面用のグリッドデータを作成する。
     *
     * @param targetDate 表示対象日
     * @return 予約入力画面用データ
     */
    public ReserveGridData buildReserveGrid(LocalDate targetDate) {
        List<Room> rooms = roomMapper.getAllRooms();
        List<LocalTime> slots = buildTimeSlots();
        List<Reservation> reservations = reservationMapper.getReservationsByDate(targetDate);

        List<ReserveRowData> rows = new ArrayList<>();
        for (Room room : rooms) {
            List<ReserveCellData> cells = new ArrayList<>();
            for (LocalTime slot : slots) {
                boolean reserved = reservations.stream()
                        .anyMatch(r -> r.getRoomId().equals(room.getId()) && slot.equals(r.getStart()));
                cells.add(new ReserveCellData(room.getId(), slot.format(DateTimeFormatter.ofPattern("HH:mm")), !reserved));
            }
            rows.add(new ReserveRowData(room.getName(), room.getId(), cells));
        }

        return new ReserveGridData(
                targetDate,
                slots.stream().map(t -> t.format(DateTimeFormatter.ofPattern("H:mm"))).toList(),
                rows);
    }

    /**
     * 予約確認画面の表示データを作成する。
     *
     * @param roomId 会議室ID
     * @param date 予約日
     * @param start 利用開始時刻
     * @param userId 利用者ID
     * @return 予約確認データ。重複時は {@code null}
     */
    public ReserveConfirmData buildReserveConfirm(String roomId, LocalDate date, LocalTime start, String userId) {
        if (isReserved(roomId, date, start)) {
            return null;
        }

        Room room = roomMapper.getRoomById(roomId);
        User user = userMapper.getUserById(userId);

        Reservation reservation = new Reservation();
        reservation.setRoomId(roomId);
        reservation.setDate(date);
        reservation.setStart(start);
        reservation.setEnd(start.plusHours(1));
        reservation.setUserId(userId);

        return new ReserveConfirmData(reservation, room, user);
    }

    /**
     * 予約を確定する。
     *
     * @param reservation 確定対象予約
     * @return 登録成功時 true、重複で登録不可時 false
     */
    public boolean createReservation(Reservation reservation) {
        if (isReserved(reservation.getRoomId(), reservation.getDate(), reservation.getStart())) {
            return false;
        }
        reservationMapper.insertReservation(reservation);
        return true;
    }

    /**
     * キャンセル入力画面用のグリッドデータを作成する。
     *
     * @param targetDate 表示対象日
     * @param userId ログイン中ユーザーID
     * @return キャンセル入力画面用データ
     */
    public CancelGridData buildCancelGrid(LocalDate targetDate, String userId) {
        List<Room> rooms = roomMapper.getAllRooms();
        List<LocalTime> slots = buildTimeSlots();
        List<Reservation> reservations = reservationMapper.getReservationsByDate(targetDate);

        List<CancelRowData> rows = new ArrayList<>();
        for (Room room : rooms) {
            List<CancelCellData> cells = new ArrayList<>();
            for (LocalTime slot : slots) {
                Reservation hit = reservations.stream()
                        .filter(r -> r.getRoomId().equals(room.getId()) && slot.equals(r.getStart()))
                        .findFirst()
                        .orElse(null);

                // 自分の予約かつ過去日でない場合のみ選択可能。
                boolean cancellable = hit != null
                        && userId.equals(hit.getUserId())
                        && !targetDate.isBefore(LocalDate.now());

                cells.add(new CancelCellData(room.getId(), slot.format(DateTimeFormatter.ofPattern("HH:mm")), cancellable));
            }
            rows.add(new CancelRowData(room.getName(), cells));
        }

        return new CancelGridData(
                targetDate,
                slots.stream().map(t -> t.format(DateTimeFormatter.ofPattern("H:mm"))).toList(),
                rows);
    }

    /**
     * キャンセル確認画面の表示データを作成する。
     *
     * @param roomId 会議室ID
     * @param date 予約日
     * @param start 利用開始時刻
     * @param userId ログイン中ユーザーID
     * @return キャンセル確認データ。キャンセル不可時は {@code null}
     */
    public CancelConfirmData buildCancelConfirm(String roomId, LocalDate date, LocalTime start, String userId) {
        Reservation reservation = reservationMapper.getReservationBySlot(roomId, date, start);
        if (!isCancelable(reservation, userId)) {
            return null;
        }

        Room room = roomMapper.getRoomById(roomId);
        return new CancelConfirmData(reservation, room);
    }

    /**
     * 予約キャンセルを実行する。
     *
     * @param reservationId 対象予約ID
     * @param userId ログイン中ユーザーID
     * @return キャンセル成功時 true、キャンセル不可時 false
     */
    public boolean cancelReservation(int reservationId, String userId) {
        Reservation reservation = reservationMapper.getReservationById(reservationId);
        if (!isCancelable(reservation, userId)) {
            return false;
        }

        reservationMapper.deleteReservationById(reservationId);
        return true;
    }

    /**
     * 指定枠が予約済みかを判定する。
     */
    private boolean isReserved(String roomId, LocalDate date, LocalTime start) {
        return reservationMapper.countReservation(roomId, date, start) > 0;
    }

    /**
     * 予約がキャンセル可能か(本人予約かつ過去日でないか)を判定する。
     */
    private boolean isCancelable(Reservation reservation, String userId) {
        return reservation != null
                && userId.equals(reservation.getUserId())
                && !reservation.getDate().isBefore(LocalDate.now());
    }

    /**
     * 1時間単位(9:00-18:00)の表示用スロットを生成する。
     */
    private List<LocalTime> buildTimeSlots() {
        List<LocalTime> timeSlots = new ArrayList<>();
        for (int hour = 9; hour <= 18; hour++) {
            timeSlots.add(LocalTime.of(hour, 0));
        }
        return timeSlots;
    }

    /** 予約入力グリッド全体。 */
    @Getter
    @AllArgsConstructor
    public static class ReserveGridData {
        private LocalDate targetDate;
        private List<String> timeSlots;
        private List<ReserveRowData> rows;
    }

    /** 予約入力グリッド1行。 */
    @Getter
    @AllArgsConstructor
    public static class ReserveRowData {
        private String roomName;
        private String roomId;
        private List<ReserveCellData> cells;
    }

    /** 予約入力グリッド1セル。 */
    @Getter
    @AllArgsConstructor
    public static class ReserveCellData {
        private String roomId;
        private String start;
        private boolean available;
    }

    /** 予約確認画面表示データ。 */
    @Getter
    @AllArgsConstructor
    public static class ReserveConfirmData {
        private Reservation reservation;
        private Room room;
        private User user;
    }

    /** キャンセル入力グリッド全体。 */
    @Getter
    @AllArgsConstructor
    public static class CancelGridData {
        private LocalDate targetDate;
        private List<String> timeSlots;
        private List<CancelRowData> rows;
    }

    /** キャンセル入力グリッド1行。 */
    @Getter
    @AllArgsConstructor
    public static class CancelRowData {
        private String roomName;
        private List<CancelCellData> cells;
    }

    /** キャンセル入力グリッド1セル。 */
    @Getter
    @AllArgsConstructor
    public static class CancelCellData {
        private String roomId;
        private String start;
        private boolean cancellable;
    }

    /** キャンセル確認画面表示データ。 */
    @Getter
    @AllArgsConstructor
    public static class CancelConfirmData {
        private Reservation reservation;
        private Room room;
    }
}
