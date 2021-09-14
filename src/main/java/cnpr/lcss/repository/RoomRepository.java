package cnpr.lcss.repository;

import cnpr.lcss.dao.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Room findByRoomName(int roomName);

    Room findByRoomId(int roomId);
}
