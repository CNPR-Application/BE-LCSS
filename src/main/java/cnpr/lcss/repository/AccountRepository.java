package cnpr.lcss.repository;

import cnpr.lcss.dao.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {

    Account findOneByUsername(String username);
}
