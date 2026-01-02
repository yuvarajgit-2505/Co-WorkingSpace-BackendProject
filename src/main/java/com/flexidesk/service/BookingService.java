package com.flexidesk.service;

import com.flexidesk.entity.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    Booking createBooking(Long userId, Long deskId, LocalDate startDate,
                          LocalDate endDate);

    List<Booking> getBookingsByUser(Long userId);
    List<Booking> getAllBookings();
    void cancelBooking(Long bookingId);

}
