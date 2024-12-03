package com.driveaze.driveaze.service.impl;

import com.driveaze.driveaze.dto.BookingDTO;
import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.entity.Booking;
import com.driveaze.driveaze.exception.OurException;
import com.driveaze.driveaze.repository.BookingRepo;
import com.driveaze.driveaze.repository.UsersRepo;
import com.driveaze.driveaze.service.interfac.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookingServiceIMPL implements BookingService {

    private int customer;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Override
    public ResponseDTO addBooking(BookingDTO bookingDTO, ResponseDTO userDetails) {
        ResponseDTO response = new ResponseDTO();

        try {
            // Ensure customerId is set in bookingDTO from userDetails if null
            if (bookingDTO.getCustomerId() == null) {
                bookingDTO.setCustomerId(userDetails.getOurUsers().getId());
            }

            // Check for duplicate booking using repository query
            boolean duplicateExists = bookingRepo.existsByCustomerIdAndVehicleNoAndStatus(
                    bookingDTO.getCustomerId(),
                    bookingDTO.getVehicleNo(),
                    0 // Assuming 0 is the status for active booking
            );

            if (duplicateExists) {
                response.setStatusCode(409);
                response.setMessage("Booking already exists for the selected vehicle and customer.");
            } else {
                // Create and save the new booking
                Booking booking = getBooking(bookingDTO, userDetails);
                bookingRepo.save(booking);

                response.setStatusCode(200);
                response.setMessage("Successfully added booking");
            }
        } catch (DataAccessException e) {
            response.setStatusCode(500);
            response.setMessage("Database error occurred while adding booking: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Unexpected error occurred while adding booking: " + e.getMessage());
        }

        return response;
    }



    private static Booking getBooking(BookingDTO bookingDTO, ResponseDTO userDetails) {
        Booking booking = new Booking(
                bookingDTO.getBookingId(),
                bookingDTO.getVehicleNo(),
                bookingDTO.getBrand(),
                bookingDTO.getModel(),
                bookingDTO.getStatus(),
                bookingDTO.getPreferredDate(),
                bookingDTO.getPreferredTime(),
                bookingDTO.getCustomerId()
        );

        booking.setCustomerId(userDetails.getOurUsers().getId());
        return booking;
    }

    @Override
    public ResponseDTO updateBooking(BookingDTO bookingDTO){
        ResponseDTO response = new ResponseDTO();

        try {
            // Attempt to find the booking by ID
            Booking booking = bookingRepo.findById(bookingDTO.getBookingId())
                    .orElseThrow(() -> new OurException("No booking found"));

            booking.setStatus(bookingDTO.getStatus());
            bookingRepo.save(booking);
            response.setStatusCode(200);
            response.setMessage("Successfully updated booking");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            // Handle other unexpected exceptions
            response.setStatusCode(500);
            response.setMessage("Error occurred while updating booking: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO getAllBooking(){
        ResponseDTO response = new ResponseDTO();

        try {

            List<Booking> bookings = bookingRepo.findAll();
//            List<BookingDTO> bookingDTOList = new ArrayList<>();
//            for (Booking booking: bookings){
//                BookingDTO bookingDTO = new BookingDTO(
//                        booking.getBookingId(),
//                        booking.getVehicleNo(),
//                        booking.getBrand(),
//                        booking.getModel(),
//                        booking.getStatus(),
//                        booking.getPreferredDate(),
//                        booking.getPreferredTime()
//                );
//                bookingDTOList.add(bookingDTO);
//            }
            if (!bookings.isEmpty()){
                response.setBookingList(bookings);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                throw new OurException("No booking Found");
            }
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while bookings: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO getCustomerBookings(ResponseDTO userDetails) {
        ResponseDTO response = new ResponseDTO();
        List<Booking> bookingList = new ArrayList<>();
        try {
            List<Booking> bookings = bookingRepo.findAll();

            // Filter bookings for the customer based on customerId
            for (Booking booking : bookings) {
                if (Objects.equals(booking.getCustomerId(), userDetails.getOurUsers().getId())) {
                    bookingList.add(booking);  // Add to the initialized list
                }
            }

            response.setBookingList(bookingList);

            // Check if the list has any bookings
            if (!response.getBookingList().isEmpty()) {
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                throw new OurException("No booking found for this customer");
            }
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching bookings: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO updateWaitingBooking(BookingDTO bookingDTO) {
        ResponseDTO response = new ResponseDTO();
        try {
            Booking booking = bookingRepo.findById(bookingDTO.getBookingId())
                    .orElseThrow(() -> new OurException("No booking found"));

            booking.setStatus(bookingDTO.getStatus());
            booking.setBrand(bookingDTO.getBrand());
            booking.setModel(bookingDTO.getModel());
            booking.setPreferredDate(bookingDTO.getPreferredDate());
            booking.setPreferredTime(bookingDTO.getPreferredTime());

            bookingRepo.save(booking);
            response.setStatusCode(200);
            response.setMessage("Successfully updated booking");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while updating booking: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO deleteWaitingBooking(BookingDTO bookingDTO) {
        ResponseDTO response = new ResponseDTO();
        try {
            if (bookingRepo.existsById(bookingDTO.getBookingId())) {
                bookingRepo.deleteById(bookingDTO.getBookingId());

                response.setStatusCode(200);
                response.setMessage("Successfully deleted booking");
            } else {
                throw new OurException("No booking found to delete");
            }

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while deleting booking: " + e.getMessage());
        }

        return response;
    }


}