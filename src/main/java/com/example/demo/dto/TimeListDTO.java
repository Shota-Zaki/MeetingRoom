package com.example.demo.dto;

import java.time.LocalTime;

import lombok.Data;

/*
 * 時間表に格納するための時間と予約を持ったDTO
 * */
@Data
public class TimeListDTO {
	
	/*
	 * 開始時間
	 * */
	private LocalTime start;
	
	/*
	 * 終了時間
	 * */
	private LocalTime end;
	
	
	/*
	 * 予約の有無
	 * */
	private boolean reserve;
	
	
}
