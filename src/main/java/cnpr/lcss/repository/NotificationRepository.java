package cnpr.lcss.repository;

import cnpr.lcss.dao.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Page<Notification> getAllByReceiverUsername_UsernameContainingIgnoreCaseOrderByCreatingDateDesc(String receiverUsername, Pageable pageable);
}
