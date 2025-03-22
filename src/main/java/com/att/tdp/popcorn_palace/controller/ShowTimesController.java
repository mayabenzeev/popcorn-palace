package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.entity.ShowTime;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.att.tdp.popcorn_palace.service.ShowTimeService;

@AllArgsConstructor
@RestController
@RequestMapping("/showtimes")
public class ShowTimesController {
    private ShowTimeService showTimeService;

    @GetMapping("/{id}")
    public ShowTime getShowTimeById(@PathVariable Long id) {
        return showTimeService.getShowTimeById(id);
    }

    @PostMapping
    public ShowTime addShowTime(@RequestBody ShowTime showTime) {
        return showTimeService.addShowTime(showTime);
    }

    @PutMapping("/update/{id}")
    public void updateShowTime(@PathVariable Long id, @RequestBody ShowTime updatedShowTime) {
        showTimeService.updateShowTime(id, updatedShowTime);
    }

    @DeleteMapping("/{id}")
    public void deleteShowTime(@PathVariable Long id) {
        showTimeService.deleteShowTime(id);
    }

}
