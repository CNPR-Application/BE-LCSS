package cnpr.lcss.repository;

import cnpr.lcss.dao.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Integer> {
    Page<Class> findByBranch_BranchIdAndSubject_SubjectIdAndShift_ShiftIdAndStatusContainingIgnoreCase(int branchId, int subjectId, int shiftId, String status, Pageable pageable);

    Page<Class> findClassByBranch_BranchIdAndStatus(int branchId, String status, Pageable pageable);

    @Query(value = "SELECT c.status " +
            "FROM Class AS c " +
            "WHERE c.classId = :classId")
    String findStatusByClassId(@Param(value = "classId") int classId);

    Class findClassByClassId(int classId);

    Page<Class> findByBranch_BranchIdAndSubject_SubjectId(int branchId, int subjectId, Pageable pageable);

    Page<Class> findByBranch_BranchIdAndSubject_SubjectIdAndShift_ShiftId(int branchId, int subjectId, int shiftId, Pageable pageable);

    Page<Class> findByBranch_BranchIdAndSubject_SubjectIdAndStatusContainingAllIgnoreCase(int branchId, int subjectId, String status, Pageable pageable);

    Page<Class> findByBranch_BranchIdAndShift_ShiftId(int branchId, int shiftId, Pageable pageable);

    Page<Class> findByBranch_BranchIdAndShift_ShiftIdAndStatusContainingAllIgnoreCase(int branchId, int shiftId, String status, Pageable pageable);

    Page<Class> findByBranch_BranchIdAndStatusContainingAllIgnoreCase(int branchId, String status, Pageable pageable);

    Page<Class> findByBranch_BranchId(int branchId, Pageable pageable);

    @Query(value = "SELECT COUNT(c.classId) " +
            "FROM Class AS c " +
            "WHERE c.status = :status")
    int countByStatusAllIgnoreCase(@Param(value = "status") String status);

    @Query(value = "SELECT COUNT(c.classId) " +
            "FROM Class AS c " +
            "WHERE c.branch.branchId = :branchId " +
            "AND c.status = :status")
    int countDistinctByBranch_BranchIdAndStatusAllIgnoreCase(@Param(value = "branchId") int branchId,
                                                             @Param(value = "status") String status);

    @Query(value = "SELECT c.subject.subjectId " +
            "FROM Class AS c " +
            "WHERE c.classId = :classId")
    int findSubjectIdByClassId(@Param(value = "classId") int classId);

    Page<Class> findClassByClassIdIsInAndStatusOrderByOpeningDateDesc(List<Integer> list, String status, Pageable pageable);

    @Query("select distinct c from Class c " +
            "join c.studentInClassList studentInClassList " +
            "join c.sessionList sessionList " +
            "where studentInClassList.student.id = ?1 " +
            "and c.status = ?2 " +
            "and studentInClassList.teacherRating = 0")
    List<Class> findClassesNeedFeedback(Integer id, String status);

    @Query(
            nativeQuery = true,
            value = "select c.class_id, c.status, sic.student_id " +
                    "from class as c " +
                    "join student_in_class as sic on c.class_id = sic.class_id " +
                    "where c.class_id = :classId"
    )
    List<Class> findStudentByClassId(@Param(value = "classId") int classId);

    @Query(value = "select c.classId from Subject as s join Class c on s.subjectId = c.subject.subjectId join TeachingSubject ts on s.subjectId = ts.subject.subjectId join Teacher t on ts.teacher.teacherId = t.teacherId where t.account.username = 'ngagv000006'")
    List<Integer> findAllByTeacherUsername(String username);
    //, c.className, s.subjectId, s.subjectName, c.status, t.account.username, t.teacherId

    @Query("select c from Class c where c.status = ?1")
    List<Class> findByStatus(String status);
}
