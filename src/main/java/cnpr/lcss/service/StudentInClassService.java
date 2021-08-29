package cnpr.lcss.service;

import cnpr.lcss.dao.Class;
import cnpr.lcss.dao.Student;
import cnpr.lcss.dao.StudentInClass;
import cnpr.lcss.model.BookingSearchResponseDto;
import cnpr.lcss.model.StudentDto;
import cnpr.lcss.model.StudentInClassSearchPagingResponseDto;
import cnpr.lcss.model.StudentInClassSearchResponseDto;
import cnpr.lcss.repository.ClassRepository;
import cnpr.lcss.repository.StudentInClassRepository;
import cnpr.lcss.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentInClassService {

    @Autowired
    StudentInClassRepository studentInClassRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    ClassRepository classRepository;
    //<editor-fold desc="10.04-get-student-in-class-by-class-id">
    public StudentInClassSearchPagingResponseDto findStudentInClassByClassId(int classId, int pageNo, int pageSize) {
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
    //</editor-fold>

    //<editor-fold desc="10.01-move-student-to-opening-class">
    public ResponseEntity<?> moveStudentToOpeningClass(int classId, List<BookingSearchResponseDto> bookingSearchResponseDto){

        try {
            //check each booking
            for (BookingSearchResponseDto bookingSearchResponseDto1 : bookingSearchResponseDto) {
                List<StudentInClass> studentInClass = studentInClassRepository.findStudentInClassByStudent_Id(bookingSearchResponseDto1.getStudentId());

                //because a student can book two booking
                //so a student can be in two class
                //check each student's class
                for (StudentInClass studentInClass1 : studentInClass) {
                    //find class by classId(in param)
                    Class aClass = classRepository.findClassByClassId(classId);
                    studentInClass1.setAClass(aClass);
                    //update new class field in student in class
                    studentInClassRepository.save(studentInClass1);
                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
        return ResponseEntity.ok(Boolean.TRUE);

    }
    //</editor-fold>
}
