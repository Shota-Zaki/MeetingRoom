package com.example.demo.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PeriodDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeService {
	
	
	public List<LocalTime> createTimeTable(PeriodDTO dto) {
		
		List<LocalTime> timeTable = new ArrayList<>();
		
		for (LocalTime t = dto.getStartTime(); t.isBefore(dto.getEndTime()); t = t.plus(dto.getInterval())) {
			timeTable.add(t);
        }
		
		return timeTable;
	}
	
	public int startPeriod(LocalTime start) {
		
		int periodIndex = 0;
		
		
		
		return periodIndex;
	}
}
