package cnpr.lcss.repository;

import cnpr.lcss.dao.Curriculum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface    CurriculumRepository extends JpaRepository<Curriculum, Integer> {

    Page<Curriculum> findByCurriculumNameContainingIgnoreCaseAndIsAvailable(@Param("name") String name,
                                                                            @Param("isAvailable") boolean isAvailable,
                                                                            Pageable pageable);

    Page<Curriculum> findByCurriculumCodeContainingIgnoreCaseAndIsAvailable(@Param("code") String code,
                                                                            @Param("isAvailable") boolean isAvailable,
                                                                            Pageable pageable);

    Curriculum findOneByCurriculumId(int curriculumId);

    Boolean existsCurriculumByCurriculumCode(String curriculumCode);

    Boolean existsCurriculumByCurriculumName(String curriculumName);
}