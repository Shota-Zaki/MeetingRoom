package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Data;

/*
 * VIEW表示用の予約データを入れるDTO
 * */

@Data
public class ReservationDTO {
	
	/*
	 * 予約ID
	 * */ 
	private int id; 
	
	/*
	 * 部屋番号
	 * */ 
	private String roomId; 
	
	/*
	 * 日付
	 * */ 
	private LocalDate date; 
	
	/* 利用開始時間
	 * */ 
	private LocalDate start; 
	
	/*
	 * 利用終了時間
	 * */ 
	private LocalDate end; 
	
	/*
	 * 予約者
	 * */ 
	private String userId; 
}
