package com.flexidesk.service.impl;

import com.flexidesk.entity.Booking;
import com.flexidesk.entity.Desk;
import com.flexidesk.entity.User;
import com.flexidesk.repository.BookingRepository;
import com.flexidesk.repository.DeskRepository;
import com.flexidesk.repository.UserRepository;
import com.flexidesk.service.BookingService;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final DeskRepository deskRepository;
    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository,
                              DeskRepository deskRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.deskRepository = deskRepository;
    }
    public Booking createBooking(Long userId,Long deskId, LocalDate startDate,
                                 LocalDate endDate){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Desk desk = deskRepository.findById(deskId)
                .orElseThrow(() -> new RuntimeException("Desk not found"));
        if (desk.getAvailable()<=0) {
            throw new RuntimeException("Desk is already booked");
        }
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setDesk(desk);
        booking.setStartdate(startDate);
        booking.setEnddate(endDate);
        booking.setBookingTime(LocalDateTime.now());

        desk.setAvailable(desk.getAvailable()-1);
        deskRepository.save(desk);

        return bookingRepository.save(booking);

    }
    @Override
    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Desk desk = booking.getDesk();
        desk.setAvailable( Math.min(desk.getAvailable() + 1, desk.getTotalSlots()));
        deskRepository.save(desk);

        bookingRepository.delete(booking);
    }



}
