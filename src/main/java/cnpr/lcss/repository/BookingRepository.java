package cnpr.lcss.repository;

import cnpr.lcss.dao.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Booking findBookingByBookingId(int bookingId);

    Page<Booking> findBookingByStudent_Id(int studentId,Pageable pageable);
}
