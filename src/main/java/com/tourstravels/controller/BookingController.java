package com.tourstravels.controller;

import com.tourstravels.entity.Booking;
import com.tourstravels.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/tours_travel/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        Booking savedBooking = bookingService.createBooking(booking);

        if (savedBooking != null) {
            return ResponseEntity.ok("Booking confirmed successfully!");
        } else {
            return ResponseEntity.status(500).body("Booking failed!");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
}
