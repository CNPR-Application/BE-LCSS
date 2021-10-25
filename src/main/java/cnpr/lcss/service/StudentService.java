package cnpr.lcss.service;

import cnpr.lcss.dao.Class;
import cnpr.lcss.dao.*;
import cnpr.lcss.model.StudentDto;
import cnpr.lcss.repository.AccountRepository;
import cnpr.lcss.repository.ClassRepository;
import cnpr.lcss.repository.StudentInClassRepository;
import cnpr.lcss.repository.StudentRepository;
import cnpr.lcss.util.Constant;
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
    @Autowired
    ClassRepository classRepository;
    @Autowired
    StudentInClassRepository studentInClassRepository;
    @Autowired
    AccountRepository accountRepository;

    //<editor-fold desc="1.13-search-student-in-branch">
    public ResponseEntity<?> findStudentInABranch(int branchId, boolean isAvailable, int pageNo, int pageSize) {
        try {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<Student> studentList = studentRepository.findStudentByBranch_BranchIdAndAccount_IsAvailable(branchId, isAvailable, pageable);
            List<StudentDto> studentDtoList = studentList.getContent().stream().map(student -> student.convertToDto()).collect(Collectors.toList());
            int pageTotal = studentList.getTotalPages();
            Map<String, Object> mapObj = new LinkedHashMap<>();
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
    //</editor-fold>

    //<editor-fold desc="1.14 Delete Student">
    public ResponseEntity<?> deleteStudent(String username) throws Exception {
        try {
            Account account = accountRepository.findOneByUsername(username);
            if (account == null) {
                throw new IllegalArgumentException(Constant.INVALID_USERNAME);
            } else {
                Student student = account.getStudent();
                Boolean studentBookingAbleToDelete = true;
                Boolean studentClassAbleToDelete = true;
                // check is there any this student's booking is paid, if there are student UNABLE TO DELETE
                List<Booking> bookingList = student.getBookingList();
                for (Booking booking : bookingList) {
                    if (booking.getStatus().matches(Constant.BOOKING_STATUS_PAID))
                        studentBookingAbleToDelete = false;
                }
                if (!studentBookingAbleToDelete) {
                    throw new IllegalArgumentException(Constant.ERROR_DELETE_STUDENT_BOOKING);
                }
                // check each class of this student,
                // is there any class with status waiting or studying,
                // if so, it should not be deleted
                List<StudentInClass> studentInClassList = student.getStudentInClassList();
                for (StudentInClass studentInClass : studentInClassList) {
                    Class aClass = studentInClass.getAClass();
                    if (aClass.getStatus().matches(Constant.CLASS_STATUS_WAITING)
                            || aClass.getStatus().matches(Constant.CLASS_STATUS_STUDYING))
                        studentClassAbleToDelete = false;
                }
                if (!studentClassAbleToDelete) {
                    throw new IllegalArgumentException(Constant.ERROR_DELETE_STUDENT_CLASS);
                }

                if (studentBookingAbleToDelete && studentClassAbleToDelete) {
                    account.setIsAvailable(false);
                    accountRepository.save(account);
                    return ResponseEntity.ok(Boolean.TRUE);
                } else {
                    return ResponseEntity.ok(Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="1.17-search-student-by-name-and-phone">
    public ResponseEntity<?> searchStudentInBranchByPhoneAndNameAndIsAvailable(int branchId, boolean isAvailable, String phone, String name, int pageNo, int pageSize) {
        try {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<Student> studentList = studentRepository.findStudentByBranch_BranchIdAndAccount_IsAvailableAndAccount_PhoneContainingIgnoreCaseAndAccount_NameContainingIgnoreCase(branchId, isAvailable,phone,name, pageable);
            List<StudentDto> studentDtoList = studentList.getContent().stream().map(student -> student.convertToDto()).collect(Collectors.toList());
            int pageTotal = studentList.getTotalPages();
            Map<String, Object> mapObj = new LinkedHashMap<>();
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
    //</editor-fold>

}
