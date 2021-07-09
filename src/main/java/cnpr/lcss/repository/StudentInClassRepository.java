package cnpr.lcss.repository;

import cnpr.lcss.dao.StudentInClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentInClassRepository extends JpaRepository<StudentInClass, Integer> {

}
