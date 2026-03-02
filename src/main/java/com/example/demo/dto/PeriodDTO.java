package com.example.demo.dto;

import java.time.Duration;
import java.time.LocalTime;

import lombok.Data;

@Data
public class PeriodDTO {
	
	// 受付開始時刻
	private final LocalTime startTime = LocalTime.of(9, 0) ;
	
	// 最終受付時刻
	private final LocalTime endTime = LocalTime.of(17, 0);
	
	// 予約間隔
	private final Duration interval = Duration.ofMinutes(60);
	
}
