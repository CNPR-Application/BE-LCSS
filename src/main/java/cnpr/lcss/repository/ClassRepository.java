package cnpr.lcss.repository;

import cnpr.lcss.dao.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ClassRepository extends JpaRepository<Class, Integer> {

    Page<Class> findByBranch_BranchIdAndSubject_SubjectIdAndShift_ShiftIdAndStatus(int branchId, int subjectId, int shiftId, String status, Pageable pageable);
}
