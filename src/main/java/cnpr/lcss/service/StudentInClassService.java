package cnpr.lcss.service;

import cnpr.lcss.dao.StudentInClass;
import cnpr.lcss.dao.Subject;
import cnpr.lcss.dao.Teacher;
import cnpr.lcss.model.FeedbackDto;
import cnpr.lcss.model.StudentInClassSearchResponseDto;
import cnpr.lcss.repository.*;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentInClassService {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ClassRepository classRepository;
    @Autowired
    StudentInClassRepository studentInClassRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    TeacherRepository teacherRepository;

    //<editor-fold desc="Generate Rating">
    public String generateRating(int newRating, String currentRating) {
        String[] arrOfInpStr = currentRating.split(Constant.SYMBOL_SLASH);
        // Rating = number of rating / number of people
        String finalResult = (Double.parseDouble(arrOfInpStr[0]) + newRating)
                + Constant.SYMBOL_SLASH
                + (Double.parseDouble(arrOfInpStr[1]) + Constant.NO_OF_RATING_PPL_ADD_ON);
        return finalResult;
    }
    //</editor-fold>

    //<editor-fold desc="10.01-move-student-to-opening-class">
    public ResponseEntity<?> moveStudentToOpeningClass(int classId, List<Integer> bookingIdList) {
        try {
/*            for (int bookingId : bookingIdList) {
                StudentInClass studentInClass = studentInClassRepository.findStudentInClassByBooking_BookingId(bookingId);
                // find class by classId (in param)
                Class aClass = classRepository.findClassByClassId(classId);
                studentInClass.setAClass(aClass);
                // update new class field in student in class
                studentInClassRepository.save(studentInClass);
            }*/
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
        return ResponseEntity.ok(Boolean.TRUE);
    }
    //</editor-fold>

    //<editor-fold desc="10.02-student-gives-feedback">
    public ResponseEntity<?> studentGivesFeedback(HashMap<String, Object> reqBody) throws Exception {
        try {
            int studentInClassId = (int) reqBody.get("studentInClassId");
            int subjectId = (int) reqBody.get("subjectId");
            int teacherId = (int) reqBody.get("teacherId");
            int teacherRating = (int) reqBody.get("teacherRating");
            int subjectRating = (int) reqBody.get("subjectRating");
            String feedback = (String) reqBody.get("feedback");

            StudentInClass updateSIC = studentInClassRepository.getById(studentInClassId);
            updateSIC.setTeacherRating(teacherRating);
            updateSIC.setSubjectRating(subjectRating);
            updateSIC.setFeedback(feedback);
            studentInClassRepository.save(updateSIC);

            try {
                Subject ratingSubject = subjectRepository.getById(subjectId);
                String updateRatingSubject = generateRating(subjectRating, ratingSubject.getRating());
                ratingSubject.setRating(updateRatingSubject);
                subjectRepository.save(ratingSubject);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(Constant.ERROR_GENERATE_SUBJECT_RATING);
            }

            try {
                Teacher ratingTeacher = teacherRepository.findByTeacherId(teacherId);
                String updateRatingTeacher = generateRating(teacherRating, ratingTeacher.getRating());
                ratingTeacher.setRating(updateRatingTeacher);
                teacherRepository.save(ratingTeacher);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(Constant.ERROR_GENERATE_TEACHER_RATING);
            }

            return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="10.04-get-student-in-class-by-class-id">
    public ResponseEntity<?> findStudentInClassByClassId(int classId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<StudentInClass> page = studentInClassRepository.findStudentInClassByaClass_ClassId(classId, pageable);
        List<StudentInClassSearchResponseDto> responseDtos = page.getContent().stream()
                .map(studentInClass -> studentInClass.convertToSearchDto()).collect(Collectors.toList());
        HashMap<String, Object> mapObj = new LinkedHashMap<>();
        mapObj.put("pageNo", pageNo);
        mapObj.put("pageSize", pageSize);
        mapObj.put("totalPage", page.getTotalPages());
        mapObj.put("studentInClassSearchResponseDtos", responseDtos);
        return ResponseEntity.ok(mapObj);
    }
    //</editor-fold>

    //<editor-fold desc="10.06-manager-view-feedback">
    public ResponseEntity<?> getFeedbackForManager(int classId, int pageNo, int pageSize) throws Exception {
        try {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<StudentInClass> studentInClassPage = studentInClassRepository.findStudentInClassByaClass_ClassId(classId, pageable);
            List<FeedbackDto> feedbackList = studentInClassPage.getContent()
                    .stream().map(studentInClass -> studentInClass.convertToFeedbackDto()).collect(Collectors.toList());
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);
            mapObj.put("totalPage", studentInClassPage.getTotalPages());
            mapObj.put("feedbackList", feedbackList);
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
