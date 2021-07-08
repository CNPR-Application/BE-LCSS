package cnpr.lcss.repository;

import cnpr.lcss.dao.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<Class, Integer> {

    Page<Class> findByBranch_BranchIdAndSubject_SubjectIdAndShift_ShiftIdAndStatus(int branchId, int subjectId, int shiftId, String status, Pageable pageable);

    Page<Class> findClassByBranch_BranchIdAndStatus(int branchId, String status, Pageable pageable);
}
