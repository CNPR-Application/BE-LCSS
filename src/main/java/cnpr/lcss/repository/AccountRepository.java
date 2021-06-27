package cnpr.lcss.repository;

import cnpr.lcss.dao.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Account findOneByUsername(String username);

    boolean existsByUsername(String username);

    String findRoleByUsername(String username);
}