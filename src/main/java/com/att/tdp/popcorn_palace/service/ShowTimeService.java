package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.ShowTime;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowTimeService {

    @Autowired
    private ShowTimeRepository showTimeRepository;

    public ShowTime addShowTime(ShowTime showTime) {
        // check if there is no intersected time intervals in the same theater
        if(showTimeRepository.findOverlappingShowTimes(showTime.getTheater(), showTime.getStartTime(), showTime.getEndTime()).isPresent()){
            throw new IllegalArgumentException(String.format(
                    "ShowTime %d in theater %s overlaps with another show time", showTime.getId(), showTime.getTheater()));
        }

        return showTimeRepository.save(showTime); // save show time to db
    }
}
