package com.driveaze.driveaze.controller;

import com.driveaze.driveaze.dto.BookingDTO;
import com.driveaze.driveaze.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping(path = "/add" )
    public String addItem(@RequestBody BookingDTO bookingDTO){
        String res = bookingService.addBooking(bookingDTO);
        return res;
    }
    @PutMapping(path = "/update")
    public  String updateItem(@RequestBody BookingDTO bookingDTO) {
        String res = bookingService.updateBookiing(bookingDTO);
        return res;
    }

    @GetMapping(path = "/getAll")
    public List<BookingDTO> getAllItems(){
        List<BookingDTO> allItems = bookingService.getAllBooking();
        return allItems;
    }
}
