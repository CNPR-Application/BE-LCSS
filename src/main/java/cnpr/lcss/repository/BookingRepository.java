package cnpr.lcss.repository;

import cnpr.lcss.dao.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Booking findBookingByBookingId(int bookingId);

    boolean existsBookingByBookingId(int bookingId);

    Page<Booking> findBookingByStudent_Account_Username(String studentUsername, Pageable pageable);

    int countBookingByAClass_ClassId(int classId);
}
