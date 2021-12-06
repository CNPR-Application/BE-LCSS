package cnpr.lcss.repository;

import cnpr.lcss.dao.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query(value = "SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.bookingId = :bookingId")
    Booking findBookingByBookingId(@Param(value = "bookingId") int bookingId);

    boolean existsBookingByBookingId(int bookingId);

    Page<Booking> findBookingByStudent_Account_Username(String studentUsername, Pageable pageable);

    @Query("select count(distinct b) from Booking b where b.aClass.classId = ?1 and b.status = ?2")
    long countWaitingBookingByClassIdAndStatusIsPaid(int classId, String status);

    @Query(
            nativeQuery = true,
            value = "select count(distinct b.booking_id) " +
                    "from class as c " +
                    "join booking b on c.class_id = b.class_id " +
                    "where c.class_id = ?1"
    )
    Integer countBookingByClassId(int classId);

    List<Booking> findBookingByStudent_Id(int studentId);

    Page<Booking> findBookingByaClass_ClassIdAndStatusContainingAllIgnoreCase(int classId, String status, Pageable pageable);

    Page<Booking> findBookingByStatusContainingAllIgnoreCase(String status, Pageable pageable);

    int countDistinctByPayingDateIsGreaterThanEqualAndBranch_BranchId(Date date, int branchId);

    int countBookingByBranch_BranchId(int branchId);

    List<Booking> findDistinctByPayingDateIsGreaterThanEqualAndBranch_BranchId(Date date, int branchId);
}
