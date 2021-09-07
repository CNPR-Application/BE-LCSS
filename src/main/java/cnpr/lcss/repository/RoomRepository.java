package cnpr.lcss.repository;

import cnpr.lcss.dao.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Room findByRoomNo(int roomNo);
    Room findByRoomId(int roomId);
}