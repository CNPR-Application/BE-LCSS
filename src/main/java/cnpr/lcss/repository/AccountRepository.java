package cnpr.lcss.repository;

import cnpr.lcss.dao.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Account findOneByUsername(String username);

    boolean existsByUsername(String username);

    @Query(value = "SELECT a.role " +
            "FROM Account a " +
            "WHERE a.username = :username")
    String findRoleByUsername(@Param(value = "username") String username);

    Page<Account> findByRoleEqualsAndUsernameContainingAndIsAvailable(@Param(value = "role") String role,
                                                                      @Param(value = "username") String keyword,
                                                                      @Param(value = "isAvailable") boolean isAvailable,
                                                                      Pageable pageable);
}