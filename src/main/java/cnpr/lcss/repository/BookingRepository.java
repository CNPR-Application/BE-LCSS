package cnpr.lcss.repository;

import cnpr.lcss.dao.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query(value = "SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.bookingId = :bookingId")
    Booking findBookingByBookingId(@Param(value = "bookingId") int bookingId);

    boolean existsBookingByBookingId(int bookingId);

    Page<Booking> findBookingByStudent_Account_Username(String studentUsername, Pageable pageable);

    int countBookingByaClass_ClassId(int classId);
}
