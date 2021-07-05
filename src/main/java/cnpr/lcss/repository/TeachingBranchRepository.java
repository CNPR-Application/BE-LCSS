package cnpr.lcss.repository;

import cnpr.lcss.dao.TeachingBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingBranchRepository extends JpaRepository<TeachingBranch, Integer> {

    boolean existsByTeacher_TeacherIdAndBranch_BranchId(int teacherId, int branchId);
}
