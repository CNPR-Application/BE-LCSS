package cnpr.lcss.repository;

import cnpr.lcss.dao.TeachingSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachingSubjectRepository extends JpaRepository<TeachingSubject, Integer> {
    List<TeachingSubject> findByTeacher_TeacherId(int teacherId);
}
