package com.example.demo.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TimeService {
	
public List<LocalTime> getTimeList(){
	List<LocalTime> startTimes = new ArrayList<>();
	return startTimes;
}

}
