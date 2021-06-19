package cnpr.lcss.repository;

import cnpr.lcss.dao.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    int countByCurriculum_CurriculumId(int curriculumId);

    List<Subject> findAllByCurriculum_CurriculumId(int curriculumId);
}
