package com.flexidesk.controller;

import com.flexidesk.entity.Booking;
import com.flexidesk.entity.User;
import com.flexidesk.repository.UserRepository;
import com.flexidesk.service.BookingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.flexidesk.dto.BookingRequestDTO;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private BookingService bookingService;
    private final UserRepository userRepository;

    public BookingController(BookingService bookingService, UserRepository userRepository) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }


    
    @PostMapping("/user/book")

    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequestDTO dto) {
        Booking booking = bookingService.createBooking(
                dto.getUserId(),
                dto.getDeskId(),
                dto.getStartDate(),
                dto.getEndDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/my")
    public  ResponseEntity<List<Booking>> myBookings(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        List<Booking> bookings = bookingService.getBookingsByUser(user.getId());
        return ResponseEntity.ok(bookings);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allbooking")
    public ResponseEntity<List<Booking>> getAllbookings() {
         List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Booking cancelled successfully");

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/book")
    public ResponseEntity<Booking> adminCreateBooking(@RequestBody BookingRequestDTO dto,Authentication auth) {
        System.out.println("Caller authorities: " + auth.getAuthorities());

            Booking booking= bookingService.createBooking(
                    dto.getUserId(),
                    dto.getDeskId(),
                    dto.getStartDate(),
                    dto.getEndDate());
                    return ResponseEntity.status(HttpStatus.CREATED).body(booking); 

    }

}
