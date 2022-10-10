package com.example.batchpjt.repository.booking;

import com.example.batchpjt.domain.booking.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {
}
