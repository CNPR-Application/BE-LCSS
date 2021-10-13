package cnpr.lcss.service;

import cnpr.lcss.dao.Student;
import cnpr.lcss.dao.Subject;
import cnpr.lcss.model.StudentDto;
import cnpr.lcss.model.SubjectSearchDto;
import cnpr.lcss.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    public ResponseEntity<?> findStudentInABranch(int branchId, boolean isAvailable, int pageNo, int pageSize) {

        try {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Map<String, Object> mapObj = new LinkedHashMap<>();

            Page<Student> studentList = studentRepository.findStudentByBranch_BranchIdAndAccount_IsAvailable(branchId, isAvailable, pageable);
            List<StudentDto> studentDtoList = studentList.getContent().stream().map(student -> student.convertToDto()).collect(Collectors.toList());
            int pageTotal = studentList.getTotalPages();


            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);
            mapObj.put("pageTotal", pageTotal);
            mapObj.put("studentResponseDtos", studentDtoList);

            return ResponseEntity.ok(mapObj);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
