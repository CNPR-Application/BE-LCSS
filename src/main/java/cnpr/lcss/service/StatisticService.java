package cnpr.lcss.service;

import cnpr.lcss.repository.*;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class StatisticService {
    @Autowired
    ClassRepository classRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    RegisteringGuestRepository registeringGuestRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    BranchRepository branchRepository;

    //<editor-fold desc="16.07-get-manager-statistic-in-month">
    public ResponseEntity<?> getManagerStatistic(Date date, int branchId) throws Exception {
        try {
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            if (!branchRepository.existsById(branchId)) {
                throw new IllegalArgumentException(Constant.INVALID_BRANCH_ID);
            }
            List<String> status = new ArrayList<>();
            status.add(Constant.CLASS_STATUS_STUDYING);
            status.add(Constant.CLASS_STATUS_FINISHED);

            /** count
             * newClass: studying/finished classes have opening dates from insertDate
             * newBooking : booking have payingDate from insertDate
             * newRegisteredInfo: registerinfo have bookingDate from insertDate
             * newStudent: student have account creating date from insertDate
             * newTeacher: teacher have account creating date from insertDate
             */

            int newBooking = bookingRepository.countDistinctByPayingDateIsGreaterThanEqual(date);
            int newRegisteredInfo = registeringGuestRepository.countDistinctByBookingDateIsGreaterThanEqual(date);
            int newStudent = studentRepository.countStudentByAccount_CreatingDateIsGreaterThanEqual(date);
            int newTeacher = teacherRepository.countTeacherByAccount_CreatingDateIsGreaterThanEqual(date);

            //Set a new Date= insertDate with a Date = 01 to count from that Month
            Date classDate = date;
            classDate.setDate(01);

            int newClass = classRepository.countDistinctByBranch_BranchIdAndStatusIsInAndOpeningDateGreaterThanEqual(branchId, status, classDate);
            mapObj.put("newClass", newClass);
            mapObj.put("newBooking", newBooking);
            mapObj.put("newRegisteredInfo", newRegisteredInfo);
            mapObj.put("newStudent", newStudent);
            mapObj.put("newTeacher", newTeacher);
            return ResponseEntity.status(HttpStatus.OK).body(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
