package cnpr.lcss.service;

import cnpr.lcss.model.BookingRequestDto;
import cnpr.lcss.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    //<editor-fold desc="Create New Booking">
    public ResponseEntity<?> createNewBooking(BookingRequestDto insBooking) throws Exception {
        try {
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
