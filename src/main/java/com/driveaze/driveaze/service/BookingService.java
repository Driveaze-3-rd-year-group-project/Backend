package com.driveaze.driveaze.service;

import com.driveaze.driveaze.dto.BookingDTO;
import java.util.List;
public interface BookingService {
    String addBooking(BookingDTO bookingDTO);

    String updateBookiing(BookingDTO bookingDTO);

    List<BookingDTO> getAllBooking();
}
