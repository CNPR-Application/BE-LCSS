package cnpr.lcss.repository;

import cnpr.lcss.dao.RegisteringGuest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RegisteringGuestRepository extends JpaRepository<RegisteringGuest, Integer> {
    Page<RegisteringGuest> findRegisteringGuestByBranch_BranchIdAndCustomerNameContainingAndPhoneContainingAndCurriculum_CurriculumNameContaining(int branchId, String customerName, String phone, String curriculumName, Pageable pageable);

    Page<RegisteringGuest> findRegisteringGuestByBranch_BranchIdAndStatusContaining(int branchId, String status, Pageable pageable);

    int countDistinctByBookingDateIsGreaterThanEqual(Date date);
}
