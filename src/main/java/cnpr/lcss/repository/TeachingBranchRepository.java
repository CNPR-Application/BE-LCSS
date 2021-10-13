package cnpr.lcss.repository;

import cnpr.lcss.dao.TeachingBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachingBranchRepository extends JpaRepository<TeachingBranch, Integer> {
    boolean existsByTeacher_TeacherIdAndBranch_BranchId(int teacherId, int branchId);

    List<TeachingBranch> findByTeacher_TeacherId(int teacherId);

    @Query("select t from TeachingBranch t where t.branch.branchId = ?1 and t.teacher.teacherId = ?2")
    TeachingBranch findByBranch_BranchIdAndTeacher_TeacherId(int branchId, int teacherId);
}
