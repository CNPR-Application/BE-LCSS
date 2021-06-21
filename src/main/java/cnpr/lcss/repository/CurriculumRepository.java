package cnpr.lcss.repository;

import cnpr.lcss.dao.Curriculum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface    CurriculumRepository extends JpaRepository<Curriculum, Integer> {

    Page<Curriculum> findByCurriculumNameContainingIgnoreCaseAndIsAvailableIsTrue(String keyword, Pageable pageable);

    Page<Curriculum> findByCurriculumCodeContainingIgnoreCaseAndIsAvailableIsTrue(String keyword, Pageable pageable);

    Curriculum findOneByCurriculumId(int curriculumId);

    Boolean existsCurriculumByCurriculumCode(String curriculumCode);

    Boolean existsCurriculumByCurriculumName(String curriculumName);
}