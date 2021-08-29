package cnpr.lcss.service;

import cnpr.lcss.dao.Class;
import cnpr.lcss.dao.StudentInClass;
import cnpr.lcss.model.StudentInClassSearchPagingResponseDto;
import cnpr.lcss.model.StudentInClassSearchResponseDto;
import cnpr.lcss.repository.BookingRepository;
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
    @Autowired
    BookingRepository bookingRepository;

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
    public ResponseEntity<?> moveStudentToOpeningClass(int classId, List<Integer> bookingId) {

        try {
            for (int bookingIdCheck : bookingId) {
                StudentInClass studentInClass = studentInClassRepository.findStudentInClassByBooking_BookingId(bookingIdCheck);
                //find class by classId(in param)
                Class aClass = classRepository.findClassByClassId(classId);
                studentInClass.setAClass(aClass);
                // update new class field in student in class
                studentInClassRepository.save(studentInClass);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
        return ResponseEntity.ok(Boolean.TRUE);

    }
    //</editor-fold>
}
