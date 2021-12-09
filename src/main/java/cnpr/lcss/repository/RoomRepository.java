package cnpr.lcss.repository;

import cnpr.lcss.dao.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Room findByRoomId(int roomId);

    @Query("select r from Room r left join r.classList classList where classList.classId = ?1")
    Room findByClassList_ClassId(int classId);

    @Query(
            nativeQuery = true,
            value = "SELECT DISTINCT r.room_id, r.room_name, r.is_available, r.branch_id " +
                    "FROM room AS r " +
                    "JOIN branch b on b.branch_id = r.branch_id " +
                    "WHERE b.branch_id = :branchId " +
                    "EXCEPT " +
                    "(SELECT r.room_id, r.room_name, r.is_available, r.branch_id " +
                    "FROM room AS r " +
                    "JOIN class c ON r.room_id = c.room_id " +
                    "JOIN shift s on c.shift_id = s.shift_id " +
                    "WHERE r.branch_id = :branchId " +
                    "AND s.shift_id = :shiftId)"
    )
    List<Room> findAvailableRoomsForOpeningClass(@Param(value = "branchId") int branchId,
                                                 @Param(value = "shiftId") int shiftId);

    Page<Room> findAllByBranch_BranchIdAndAndIsAvailable(int branchId, boolean isAvailable, Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "select distinct r.room_id " +
                    "from room as r " +
                    "join class c on r.room_id = c.room_id " +
                    "where c.class_id = ?1"
    )
    Integer findRoomIdByClassId(Integer classId);
}
