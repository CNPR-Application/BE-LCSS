package cnpr.lcss.repository;

import cnpr.lcss.dao.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface SubjectRepository extends JpaRepository<Subject,Integer> {
    Page<Subject> findSubjectByCurriculumIddAndAndIsAvailableIsTrue(int keyword, Pageable pageable);


}
