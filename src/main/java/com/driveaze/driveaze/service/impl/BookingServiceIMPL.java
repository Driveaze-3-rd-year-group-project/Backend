package com.driveaze.driveaze.service.impl;

import com.driveaze.driveaze.dto.BookingDTO;
import com.driveaze.driveaze.entity.Booking;
import com.driveaze.driveaze.repository.BookingRepo;
import com.driveaze.driveaze.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceIMPL implements BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    @Override
    public String addBooking(BookingDTO bookingDTO){
        Booking booking = new Booking(
                bookingDTO.getBookingId(),
                bookingDTO.getVehicleNo(),
                bookingDTO.getBrand(),
                bookingDTO.getModel(),
                bookingDTO.getStatus(),
                bookingDTO.getPreferredDate(),
                bookingDTO.getPreferredTime()
        );
        bookingRepo.save(booking);
        return "Booking Added";
    }

    @Override
    public String updateBookiing(BookingDTO bookingDTO){
        if(bookingRepo.existsById(bookingDTO.getBookingId())){
            Booking booking = bookingRepo.getById(bookingDTO.getBookingId());

            booking.setStatus(bookingDTO.getStatus());

            bookingRepo.save(booking);
            return "Booking Updated";
        }else{
            return "No booking found!";
        }
    }

    @Override
    public List<BookingDTO> getAllBooking(){
        List<Booking> bookings = bookingRepo.findAll();
        List<BookingDTO> bookingDTOList = new ArrayList<>();

        for (Booking booking: bookings){
            BookingDTO bookingDTO = new BookingDTO(
                    booking.getBookingId(),
                    booking.getVehicleNo(),
                    booking.getBrand(),
                    booking.getModel(),
                    booking.getStatus(),
                    booking.getPreferredDate(),
                    booking.getPreferredTime()
            );
            bookingDTOList.add(bookingDTO);
        }
        return  bookingDTOList;
    }

}
