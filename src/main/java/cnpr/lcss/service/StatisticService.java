package cnpr.lcss.service;

import cnpr.lcss.dao.Booking;
import cnpr.lcss.dao.Branch;
import cnpr.lcss.model.AdminStatisticInMonthResponse;
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
    BranchRepository branchRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;

    //<editor-fold desc="16.07-get-manager-statistic-in-month">
    public ResponseEntity<?> getManagerStatistic(Date date, int branchId) throws Exception {
        try {
            if (!branchRepository.existsById(branchId)) {
                throw new IllegalArgumentException(Constant.INVALID_BRANCH_ID);
            }
            List<String> status = new ArrayList<>();
            status.add(Constant.CLASS_STATUS_STUDYING);
            status.add(Constant.CLASS_STATUS_FINISHED);

            /** count
             * newClass: studying/finished classes have opening dates from insertDate
             * newBooking : booking have payingDate from insertDate
             * newRegisteredInfo: register info have bookingDate from insertDate
             * newStudent: student have account creating date from insertDate
             */

            long monthRevenue = 0;
            int newBooking = 0;
            List<Booking> bookingList = bookingRepository.findDistinctByPayingDateIsGreaterThanEqualAndBranch_BranchId(date, branchId);
            for (Booking booking : bookingList) {
                monthRevenue += booking.getPayingPrice();
                //count booking
                newBooking++;

            }
            int newRegisteredInfo = registeringGuestRepository.countDistinctByBookingDateIsGreaterThanEqualAndBranch_BranchId(date, branchId);
            int newStudent = studentRepository.countStudentByAccount_CreatingDateIsGreaterThanEqualAndBranch_BranchIdAndAccount_IsAvailable(date, branchId, Boolean.TRUE);

            //Set a new Date= insertDate with a Date = 01 to count from that Month
            Date classDate = date;
            classDate.setDate(01);

            int newClass = classRepository.countDistinctByBranch_BranchIdAndStatusIsInAndOpeningDateGreaterThanEqual(branchId, status, classDate);

            int totalClass = classRepository.countDistinctByBranch_BranchId(branchId);
            int totalBooking = bookingRepository.countBookingByBranch_BranchId(branchId);
            int totalRegisteredInfo = registeringGuestRepository.countRegisteringGuestByBranch_BranchId(branchId);
            int totalStudent = studentRepository.countStudentByBranch_BranchIdAndAccount_IsAvailable(branchId, Boolean.TRUE);


            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            mapObj.put("newClass", newClass);
            mapObj.put("newBooking", newBooking);
            mapObj.put("newRegisteredInfo", newRegisteredInfo);
            mapObj.put("newStudent", newStudent);
            mapObj.put("monthRevenue", monthRevenue);
            mapObj.put("totalClass", totalClass);
            mapObj.put("totalBooking", totalBooking);
            mapObj.put("totalRegisteredInfo", totalRegisteredInfo);
            mapObj.put("totalStudent", totalStudent);

            return ResponseEntity.status(HttpStatus.OK).body(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="16.08-get-admin-statistic-">
    public ResponseEntity<?> getAdminStatistic(Date date) throws Exception {
        try {
            List<Branch> branchList = branchRepository.findAll();
            List<AdminStatisticInMonthResponse> adminStatisticInMonthResponsesList = new ArrayList<>();
            int totalNewClass = 0;
            int totalNewBooking = 0;
            int totalNewRegisteredInfo = 0;
            int totalNewStudent = 0;
            int totalNewTeacher = 0;
            long totalMonthRevenue = 0;
            Date dateFromRequest = (Date) date.clone();
            Date dateFromRequestWith1stDate = (Date) date.clone();
            //loop each branch
            for (Branch branch : branchList) {
                int branchId = branch.getBranchId();
                //get each branch statistic and add to Response List
                List<String> status = new ArrayList<>();
                status.add(Constant.CLASS_STATUS_STUDYING);
                status.add(Constant.CLASS_STATUS_FINISHED);

                long monthRevenue = 0;
                int newBooking = 0;
                List<Booking> bookingList = bookingRepository.findDistinctByPayingDateIsGreaterThanEqualAndBranch_BranchId(dateFromRequest, branchId);
                for (Booking booking : bookingList) {
                    monthRevenue += booking.getPayingPrice();
                    //count booking
                    newBooking++;

                }

                int newRegisteredInfo = registeringGuestRepository.countDistinctByBookingDateIsGreaterThanEqualAndBranch_BranchId(dateFromRequest, branchId);
                int newStudent = studentRepository.countStudentByAccount_CreatingDateIsGreaterThanEqualAndBranch_BranchIdAndAccount_IsAvailable(dateFromRequest, branchId, Boolean.TRUE);
                long newTeacher = teacherRepository.countTeacherByBranch_BranchIdAndAccount_CreatingDateIsGreaterThanEqualAndAccount_IsAvailable(branchId, dateFromRequest, Boolean.TRUE);

                //Set a new Date = insertDate with a Date = 01 to count from that Month

                dateFromRequestWith1stDate.setDate(01);
                int newClass = classRepository.countDistinctByBranch_BranchIdAndStatusIsInAndOpeningDateGreaterThanEqual(branchId, status, dateFromRequestWith1stDate);

                int totalClass = classRepository.countDistinctByBranch_BranchId(branchId);
                int totalBooking = bookingRepository.countBookingByBranch_BranchId(branchId);
                int totalRegisteredInfo = registeringGuestRepository.countRegisteringGuestByBranch_BranchId(branchId);
                int totalStudent = studentRepository.countStudentByBranch_BranchIdAndAccount_IsAvailable(branchId, Boolean.TRUE);
                int totalTeacher = teacherRepository.countDistinctByTeachingBranchList_Branch_BranchIdAndAccount_IsAvailable(branchId, Boolean.TRUE);
                AdminStatisticInMonthResponse adminStatisticInMonthResponse = new AdminStatisticInMonthResponse();
                adminStatisticInMonthResponse.setBranchId(branchId);
                adminStatisticInMonthResponse.setBranchName(branch.getBranchName());
                adminStatisticInMonthResponse.setNewClass(newClass);
                adminStatisticInMonthResponse.setNewBooking(newBooking);
                adminStatisticInMonthResponse.setNewRegisteredInfo(newRegisteredInfo);
                adminStatisticInMonthResponse.setNewStudent(newStudent);
                adminStatisticInMonthResponse.setNewTeacher(newTeacher);
                adminStatisticInMonthResponse.setMonthRevenue(monthRevenue);
                adminStatisticInMonthResponse.setTotalClass(totalClass);
                adminStatisticInMonthResponse.setTotalBooking(totalBooking);
                adminStatisticInMonthResponse.setTotalRegisteredInfo(totalRegisteredInfo);
                adminStatisticInMonthResponse.setTotalStudent(totalStudent);
                adminStatisticInMonthResponse.setTotalTeacher(totalTeacher);


                adminStatisticInMonthResponsesList.add(adminStatisticInMonthResponse);

                //count total for a center
                totalNewClass += newClass;
                totalNewBooking += newBooking;
                totalNewRegisteredInfo += newRegisteredInfo;
                totalNewStudent += newStudent;
                totalNewTeacher += newTeacher;
                totalMonthRevenue += monthRevenue;
            }

            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            mapObj.put("branchesStatisticResponseDtoList", adminStatisticInMonthResponsesList);
            mapObj.put("totalNewClass", totalNewClass);
            mapObj.put("totalNewBooking", totalNewBooking);
            mapObj.put("totalNewRegisteredInfo", totalNewRegisteredInfo);
            mapObj.put("totalNewStudent", totalNewStudent);
            mapObj.put("totalNewTeacher", totalNewTeacher);
            mapObj.put("totalMonthRevenue", totalMonthRevenue);
            return ResponseEntity.status(HttpStatus.OK).body(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
