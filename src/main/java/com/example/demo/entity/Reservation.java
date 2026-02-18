package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class Reservation {
	//フィールド
	/**
	* 予約番号
	*/
	private int id;
		
	/**
	 * 会議室ID
	 */
	private String roomId;
	
	/**
	 * 利用日
	 */
	private LocalDate date;
	
	/**
	 * 利用開始時刻
	 */
	private LocalTime start;
	
	/**
	 * 利用終了時刻
	 */
	private LocalTime end;
	
	/**
	 * 利用者ID
	 */
	private String userId;
}
