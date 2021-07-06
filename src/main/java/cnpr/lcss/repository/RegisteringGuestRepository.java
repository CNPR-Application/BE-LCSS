package cnpr.lcss.repository;

import cnpr.lcss.dao.RegisteringGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteringGuestRepository extends JpaRepository<RegisteringGuest, Integer> {

}
