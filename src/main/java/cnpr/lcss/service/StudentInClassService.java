package cnpr.lcss.service;

import cnpr.lcss.dao.StudentInClass;
import cnpr.lcss.model.StudentInClassSearchPagingResponseDto;
import cnpr.lcss.model.StudentInClassSearchResponseDto;
import cnpr.lcss.repository.StudentInClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentInClassService {
    @Autowired
    StudentInClassRepository studentInClassRepository;

    public StudentInClassSearchPagingResponseDto findStudentInClassbyClassId(int classId, int pageNo, int pageSize) {
        // pageNo starts at 0
        // always set first page = 1 ---> pageNo - 1
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<StudentInClass> page = studentInClassRepository.findStudentInClassByaClass_ClassId(classId, pageable);
        List<StudentInClass> studentInClasses = page.getContent();
        List<StudentInClassSearchResponseDto> studentInClassSearchResponseDtos = studentInClasses.stream().map(studentInClass -> studentInClass.convertToSearchDto()).collect(Collectors.toList());
        int pageTotal = page.getTotalPages();

        StudentInClassSearchPagingResponseDto studentInClassSearchPagingResponseDto = new StudentInClassSearchPagingResponseDto(pageNo, pageSize, pageTotal, studentInClassSearchResponseDtos);

        return studentInClassSearchPagingResponseDto;
    }
}
