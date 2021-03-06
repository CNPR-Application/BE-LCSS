package cnpr.lcss.controller;

import cnpr.lcss.dao.Branch;
import cnpr.lcss.model.*;
import cnpr.lcss.service.*;
import cnpr.lcss.util.Constant;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@EnableScheduling
public class RestApi {
    @Autowired
    AccountService accountService;
    @Autowired
    AttendanceService attendanceService;
    @Autowired
    BookingService bookingService;
    @Autowired
    BranchService branchService;
    @Autowired
    ClassService classService;
    @Autowired
    CurriculumService curriculumService;
    @Autowired
    FirebaseService fireBaseService;
    @Autowired
    RegisteringGuestService registeringGuestService;
    @Autowired
    SessionService sessionService;
    @Autowired
    ShiftService shiftService;
    @Autowired
    SubjectDetailService subjectDetailService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    StudentInClassService studentInClassService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    RoomService roomService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    StudentService studentService;
    @Autowired
    StaffService staffService;
    @Autowired
    SchedulerService schedulerService;
    @Autowired
    TeachingSubjectService teachingSubjectService;
    @Autowired
    StatisticService statisticService;

    //<editor-fold desc="Welcome Page">

    /**
     * @return
     * @apiNote welcome page
     */
    @CrossOrigin
    @RequestMapping(value = "/")

    public String welcome() {
        return "Welcome to LCSS - Language Center Support System!\n" + ZonedDateTime.now();
    }
    //</editor-fold>

    /**
     * -------------------------------ACCOUNT--------------------------------
     */

    //<editor-fold desc="1.01-check-login">

    /**
     * @param loginRequestDto
     * @return
     * @throws Exception
     * @apiNote 1.01-check-login
     * @author LamHNT - 2021.06.03
     */
    @CrossOrigin
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> checkLogin(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
        return accountService.checkLogin(loginRequestDto);
    }
    //</editor-fold>

    //<editor-fold desc="1.02-search-account-like-username-paging">

    /**
     * @param role
     * @param keyword
     * @param isAvailable
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @author LamHNT - 2021.11.10
     * @apiNote 1.02-search-account-like-username-paging
     */
    @CrossOrigin
    @RequestMapping(value = "/accounts", params = "role", method = RequestMethod.GET)
    public ResponseEntity<?> searchAccountLikeUsernamePaging(@RequestParam(value = "role") String role,
                                                             @RequestParam(value = "username") String keyword,
                                                             @RequestParam(value = "isAvailable") boolean isAvailable,
                                                             @RequestParam(value = "pageNo") int pageNo,
                                                             @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return accountService.searchAccountLikeUsernamePaging(role, keyword, isAvailable, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="1.03-search-account-like-name">

    /**
     * @param name
     * @param role
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 1.03-search account by name
     * @author HuuNT - 2021.09.22
     */
    @CrossOrigin
    @RequestMapping(value = "/account", params = "name", method = RequestMethod.GET)
    public ResponseEntity<?> searchAccountLikeNamePaging(@RequestParam(value = "role") String role,
                                                         @RequestParam(value = "name") String name,
                                                         @RequestParam(value = "pageNo") int pageNo,
                                                         @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return accountService.searchAccountLikeNamePaging(role, name, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="1.04-search-info-by-username">

    /**
     * @param username
     * @return
     * @apiNote 1.04-search-info-by-username
     * @author LamHNT - 2021.06.26
     */
    @CrossOrigin
    @RequestMapping(value = "/accounts/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> searchInfoByUsername(@PathVariable String username) throws Exception {
        return accountService.searchInfoByUsername(username);
    }


    //</editor-fold>

    //<editor-fold desc="1.05-create-new-account">

    /**
     * @param newAccount
     * @return
     * @throws Exception
     * @apiNote 1.05 Create New Account
     * @author LamHNT - 2021.06.30
     */
    @CrossOrigin
    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity<?> createNewAccount(@RequestBody NewAccountRequestDto newAccount) throws Exception {
        return accountService.createNewAccount(newAccount);
    }
    //</editor-fold>

    //<editor-fold desc="1.06-update-account">

    /**
     * @param username
     * @return
     * @throws Exception
     * @apiNote 1.06-update-account
     * @author LamHNT - 2021.10.20
     */
    @CrossOrigin
    @RequestMapping(value = "/accounts", params = "username", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAccount(@RequestParam String username,
                                           @RequestBody HashMap<String, Object> reqBody) throws Exception {
        return accountService.updateAccount(username, reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="1.07-update-role">

    /**
     * @param username
     * @param role
     * @return
     * @throws Exception
     * @apiNote 1.07-update-role
     * @author LamHNT - 2021.07.01
     */
    @CrossOrigin
    @RequestMapping(value = "/admin/role", method = RequestMethod.PUT)
    public ResponseEntity<?> updateRoleByUsername(@RequestParam(value = "username") String username,
                                                  @RequestBody String role) throws Exception {
        return accountService.updateRole(username, role);
    }
    //</editor-fold>

    //<editor-fold desc="1.08-delete-account">

    /**
     * @param keyword
     * @return
     * @throws Exception
     * @apiNote 1.08-delete-account
     * @author HuuNT - 2021.06.30
     */
    @CrossOrigin
    @RequestMapping(value = "/accounts", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAccountByUserName(@RequestParam(value = "username") String keyword) throws Exception {
        return accountService.deleteByUserName(keyword);
    }
    //</editor-fold>

    //<editor-fold desc="1.09-change-password">

    /**
     * @param username
     * @param reqBody
     * @return
     * @throws Exception
     * @apiNote 1.09-change-password
     * @author HuuNT - 2021.09.30
     */
    @CrossOrigin
    @RequestMapping(value = "/accounts-change-password", method = RequestMethod.PUT)
    public ResponseEntity<?> changePassword(@RequestParam(value = "username") String username,
                                            @RequestBody HashMap<String, Object> reqBody) throws Exception {
        return accountService.changePassword(username, reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="1.11-search-teachers-by-branch-id-and-by-subject-id">

    /**
     * @param branchId
     * @param subjectId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 1.11-search-teachers-by-branch-id-and-by-subject-id
     * @author LamHNT - 2021.08.29
     */
    @CrossOrigin
    @RequestMapping(value = "/teachers", method = RequestMethod.GET)
    public ResponseEntity<?> findTeachersByBranchIdAndSubjectId(int branchId, int subjectId, int pageNo, int pageSize) throws Exception {
        return teacherService.findTeachersByBranchIdAndSubjectId(branchId, subjectId, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="1.12-search-teacher-in-branch">

    /**
     * @param branchId
     * @param isAvailable
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @author LamHNT - 2021.10.12
     * @apiNote 1.12-search-teacher-in-branch
     */
    @CrossOrigin
    @RequestMapping(value = "/teachers", params = "branchId", method = RequestMethod.GET)
    public ResponseEntity<?> searchTeacherInBranch(@RequestParam(value = "branchId", defaultValue = "1") int branchId,
                                                   @RequestParam(value = "isAvailable", defaultValue = "true") boolean isAvailable,
                                                   @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                   @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return accountService.searchTeacherInBranch(branchId, isAvailable, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="1.13-search-student-in-branch">

    /**
     * @param branchId
     * @param isAvailable
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 1.13-search-student-in-branch
     * @author HuuNT - 2021.10.12
     */
    @CrossOrigin
    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public ResponseEntity<?> findStudentInABranch(@RequestParam(value = "branchId") int branchId,
                                                  @RequestParam(value = "isAvailable") boolean isAvailable,
                                                  @RequestParam(value = "pageNo") int pageNo,
                                                  @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return studentService.findStudentInABranch(branchId, isAvailable, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="1.14-delete-student">

    /**
     * @param username
     * @return
     * @throws Exception
     * @apiNote 1.14 Delete Student
     * @author HuuNT - 2021.10.19
     */
    @CrossOrigin
    @RequestMapping(value = "/students/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteStudent(@PathVariable String username) throws Exception {
        return studentService.deleteStudent(username);
    }
    //</editor-fold>

    //<editor-fold desc="1.15-delete-teacher">

    /**
     * @param username
     * @return
     * @throws Exception
     * @apiNote 1.15-delete-teacher
     * @author HuuNT - 2021.10.19
     */
    @CrossOrigin
    @RequestMapping(value = "/teachers/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTeacher(@PathVariable String username) throws Exception {
        return teacherService.deleteTeacher(username);
    }
    //</editor-fold>

    //<editor-fold desc="1.16-delete-staff">

    /**
     * @param username
     * @return
     * @throws Exception
     * @apiNote 1.16-delete-staff
     * @author HuuNT - 2021.10.21
     */
    @CrossOrigin
    @RequestMapping(value = "/staffs/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteStaffOrManager(@PathVariable String username) throws Exception {
        return staffService.deleteStaffOrManager(username);
    }
    //</editor-fold>

    //<editor-fold desc="1.17-search-student-by-name-and-phone">

    /**
     * @param branchId
     * @param isAvailable
     * @param phone
     * @param name
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @author HuuNT - 2021.10.25
     * @apiNote 1.17-search-student-by-name-phone
     */
    @CrossOrigin
    @RequestMapping(value = "/students", params = "branchId", method = RequestMethod.GET)
    public ResponseEntity<?> searchStudentInBranchByPhoneAndNameAndIsAvailable(@RequestParam(value = "branchId") int branchId,
                                                                               @RequestParam(value = "isAvailable") boolean isAvailable,
                                                                               @RequestParam(value = "phone") String phone,
                                                                               @RequestParam(value = "name") String name,
                                                                               @RequestParam(value = "pageNo") int pageNo,
                                                                               @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return studentService.searchStudentInBranchByPhoneAndNameAndIsAvailable(branchId, isAvailable, phone, name, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="1.18-search-teacher-by-branch-id-and-phone-and-name-and-is-available">

    /**
     * @param branchId
     * @param isAvailable
     * @param phone
     * @param name
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @author HuuNT - 2021.10.25
     * @apiNote 1.18-search-teacher-by-branch-id-and-phone-and-name-and-is-available
     */
    @CrossOrigin
    @RequestMapping(value = "/teachers-in-branch", params = "phone", method = RequestMethod.GET)
    public ResponseEntity<?> searchTeacherInBranchByPhoneAndNameAndIsAvailable(@RequestParam(value = "branchId") int branchId,
                                                                               @RequestParam(value = "isAvailable") boolean isAvailable,
                                                                               @RequestParam(value = "phone") String phone,
                                                                               @RequestParam(value = "name") String name,
                                                                               @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                                               @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return accountService.searchTeacherInBranchByPhoneAndNameAndIsAvailable(branchId, isAvailable, phone, name, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="1.19-get-available-teacher-for-opening-class">

    /**
     * @param branchId
     * @param shiftId
     * @param openingDate
     * @param subjectId
     * @return
     * @throws Exception
     * @apiNote 1.19-get-available-teacher-for-opening-class
     * @author HuuNT - 2021.10.28
     */
    @CrossOrigin
    @RequestMapping(value = "/teachers/{branchId}/search", method = RequestMethod.GET)
    public ResponseEntity<?> getAvailableTeachersOpeningClass(@PathVariable int branchId,
                                                              @RequestParam(value = "shiftId") int shiftId,
                                                              @RequestParam String openingDate,
                                                              @RequestParam(value = "subjectId") int subjectId) throws Exception {
        return teacherService.getAvailableTeachersForOpeningClass(branchId, shiftId, openingDate, subjectId);
    }
    //</editor-fold>

    //<editor-fold desc="1.20-get-account-by-role-and-branch-and-isAvailable">

    /**
     * @param branchId
     * @param role
     * @param isAvailable
     * @return
     * @throws Exception
     * @apiNote 1.20-get-account-by-role-and-branch-and-isAvailable
     * @author HuuNT - 2021.11.01
     */
    @CrossOrigin
    @RequestMapping(value = "/accounts-by-role", method = RequestMethod.GET)
    public ResponseEntity<?> searchInfoByUsername(@RequestParam(value = "branchId") int branchId,
                                                  @RequestParam(value = "role") String role,
                                                  @RequestParam(value = "isAvailable") boolean isAvailable) throws Exception {
        return accountService.getAccountByRoleAndIsAvalableInBranch(branchId, role, isAvailable);
    }
    //</editor-fold>

    //<editor-fold desc="1.21-forgot-password">

    /**
     * @param username
     * @return
     * @throws Exception
     * @apiNote 1.21-forgot-password
     * @author HuuNT - 2021.11.04
     */
    @CrossOrigin
    @RequestMapping(value = "/forgot-password", method = RequestMethod.PUT)
    public ResponseEntity<?> forgotPassword(@RequestParam(value = "username") String username) throws Exception {
        return accountService.forgotPassword(username);
    }
    //</editor-fold>

    /**
     * -------------------------------BRANCH--------------------------------
     */

    //<editor-fold desc="2.01-search-branch-by-branch-name">

    /**
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 2.01-search-branch-by-branch-name
     * @author HuuNT - 2021.06.09 | LamHNT - 2021.11.10
     */
    @CrossOrigin
    @RequestMapping(value = "/admin/branches", params = "name", method = RequestMethod.GET)
    public ResponseEntity<?> searchBranchByName(@RequestParam(value = "name") String keyword,
                                                @RequestParam(value = "isAvailable") boolean isAvailable,
                                                @RequestParam(value = "pageNo") int pageNo,
                                                @RequestParam(value = "pageSize") int pageSize) {
        return branchService.findByBranchNameContainingIgnoreCaseAndIsAvailableIsTrue(keyword, isAvailable, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="2.02-search-branch-by-branch-id">

    /**
     * @param branchId: int
     * @return branchdto
     * @throws Exception
     * @apiNote 2.02-search-branch-by-branch-id
     * @author HuuNT - 08.June.2021
     */
    @CrossOrigin
    @RequestMapping(value = "admin/branches/{branchId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Branch findByBranchId(@PathVariable int branchId) {
        return branchService.findBranchByBranchId(branchId);
    }
    //</editor-fold>

    //<editor-fold desc="2.03-delete-branch-by-id">

    /**
     * @param branchId
     * @return
     * @throws Exception
     * @apiNote 2.03-delete-branch-by-id
     * @author HuuNT- 2021.06.08
     */
    @CrossOrigin
    @RequestMapping(value = "/admin/branches/{branchId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBranchByBranchId(@PathVariable int branchId) throws Exception {
        return branchService.deleteByBranchId(branchId);
    }
    //</editor-fold>

    //<editor-fold desc="2.04-create-branch">

    /**
     * @param
     * @return true/false
     * @throws Exception
     * @apiNote 2.04-create-branch
     * @author HuuNT - 12.06.2021
     * @body new Branch
     */
    @CrossOrigin
    @RequestMapping(value = "/admin/branches", method = RequestMethod.POST)
    public ResponseEntity<?> createNewBranch(@RequestBody BranchRequestDto newBranch) throws Exception {
        return branchService.createNewBranch(newBranch);
    }
    //</editor-fold>

    //<editor-fold desc="2.05-edit-branch-by-branch-id">

    /**
     * @param branchId
     * @param insBranch
     * @return
     * @throws Exception
     * @apiNote 2.05-edit-branch-by-branch-id
     * @author LamHNT - 2021.06.15
     */
    @CrossOrigin
    @RequestMapping(value = "/admin/branches/{branchId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateBranchByBranchId(@PathVariable int branchId,
                                                    @RequestBody BranchRequestDto insBranch) throws Exception {
        return branchService.updateBranch(branchId, insBranch);
    }
    //</editor-fold>

    /**
     * -------------------------------CURRICULUM--------------------------------
     */

    //<editor-fold desc="3.01-search-curriculum-by-curriculum-name">

    /**
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 3.01-search-curriculum-by-curriculum-name
     * @author LamHNT - 2021.06.08
     */
    @CrossOrigin
    @RequestMapping(value = "/curriculums", params = "name", method = RequestMethod.GET)
    public ResponseEntity<?> searchCurriculumByName(@RequestParam(value = "name") String keyword,
                                                    @RequestParam(value = "isAvailable") boolean isAvailable,
                                                    @RequestParam(value = "pageNo") int pageNo,
                                                    @RequestParam(value = "pageSize") int pageSize) {
        return curriculumService.findByCurriculumNameContainingIgnoreCaseAndIsAvailable(keyword, isAvailable, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="3.02-search-curriculum-by-curriculum-code">

    /**
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 3.02-search-curriculum-by-curriculum-code
     * @author LamHNT - 2021.06.08
     */
    @CrossOrigin
    @RequestMapping(value = "/curriculums", params = "code", method = RequestMethod.GET)
    public ResponseEntity<?> searchCurriculumByCode(@RequestParam(value = "code") String keyword,
                                                    @RequestParam(value = "isAvailable") boolean isAvailable,
                                                    @RequestParam(value = "pageNo") int pageNo,
                                                    @RequestParam(value = "pageSize") int pageSize) {
        return curriculumService.findByCurriculumCodeContainingIgnoreCaseAndIsAvailable(keyword, isAvailable, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="3.03-get-curriculum-details-by-curriculum-id">

    /**
     * @param curriculumId
     * @return
     * @throws Exception
     * @apiNote 3.03-get-curriculum-details-by-curriculum-id
     * @author LamHNT - 2021.06.09
     */
    @CrossOrigin
    @RequestMapping(value = "/curriculums/{curriculumId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCurriculumDetails(@PathVariable int curriculumId) throws Exception {
        return curriculumService.findOneByCurriculumId(curriculumId);
    }
    //</editor-fold>

    //<editor-fold desc="3.04-delete-curriculum-by-curriculum-id">

    /**
     * @param curriculumId
     * @return
     * @throws Exception
     * @apiNote 3.04-delete-curriculum-by-curriculum-id
     * @author LamHNT - 2021.06.10
     */
    @CrossOrigin
    @RequestMapping(value = "/curriculums/{curriculumId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCurriculumByCurriculumId(@PathVariable int curriculumId) throws Exception {
        return curriculumService.deleteByCurriculumId(curriculumId);
    }
    //</editor-fold>

    //<editor-fold desc="3.05-create-curriculum">

    /**
     * @param newCur
     * @return
     * @throws Exception
     * @apiNote 3.05-create-curriculum
     * @author LamHNT - 2021.06.11
     */
    @CrossOrigin
    @RequestMapping(value = "/curriculums", method = RequestMethod.POST)
    public ResponseEntity<?> createNewCurriculum(@RequestBody CurriculumRequestDto newCur) throws Exception {
        return curriculumService.createNewCurriculum(newCur);
    }
    //</editor-fold>

    //<editor-fold desc="3.06-edit-curriculum-by-curriculum-id">

    /**
     * @param curriculumId
     * @param insCur
     * @return
     * @throws Exception
     * @apiNote 3.06-edit-curriculum-by-curriculum-id
     * @author LamHNT - 2021.06.12
     */
    @CrossOrigin
    @RequestMapping(value = "/curriculums/{curriculumId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCurriculumByCurriculumId(@PathVariable int curriculumId,
                                                            @RequestBody CurriculumRequestDto insCur) throws Exception {
        return curriculumService.updateCurriculum(curriculumId, insCur);
    }
    //</editor-fold>

    /**
     * -------------------------------SUBJECT--------------------------------
     */

    //<editor-fold desc="4.01-search-subject-by-subject-name">

    /**
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 4.01-search-subject-by-subject-name
     * @author HuuNT - 2021.06.21 / LamHNT - 2021.07.19
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects", params = "name", method = RequestMethod.GET)
    public ResponseEntity<?> searchSubjectByName(@RequestParam(value = "name") String keyword,
                                                 @RequestParam(value = "isAvailable") boolean isAvailable,
                                                 @RequestParam(value = "pageNo") int pageNo,
                                                 @RequestParam(value = "pageSize") int pageSize) {

        return subjectService.findBySubjectNameContainsAndIsAvailable(keyword, isAvailable, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="4.02-search-subject-by-subject-code">

    /**
     * @param keyword
     * @param isAvailable
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 4.02-search-subject-by-subject-code
     * @author HuuNT - 2021.06.21 / LamHNT - 2021.07.19
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects", params = "code", method = RequestMethod.GET)
    public ResponseEntity<?> searchSubjectByCode(@RequestParam(value = "code") String keyword,
                                                 @RequestParam(value = "isAvailable") boolean isAvailable,
                                                 @RequestParam(value = "pageNo") int pageNo,
                                                 @RequestParam(value = "pageSize") int pageSize) {

        return subjectService.findBySubjectCodeAndIsAvailable(keyword, isAvailable, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="4.03-search-subject-by-curriculum-id">

    /**
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 4.03-search-subject-by-curriculum-id
     * @author HuuNT - 2021.06.17
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects", params = "curriculumId", method = RequestMethod.GET)
    public ResponseEntity<?> searchSubjectByCurriculumId(@RequestParam(value = "curriculumId") int keyword,
                                                         @RequestParam(value = "isAvailable") boolean isAvailable,
                                                         @RequestParam(value = "pageNo") int pageNo,
                                                         @RequestParam(value = "pageSize") int pageSize) {
        return subjectService.findSubjectByCurriculumIdAndAndIsAvailable(keyword, isAvailable, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="4.04-search-subject-by-subject-id">

    /**
     * @param subjectId
     * @return
     * @throws Exception
     * @apiNote 4.04-search-subject-by-subject-id
     * @author HuuNT - 2021.06.22 / LamHNT - 2021.06.23
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects/{subjectId}", method = RequestMethod.GET)
    public ResponseEntity<?> searchSubjectAndCurriculumInfoBySubjectId(@PathVariable int subjectId) throws Exception {
        return subjectService.findSubjectAndCurriculumBySubjectId(subjectId);
    }
    //</editor-fold>

    //<editor-fold desc="4.05-delete-subject-included-subject-detail">

    /**
     * @param subjectId
     * @return
     * @throws Exception
     * @apiNote 4.05-delete-subject-included-subject-detail
     * @author HuuNT - 2021.06.22
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects/{subjectId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSubjectBySubjectId(@PathVariable int subjectId) throws Exception {
        return subjectService.deleteSubjectBySubjectId(subjectId);
    }
    //</editor-fold>

    //<editor-fold desc="4.06-create-subject">

    /**
     * @param
     * @param
     * @param
     * @return
     * @apiNote 4.06-create-subject
     * @author HuuNT - 2021.06.17 / LamHNT - 2021.06.23
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects", method = RequestMethod.POST)
    public ResponseEntity<?> createNewSubject(@RequestBody SubjectCreateRequestDto newSub) throws Exception {
        return subjectService.createNewSubject(newSub);
    }
    //</editor-fold>

    //<editor-fold desc="4.07-update-subject-by-subject-id">

    /**
     * @param subjectId
     * @param subjectUpdateRequestDto
     * @return
     * @throws Exception
     * @apiNote 4.07-update-subject-by-subject-id
     * @author HuuNT - 2021.06.22
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects/{subjectId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateSubjectDetail(@PathVariable int subjectId,
                                                 @RequestBody SubjectUpdateRequestDto subjectUpdateRequestDto) throws Exception {
        return subjectService.updateSubject(subjectId, subjectUpdateRequestDto);
    }
    //</editor-fold>

    //<editor-fold desc="4.08-get-subject-of-teacher">

    /**
     * @param teacherUsername
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 4.08-get-subject-of-teacher
     * @author HuuNT - 2021.11.02
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects", params = "teacherUsername", method = RequestMethod.GET)
    public ResponseEntity<?> searchSubjectOfTeacher(@RequestParam(value = "teacherUsername") String teacherUsername,
                                                    @RequestParam(value = "pageNo") int pageNo,
                                                    @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return subjectService.searchSubjectOfTeacher(teacherUsername, pageNo, pageSize);
    }
    //</editor-fold>

    /**
     * -------------------------------SUBJECT DETAIL--------------------------------
     */

    //<editor-fold desc="5.01-search-subject-detail-by-subject-id">

    /**
     * @param subjectId
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 5.01-search-subject-detail-by-subject-id
     * @author LamHNT - 2021.11.10
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects/details", method = RequestMethod.GET)
    public ResponseEntity<?> findSubjectDetailBySubjectId(@RequestParam(value = "subjectId") int subjectId,
                                                          @RequestParam(value = "isAvailable") boolean isAvailable,
                                                          @RequestParam(value = "pageNo") int pageNo,
                                                          @RequestParam(value = "pageSize") int pageSize) {
        return subjectDetailService.findSubjectDetailBySubjectId(subjectId, isAvailable, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="5.02-delete-subject-detail-by-subject-detail-id">

    /**
     * @param subjectDetailId
     * @return
     * @throws Exception
     * @apiNote 5.02-delete-subject-detail-by-subject-detail-id
     * @author LamHNT - 2021.06.21
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects/details/{subjectDetailId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSubjectDetailBySubjectDetailId(@PathVariable int subjectDetailId) throws Exception {
        return subjectDetailService.deleteSubjectDetailBySubjectDetailId(subjectDetailId);
    }
    //</editor-fold>

    //<editor-fold desc="5.03-create-new-subject-detail">

    /**
     * @param newSubjectDetail
     * @return
     * @throws Exception
     * @apiNote 5.03-create-new-subject-detail
     * @author LamHNT - 2021.06.20
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects/details", method = RequestMethod.POST)
    public ResponseEntity<?> createNewSubjectDetail(@RequestBody SubjectDetailRequestDto newSubjectDetail) throws Exception {
        return subjectDetailService.createNewSubjectDetail(newSubjectDetail);
    }
    //</editor-fold>

    //<editor-fold desc="5.04-update-subject-detail-by-subject-detail-id">

    /**
     * @param subjectDetailId
     * @param subjectDetailUpdateRequestDto
     * @return
     * @throws Exception
     * @apiNote 5.04-update-subject-detail-by-subject-detail-id
     * @author LamHNT - 2021.06.20
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects/details/{subjectDetailId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateSubjectDetail(@PathVariable int subjectDetailId,
                                                 @RequestBody Map<String, String> subjectDetailUpdateRequestDto) throws Exception {
        return subjectDetailService.updateSubjectDetail(subjectDetailId, subjectDetailUpdateRequestDto);
    }
    //</editor-fold>

    /**
     * -------------------------------TEACHING SUBJECT-------------------------------
     */

    //<editor-fold desc="6.02-search-teaching-subject-by-teacher-username-and-is-available">

    /**
     * @param teacherUsername
     * @return
     * @throws Exception
     * @apiNote 6.02-search-teaching-subject-by-teacher-username-and-is-available
     * @author LamHNT - 2021.10.25
     */
    @CrossOrigin
    @RequestMapping(value = "/teaching-subjects", params = "teacherUsername", method = RequestMethod.GET)
    public ResponseEntity<?> searchTeachingSubjectByTeacherUsernameAndIsAvailable(@RequestParam(value = "teacherUsername") String teacherUsername) throws Exception {
        return teachingSubjectService.searchTeachingSubjectByTeacherUsernameAndIsAvailable(teacherUsername);
    }
    //</editor-fold>

    //<editor-fold desc="6.03-delete-teaching-subject">

    /**
     * @param teacherUsername
     * @param subjectId
     * @return
     * @throws Exception
     * @author LamHNT - 2021.10.25
     * @apiNote 6.03-delete-teaching-subject
     */
    @CrossOrigin
    @RequestMapping(value = "/teaching-subjects/{teacherUsername}/{subjectId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTeachingSubject(@PathVariable(value = "teacherUsername") String teacherUsername,
                                                   @PathVariable(value = "subjectId") int subjectId) throws Exception {
        return teachingSubjectService.deleteTeachingSubject(teacherUsername, subjectId);
    }
    //</editor-fold>

    //<editor-fold desc="6.04-create-new-teaching-subject">

    /**
     * @param reqBody
     * @return
     * @throws Exception
     * @author LamHNT - 2021.10.25
     * @apiNote 6.04-create-new-teaching-subject
     */
    @CrossOrigin
    @RequestMapping(value = "/teaching-subjects", method = RequestMethod.POST)
    public ResponseEntity<?> createNewTeachingSubject(@RequestBody HashMap<String, Object> reqBody) throws Exception {
        return teachingSubjectService.createNewTeachingSubject(reqBody);
    }
    //</editor-fold>

    /**
     * -------------------------------GUEST-------------------------------
     */

    //<editor-fold desc="7.01-search-guest-by-branchid-and-name">

    /**
     * @param branchId
     * @param name
     * @param pageNo
     * @param pageSize
     * @return
     * @author HuuNT 2021.07.08 | LamHNT 2021.11.10
     * @apiNote 7.01-search-guest-by-branchid-and-name
     */
    @CrossOrigin
    @RequestMapping(value = "/guests", method = RequestMethod.GET)
    public ResponseEntity<?> findGuestByBranchIdAndName(@RequestParam(value = "branchId") int branchId,
                                                        @RequestParam(value = "name") String name,
                                                        @RequestParam(value = "phone") String phone,
                                                        @RequestParam(value = "curriculumName") String curriculumName,
                                                        @RequestParam(value = "pageNo") int pageNo,
                                                        @RequestParam(value = "pageSize") int pageSize) {
        return registeringGuestService.findRegisterGuestByBranchIdAndCustomerName(branchId, name, phone, curriculumName, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="7.05-search-guest-by-status">

    /**
     * @param branchId
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 7.05-search Guest by Status
     * @author HuuNT - 2021.07.09 | LamHNT 2021.11.10
     */
    @CrossOrigin
    @RequestMapping(value = "/guests", params = "status", method = RequestMethod.GET)
    public ResponseEntity<?> findGuestByBranchIdAndStatus(@RequestParam(value = "branchId") int branchId,
                                                          @RequestParam(value = "status") String status,
                                                          @RequestParam(value = "pageNo") int pageNo,
                                                          @RequestParam(value = "pageSize") int pageSize) {
        return registeringGuestService.findRegisterGuestByBranchIdAndStatus(branchId, status, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="7.07-register-guest">

    /**
     * @param insGuest
     * @return
     * @throws Exception
     * @apiNote 7.07-register-guest
     * @author LamHNT - 2021.07.05
     */
    @CrossOrigin
    @RequestMapping(value = "/guests", method = RequestMethod.POST)
    public ResponseEntity<?> createNewRegisteringGuest(@RequestBody RegisteringGuestRequestDto insGuest) throws Exception {
        return registeringGuestService.createNewRegisteringGuest(insGuest);
    }
    //</editor-fold>

    //<editor-fold desc="7.08-update-guest">

    /**
     * @param guestId
     * @param cusAtt
     * @return
     * @throws Exception
     * @apiNote 7.08-update-guest
     * @author LamHNT - 2021.07.08 / 2021.07.10
     */
    @CrossOrigin
    @RequestMapping(value = "/guests/{guestId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateGuest(@PathVariable int guestId, @RequestBody Map<String, String> cusAtt) throws Exception {
        cusAtt.get("status");
        cusAtt.get("description");
        return registeringGuestService.updateGuest(guestId, cusAtt);
    }
    //</editor-fold>

    /**
     * -------------------------------BOOKING-------------------------------
     */

    //<editor-fold desc="8.01-search-booking-by-class-id-and-status-in-a-branch">

    /**
     * @param classId
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 8.01 search booking by Class ID And Status in A Branch
     * @author HuuNT - 2021.19.09
     */
    @CrossOrigin
    @RequestMapping(value = "/bookings", params = "classId", method = RequestMethod.GET)
    public ResponseEntity<?> findBookingByClassIdAndPhoneAndStatus(@RequestParam(value = "classId") int classId,
                                                                   @RequestParam(value = "status") String status,
                                                                   @RequestParam(value = "pageNo") int pageNo,
                                                                   @RequestParam(value = "pageSize") int pageSize) {
        return bookingService.findBookingByClassIdAndPhoneAndStatus(classId, status, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="8.02-search-booking-by-student-id">

    /**
     * @param studentUsername
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 8.02-search-booking-by-studentUserName
     * @author HuuNT - 2021.07.09/2021.19.09
     */
    @CrossOrigin
    @RequestMapping(value = "/bookings", method = RequestMethod.GET)
    public ResponseEntity<?> findBookingByStudentId(@RequestParam(value = "studentUsername") String studentUsername,
                                                    @RequestParam(value = "pageNo") int pageNo,
                                                    @RequestParam(value = "pageSize") int pageSize) {
        return bookingService.findBookingByStudentUsername(studentUsername, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="8.03-get-booking-detail-by-id">

    /**
     * @param bookingId
     * @return
     * @throws Exception
     * @apiNote 8.03-get-booking-detail-by-id
     * @author HuuNT - 2021.07.10/2021.19.09
     */
    @CrossOrigin
    @RequestMapping(value = "/bookings", params = "bookingId", method = RequestMethod.GET)
    public BookingSearchResponseDto findBookingByBookingId(@RequestParam(value = "bookingId") int bookingId) throws Exception {
        return bookingService.findBookingByBookingId(bookingId);
    }
    //</editor-fold>

    //<editor-fold desc="8.05-create-new-booking">

    /**
     * @param bookingRequestDto
     * @return
     * @throws Exception
     * @apiNote 8.05-create-new-booking
     * @author LamHNT - 2021.07.08
     */
    @CrossOrigin
    @RequestMapping(value = "/bookings", method = RequestMethod.POST)
    public ResponseEntity<?> createNewBooking(@RequestBody BookingRequestDto bookingRequestDto) throws Exception {
        return bookingService.createNewBooking(bookingRequestDto);
    }
    //</editor-fold>

    //<editor-fold desc="8.06-update-booking">

    /**
     * @param bookingId
     * @param bookingAtt
     * @return
     * @throws Exception
     * @apiNote 8.06-update-booking
     * @author HuuNT - 2021.10.19
     */
    @CrossOrigin
    @RequestMapping(value = "/bookings", method = RequestMethod.PUT)
    public ResponseEntity<?> updateBooking(@RequestParam int bookingId,
                                           @RequestBody Map<String, String> bookingAtt) throws Exception {
        return bookingService.updateBooking(bookingId, bookingAtt);
    }
    //</editor-fold>

    /**
     * -------------------------------CLASS-------------------------------
     */

    //<editor-fold desc="9.01-search-class-by-subject-id-shift-id-status-paging">

    /**
     * @param branchId
     * @param subjectId
     * @param shiftId
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 9.01-search-class-by-subject-id-shift-id-status-paging
     * @author LamHNT - 2021.07.07 / HuuNT - 2021.09.15
     */
    @CrossOrigin
    @RequestMapping(value = "/classes/{branchId}/filter", params = "subjectId", method = RequestMethod.GET)
    public ResponseEntity<?> searchAllClassByBranchIdAndSubjectIdAndShiftIdAndStatusPaging(@PathVariable(value = "branchId") int branchId,
                                                                                           @RequestParam(value = "subjectId", required = false) int subjectId,
                                                                                           @RequestParam(value = "shiftId", required = false) int shiftId,
                                                                                           @RequestParam(value = "status") String status,
                                                                                           @RequestParam(value = "pageNo") int pageNo,
                                                                                           @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return classService.searchAllClassByBranchIdAndSubjectIdAndShiftIdAndStatusPaging(branchId, subjectId, shiftId, status, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="9.02-get-all-class-by-branchId-status">

    /**
     * @param branchId
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 9.02-get-all-class-by-branchId-status
     * @author HuuNT - 2021.07.07
     */
    @CrossOrigin
    @RequestMapping(value = "/classes", params = "branchId", method = RequestMethod.GET)
    public ResponseEntity<?> searchAllClassByBranchIdAndStatusPaging(@RequestParam(value = "branchId") int branchId,
                                                                     @RequestParam(value = "status") String status,
                                                                     @RequestParam(value = "pageNo") int pageNo,
                                                                     @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return classService.searchAllClassByBranchIdAndStatusPaging(branchId, status, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="9.03-search-class-of-student-and-teacher">

    /**
     * @param username
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 9.03-search-class-by-username-status
     * @author HuuNT - 2021.09.18
     */
    @CrossOrigin
    @RequestMapping(value = "/student-class/{username}", params = "status", method = RequestMethod.GET)
    public ResponseEntity<?> searchClassByUsernameAndStatusPaging(@PathVariable(value = "username") String username,
                                                                  @RequestParam(value = "status") String status,
                                                                  @RequestParam(value = "pageNo") int pageNo,
                                                                  @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return classService.searchClassByUsernameAndStatusPaging(username, status, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="9.04-search-class-detail-by-class-id">

    /**
     * @param classId
     * @return
     * @throws Exception
     * @apiNote 9.04-search-class-detail-by-class-id
     * @author LamHNT - 2021.11.11
     */
    @CrossOrigin
    @RequestMapping(value = "/classes/{classId}", method = RequestMethod.GET)
    public ResponseEntity<?> searchClassDetailByClassId(@PathVariable(value = "classId") Integer classId) throws Exception {
        return classService.searchClassDetailByClassId(classId);
    }
    //</editor-fold>

    //<editor-fold desc="9.05-search-class-of-teacher-by-username">

    /**
     * @param username
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 9.05-search class of teacher by username
     * @author HuuNT - 2021.09.20
     */
    @CrossOrigin
    @RequestMapping(value = "/teacher-class/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> searchClassByTeacherUsernameAndStatusPaging(@PathVariable(value = "username") String username,
                                                                         @RequestParam(value = "status") String status,
                                                                         @RequestParam(value = "pageNo") int pageNo,
                                                                         @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return classService.searchClassByTeacherUsernameAndStatusPaging(username, status, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="9.06-create-new-class">

    /**
     * @param classRequestDto
     * @return
     * @throws Exception
     * @apiNote 9.06-create-new-class
     * @author LamHNT - 2021.07.05
     */
    @CrossOrigin
    @RequestMapping(value = "/classes", method = RequestMethod.POST)
    public ResponseEntity<?> createNewClass(@RequestBody ClassRequestDto classRequestDto) throws Exception {
        return classService.createNewClass(classRequestDto);
    }
    //</editor-fold>

    //<editor-fold desc="9.07-edit-class">

    /**
     * @param classId
     * @param reqBody
     * @return
     * @throws Exception
     * @apiNote 9.07-edit-class
     * @author LamHNT - 2021.08.31
     */
    @CrossOrigin
    @RequestMapping(value = "/classes/{classId}", method = RequestMethod.PUT)
    public ResponseEntity<?> editClass(@PathVariable int classId,
                                       @RequestBody Map<String, Object> reqBody) throws Exception {
        return classService.editClass(classId, reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="9.08-delete-class">

    /**
     * @param classId
     * @return
     * @throws Exception
     * @apiNote 9.08-delete-class
     * @author LamHNT - 2021.11.16
     */
    @CrossOrigin
    @RequestMapping(value = "/classes/{classId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> deleteClass(@PathVariable(value = "classId") Integer classId,
                                         @RequestBody HashMap<String, Object> reqBody) throws Exception {
        return classService.deleteClass(classId, reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="9.09-get-classes-statistic">

    /**
     * @param branchId
     * @return
     * @throws Exception
     * @apiNote 9.09-get-classes-statistic
     * @author LamHNT - 2021.07.10
     */
    @CrossOrigin
    @RequestMapping(value = "/classes-status", method = RequestMethod.GET)
    public ResponseEntity<?> getClassesStatistic(@RequestParam int branchId) throws Exception {
        return classService.getClassesStatistic(branchId);
    }
    //</editor-fold>

    //<editor-fold desc="9.10-activate-class">

    /**
     * @param reqBody
     * @return
     * @throws Exception
     * @apiNote 9.10-activate-class
     * @author LamHNT - 2021.07.14
     */
    @CrossOrigin
    @RequestMapping(value = "/activate-class", method = RequestMethod.POST)
    public ResponseEntity<?> activateClass(@RequestBody Map<String, Object> reqBody) throws Exception {
        return classService.activateClass(reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="9.11-get-all-classes-has-not-got-feedback-from-student-by-student-username">

    /**
     * @param studentUsername
     * @return
     * @throws Exception
     * @author LamHNT - 2021.09.24
     * @apiNote 9.11-get-all-classes-has-not-got-feedback-from-student-by-student-username
     */
    @CrossOrigin
    @RequestMapping(value = "/student-feedback-class/{studentUsername}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllClassesHasNotGotFeedbackFromStudentByStudentUsername(@PathVariable(value = "studentUsername") String studentUsername) throws Exception {
        return classService.getAllClassesHasNotGotFeedbackFromStudentByStudentUsername(studentUsername);
    }
    //</editor-fold>

    //<editor-fold desc="9.12-scan-all-classes-to-update-class-status-to-finished">

    /**
     * @throws Exception
     * @author LamHNT - 2021.10.21
     * @apiNote 9.12-scan-all-classes-to-update-class-status-to-finished
     */
    public void scanAndUpdateClasses() {
        schedulerService.scanAndUpdateClasses();
    }
    //</editor-fold>

    //<editor-fold desc="9.13-class-static-teacher">

    /**
     * @param teacherUsername
     * @return
     * @throws Exception
     * @author HuuNT - 2021.11.17
     * @apiNote 9.13-class-static-teacher
     */
    @CrossOrigin
    @RequestMapping(value = "/classes-status", params = "teacherUsername", method = RequestMethod.GET)
    public ResponseEntity<?> getTeacherClassesStatistic(@RequestParam(value = "teacherUsername") String teacherUsername) throws Exception {
        return classService.getTeacherClassesStatistic(teacherUsername);
    }
    //</editor-fold>

    //<editor-fold desc="9.14-suspend-class">

    /**
     * @param studentInClassId
     * @param reqBody
     * @return
     * @throws Exception
     * @apiNote 9.14-suspend-class
     * @author LamHNT - 2021.12.02
     */
    @CrossOrigin
    @RequestMapping(value = "/class-suspend", method = RequestMethod.PUT)
    public ResponseEntity<?> suspendClass(@RequestParam(value = "studentInClassId") Integer studentInClassId,
                                          @RequestBody HashMap<String, Object> reqBody) throws Exception {
        return classService.suspendClass(studentInClassId, reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="9.15-search-class-to-suspend">

    /**
     * @param status
     * @param price
     * @param branchId
     * @return
     * @throws Exception
     * @author HuuNT - 2021.12.01
     * @apiNote 9.15-search-class-to-suspend
     */
    @CrossOrigin
    @RequestMapping(value = "/class-suspend", method = RequestMethod.GET)
    public ResponseEntity<?> getClassToSuspend(@RequestParam(value = "status") String status,
                                               @RequestParam(value = "price") float price,
                                               @RequestParam(value = "branchId") int branchId) throws Exception {
        return classService.getClassToSuspend(status, price, branchId);
    }
    //</editor-fold>

    //<editor-fold desc="9.16-get-class-suspend-true-of-student">

    /**
     * @param studentUsername
     * @return
     * @throws Exception
     * @author HuuNT - 2021.12.01
     * @apiNote 9.16-get-class-suspend-true-of-student
     */
    @CrossOrigin
    @RequestMapping(value = "/class-suspend", params = "studentUsername", method = RequestMethod.GET)
    public ResponseEntity<?> getClassSuspendIsTrueOfStudent(@RequestParam(value = "studentUsername") String studentUsername) throws Exception {
        return classService.getClassSuspendIsTrueOfStudent(studentUsername);
    }
    //</editor-fold>

    /**
     * -------------------------------STUDENT IN CLASS--------------------------------
     */

    //<editor-fold desc="10.01-move-student-in-class-by-class-id">

    /**
     * @param classId
     * @param
     * @return
     * @throws Exception
     * @apiNote 10.01-move-student-in-class-by-class-id
     * @author HuuNT - 2021.08.29
     */
    @CrossOrigin
    @RequestMapping(value = "/move-student-in-class", method = RequestMethod.PUT)
    public ResponseEntity<?> moveStudentInClass(@RequestParam(value = "classId") int classId,
                                                @RequestBody List<Integer> bookingId) throws Exception {
        return studentInClassService.moveStudentToOpeningClass(classId, bookingId);
    }
    //</editor-fold>

    //<editor-fold desc="10.02-student-gives-feedback">

    /**
     * @param reqBody
     * @return
     * @throws Exception
     * @apiNote 10.02-student-gives-feedback
     * @author LamHNT - 2021.09.25
     */
    @CrossOrigin
    @RequestMapping(value = "/feedback", method = RequestMethod.PUT)
    public ResponseEntity<?> studentGivesFeedback(@RequestBody HashMap<String, Object> reqBody) throws Exception {
        return studentInClassService.studentGivesFeedback(reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="10.04-get-student-in-class-by-class-id">

    /**
     * @param classId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 10.04-get-student-in-class-by-class-id
     * @author HuuNT - 2021.07.19 | LamHNT - 2021.11.10
     */
    @CrossOrigin
    @RequestMapping(value = "/student-in-class", method = RequestMethod.GET)
    public ResponseEntity<?> getStudentInClass(@RequestParam(value = "classId") int classId,
                                               @RequestParam(value = "pageNo") int pageNo,
                                               @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return studentInClassService.findStudentInClassByClassId(classId, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="10.06-manager-view-feedback">

    /**
     * @param classId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 10.06-manager-view-feedback
     * @author LamHNT - 2021.09.25
     */
    @CrossOrigin
    @RequestMapping(value = "/feedback/{classId}/", params = "pageNo", method = RequestMethod.GET)
    public ResponseEntity<?> getFeedbackForManager(@PathVariable(value = "classId") int classId,
                                                   @RequestParam(value = "pageNo") int pageNo,
                                                   @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return studentInClassService.getFeedbackForManager(classId, pageNo, pageSize);
    }
    //</editor-fold>

    /**
     * -------------------------------SESSION--------------------------------
     */

    //<editor-fold desc="11.03-view-schedule">

    /**
     * @param date
     * @return
     * @throws Exception
     * @apiNote 11.03-view-schedule
     * @author LamHNT - 2021.08.19
     */
    @CrossOrigin
    @RequestMapping(value = "/schedules", method = RequestMethod.GET)
    public ResponseEntity<?> viewSchedule(@RequestParam(value = "date") String date,
                                          @RequestParam(value = "branchId") int branchId) throws Exception {
        return sessionService.viewSchedule(date, branchId);
    }
    //</editor-fold>

    //<editor-fold desc="11.04-view-session-of-class">

    /**
     * @param classId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 11.04 - View Session Of a Class
     * @author HuuNT - 2021.09.21
     */
    @CrossOrigin
    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public ResponseEntity<?> viewSessionOfaClass(@RequestParam int classId,
                                                 @RequestParam int pageNo,
                                                 @RequestParam int pageSize) throws Exception {
        return sessionService.viewSessionOfaClass(classId, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="11.06-search-student-schedule">

    /**
     * @param studentUsername
     * @param srchDate
     * @return
     * @throws Exception
     * @author LamHNT - 2021.10.19
     * @apiNote 11.06-search-student-schedule
     */
    @CrossOrigin
    @RequestMapping(value = "/schedules", params = "studentUsername", method = RequestMethod.GET)
    public ResponseEntity<?> searchStudentSchedule(@RequestParam(value = "studentUsername") String studentUsername,
                                                   @RequestParam(value = "srchDate")
                                                   @DateTimeFormat(pattern = Constant.DATE_PATTERN) Date srchDate) throws Exception {
        return sessionService.searchStudentSchedule(studentUsername, srchDate);
    }
    //</editor-fold>

    //<editor-fold desc="11.07-search-teacher-schedule">

    /**
     * @param teacherUsername
     * @param srchDate
     * @return
     * @throws Exception
     * @apiNote 11.07-search-teacher-schedule
     * @author LamHNT - 2021.10.21
     */
    @CrossOrigin
    @RequestMapping(value = "/schedules", params = "teacherUsername", method = RequestMethod.GET)
    public ResponseEntity<?> searchTeacherSchedule(@RequestParam(value = "teacherUsername") String teacherUsername,
                                                   @RequestParam(value = "srchDate")
                                                   @DateTimeFormat(pattern = Constant.DATE_PATTERN) Date srchDate) throws Exception {
        return sessionService.searchTeacherSchedule(teacherUsername, srchDate);
    }
    //</editor-fold>

    //<editor-fold desc="11.08-update-session-in-class">

    /**
     * @param reqBody
     * @return
     * @throws Exception
     * @author LamHNT - 2021.10.13
     * @apiNote 11.08-update-session-in-class
     */
    @CrossOrigin
    @RequestMapping(value = "/sessions", method = RequestMethod.PUT)
    public ResponseEntity<?> updateSessionInClass(@RequestBody HashMap<String, Object> reqBody) throws Exception {
        return sessionService.updateSessionInClass(reqBody);
    }
    //</editor-fold>

    /**
     * -------------------------------ATTENDANCE--------------------------------
     */

    //<editor-fold desc="12.01-view-student-attendance-in-a-class">

    /**
     * @param studentUsername
     * @param classId
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 12.01-view-student-attendance-in-a-class
     * @author LamHNT - 2021.09.21
     */
    @CrossOrigin
    @RequestMapping(value = "/attendance/{studentUsername}/", method = RequestMethod.GET)
    public ResponseEntity<?> viewStudentAttendanceInAClass(@PathVariable(value = "studentUsername") String studentUsername,
                                                           @RequestParam(value = "classId") int classId,
                                                           @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                           @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return attendanceService.viewStudentAttendanceInAClass(studentUsername, classId, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="12.02-view-all-student-attendance-in-a-session">

    /**
     * @param sessionId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 12.02-view-all-student-attendance-in-a-session
     * @author LamHNT - 2021.09.21
     */
    @CrossOrigin
    @RequestMapping(value = "/attendance/teacher/{sessionId}/", method = RequestMethod.GET)
    public ResponseEntity<?> viewAllStudentAttendanceInASession(@PathVariable(value = "sessionId") int sessionId,
                                                                @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                                @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return attendanceService.viewAllStudentAttendanceInASession(sessionId, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="12.03-update-attendance">

    /**
     * @param updateAttendanceList
     * @return
     * @throws Exception
     * @apiNote 12.03-update-attendance
     * @author LamHNT - 2021.09.21
     */
    @CrossOrigin
    @RequestMapping(value = "/attendance", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAttendance(@RequestBody List<UpdateAttendanceDto> updateAttendanceList) throws Exception {
        return attendanceService.updateAttendance(updateAttendanceList);
    }
    //</editor-fold>

    //<editor-fold desc="12.04-reopen-attendance-in-a-session">

    /**
     * @param reqBody
     * @return
     * @throws Exception
     * @author LamHNT - 2021.10.21
     * @apiNote 12.04-reopen-attendance-in-a-session
     */
    @CrossOrigin
    @RequestMapping(value = "/reopen-attendance", method = RequestMethod.PUT)
    public ResponseEntity<?> reopenAttendanceInASession(@RequestBody HashMap<String, Object> reqBody) throws Exception {
        return attendanceService.reopenAttendanceInASession(reqBody);
    }
    //</editor-fold>

    /**
     * -------------------------------SHIFT--------------------------------
     */

    //<editor-fold desc="13.01-search-shift-by-shift-id">

    /**
     * @param shiftId
     * @return
     * @throws Exception
     * @apiNote 13.01-search-shift-by-shift-id
     * @author HuuNT - 2021.06.26
     */
    @CrossOrigin
    @RequestMapping(value = "/shifts/{shiftId}", method = RequestMethod.GET)
    public ResponseEntity<?> searchShiftByShiftId(@PathVariable int shiftId) throws Exception {
        return shiftService.findShiftByShiftId(shiftId);
    }
    //</editor-fold>

    //<editor-fold desc="13.02-search-shift-by-dow-and-by-time-start-containing">

    /**
     * @param dayOfWeek
     * @param timeStart
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 13.02-search-shift-by-dow-and-by-time-start-containing
     * @author HuuNT - 2021.06.24 | LamHNT - 2021.06.26
     */
    @CrossOrigin
    @RequestMapping(value = "/shifts", params = "dayOfWeek", method = RequestMethod.GET)
    public ResponseEntity<?> searchShiftByDayOfWeekContainingOrTimeStartContaining(@RequestParam(value = "dayOfWeek") String dayOfWeek,
                                                                                   @RequestParam(value = "timeStart") String timeStart,
                                                                                   @RequestParam(value = "pageNo") int pageNo,
                                                                                   @RequestParam(value = "pageSize") int pageSize) {
        return shiftService.searchShiftByDayOfWeekContainingOrTimeStartContaining(dayOfWeek, timeStart, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="13.03-create-new-shift">

    /**
     * @param shiftRequestDto
     * @return
     * @throws Exception
     * @apiNote 13.03-create-new-shift
     * @author LamHNT - 2021.06.24
     */
    @CrossOrigin
    @RequestMapping(value = "/shifts", method = RequestMethod.POST)
    public ResponseEntity<?> createNewShift(@RequestBody ShiftRequestDto shiftRequestDto) throws Exception {
        return shiftService.createNewShift(shiftRequestDto);
    }

    //</editor-fold>

    //<editor-fold desc="13.04-delete-shift-by-id">

    /**
     * @param shiftId
     * @return
     * @throws Exception
     * @apiNote 13.04-Delete-Shift
     * @author HuuNT - 2021.06.26
     */
    @CrossOrigin
    @RequestMapping(value = "/shifts/{shiftId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteShiftByShiftId(@PathVariable int shiftId) throws Exception {
        return shiftService.deleteShiftByShiftId(shiftId);
    }
    //</editor-fold>

    //<editor-fold desc="13.05-get-all-shift-by-isAvailable">

    /**
     * @param isAvailable
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 13.05-get-all-shift-by-isAvailable
     * @author HuuNT 2021.06.26 | LamHNT 2021.11.10
     */
    @CrossOrigin
    @RequestMapping(value = "/shifts", method = RequestMethod.GET)
    public ResponseEntity<?> getAllShiftByIsAvailable(@RequestParam(value = "isAvailable") boolean isAvailable,
                                                      @RequestParam(value = "pageNo") int pageNo,
                                                      @RequestParam(value = "pageSize") int pageSize) {
        return shiftService.findAllShiftByIsAvailable(isAvailable, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="13.06-Revival Shift by Id">

    /**
     * @param shiftId
     * @return
     * @throws Exception
     * @apiNote 13.06-revival shift by id
     * @author HuuNT - 2021.06.26
     */
    @CrossOrigin
    @RequestMapping(value = "/shifts/{shiftId}", method = RequestMethod.PUT)
    public ResponseEntity<?> revivalShiftByShiftId(@PathVariable int shiftId) throws Exception {
        return shiftService.revivalShiftbyShiftId(shiftId);
    }
    //</editor-fold>

    /**
     * -------------------------------ROOM--------------------------------
     */

    //<editor-fold desc="14.01-get-available-rooms-for-opening-class">

    /**
     * @param branchId
     * @param shiftId
     * @param classId
     * @param openingDate
     * @return
     * @throws Exception
     * @author LamHNT - 2021.09.18
     * @apiNote 14.01-get-available-rooms-for-opening-class
     */
    @CrossOrigin
    @RequestMapping(value = "/rooms/{branchId}/search", method = RequestMethod.GET)
    public ResponseEntity<?> getAvailableRoomsForOpeningClass(@PathVariable int branchId,
                                                              @RequestParam(value = "shiftId") int shiftId,
                                                              @RequestParam(value = "classId") int classId,
                                                              @RequestParam String openingDate) throws Exception {
        return roomService.getAvailableRoomsForOpeningClass(branchId, shiftId, classId, openingDate);
    }
    //</editor-fold>

    //<editor-fold desc="14.02-get-all-room-in-branch">

    /**
     * @param branchId
     * @param isAvailable
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 14.02-get-all-room-in-branch
     * @author HuuNT - 2021.10.21
     */
    @CrossOrigin
    @RequestMapping(value = "/rooms/{branchId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAvailableRoomsInBranch(@PathVariable int branchId,
                                                       @RequestParam(value = "isAvailable") boolean isAvailable,
                                                       @RequestParam(value = "pageNo") int pageNo,
                                                       @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return roomService.getAvailableRoomsInBranch(branchId, isAvailable, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="14.03-update-room">

    /**
     * @param reqBody
     * @return
     * @throws Exception
     * @apiNote 14.03 update room
     * @author HuuNT - 2021.10.21
     */
    @CrossOrigin
    @RequestMapping(value = "/rooms", method = RequestMethod.PUT)
    public ResponseEntity<?> updateRoom(@RequestBody HashMap<String, Object> reqBody) throws Exception {
        return roomService.updateRoom(reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="14.04-create-new-room">

    /**
     * @param reqBody
     * @return
     * @throws Exception
     * @apiNote 14.04 - Create New Room
     * @author HuuNT - 2021.10.22
     */
    @CrossOrigin
    @RequestMapping(value = "/rooms", method = RequestMethod.POST)
    public ResponseEntity<?> createNewRoom(@RequestBody HashMap<String, String> reqBody) throws Exception {
        return roomService.createNewRoom(reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="14.05-delete-room">

    /**
     * @param roomId
     * @return
     * @throws Exception
     * @apiNote 14.05-delete-room
     * @author HuuNT - 2021.10.22
     */
    @CrossOrigin
    @RequestMapping(value = "/rooms", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRoom(@RequestParam(value = "roomId") int roomId) throws Exception {
        return roomService.deleteRoom(roomId);
    }
    //</editor-fold>

    /**
     * -------------------------------NOTIFICATION--------------------------------
     */

    //<editor-fold desc="15.01-create-notification-for-all-in-a-branch">

    /**
     * @param reqBody
     * @return
     * @throws Exception
     * @apiNote 15.01-create-notification-for-all-in-a-branch
     * @author LamHNT - 2021.09.28
     */
    @CrossOrigin
    @RequestMapping(value = "/notification-to-all", method = RequestMethod.POST)
    public ResponseEntity<?> createNotificationInBranch(@RequestBody HashMap<String, Object> reqBody) throws Exception {
        return notificationService.createNotificationInBranch(reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="15.02-create-notification-for-student-and-teacher-in-a-class">

    /**
     * @param reqBody
     * @return
     * @throws Exception
     * @apiNote 15.02-create-notification-for-student-and-teacher-in-a-class
     * @author LamHNT - 2021.09.27
     */
    @CrossOrigin
    @RequestMapping(value = "/notification-in-class", method = RequestMethod.POST)
    public ResponseEntity<?> createNotificationInClass(@RequestBody HashMap<String, Object> reqBody) throws Exception {
        return notificationService.createNotificationInClass(reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="15.03-create-notification-for-person">

    /**
     * @param reqBody
     * @return
     * @throws Exception
     * @apiNote 15.03-create-notification-for-person
     * @author HuuNT - 2021.09.30
     */
    @CrossOrigin
    @RequestMapping(value = "/notification-to-person", method = RequestMethod.POST)
    public ResponseEntity<?> createNotificationForPerson(@RequestBody HashMap<String, Object> reqBody) throws Exception {
        return notificationService.createNotificationForPerson(reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="15.04-create-notification-for-staff-and-manager-in-a-branch">

    /**
     * @param reqBody
     * @return
     * @throws Exception
     * @apiNote 15.04-create-notification-for-staff-and-manager-in-a-branch
     * @author LamHNT - 2021.09.29
     */
    @CrossOrigin
    @RequestMapping(value = "/notification-to-staff", method = RequestMethod.POST)
    public ResponseEntity<?> createNotificationToStaff(@RequestBody HashMap<String, Object> reqBody) throws Exception {
        return notificationService.createNotificationToStaff(reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="15.05-get-all-notification">

    /**
     * @param userName
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 15.05-get-all-notification
     * @author HuuNT - 2021.09.29
     */
    @CrossOrigin
    @RequestMapping(value = "/notification/{userName}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllNotificationByIsRead(@PathVariable(value = "userName") String userName,
                                                        @RequestParam int pageNo,
                                                        @RequestParam int pageSize) throws Exception {
        return notificationService.getAllNotificationByUserName(userName, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="15.06-update-notification">

    /**
     * @param notificationId
     * @param reqBody
     * @return
     * @apiNote 15.06-update-notification
     * @author LamHNT - 2021.09.28
     */
    @CrossOrigin
    @RequestMapping(value = "/notification/{notificationId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateNotification(@PathVariable(value = "notificationId") int notificationId,
                                                @RequestBody HashMap<String, Object> reqBody) throws Exception {
        return notificationService.updateNotification(notificationId, reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="15.07-send-noti-and-email-to-group-person">

    /**
     * @param notiAndEmailToGroupRequestDto
     * @return
     * @throws Exception
     * @apiNote 15.07-send-noti-and-email-to-group-person
     * @author HuuNT - 2021.11.04
     */
    @CrossOrigin
    @RequestMapping(value = "/notification-to-group", method = RequestMethod.POST)
    public ResponseEntity<?> createNotificationAndSendEmailToGroup(@RequestBody NotiAndEmailToGroupRequestDto notiAndEmailToGroupRequestDto) throws Exception {
        return notificationService.createNotificationAndSendEmailToGroup(notiAndEmailToGroupRequestDto);
    }
    //</editor-fold>

    /**
     * -------------------------------STATISTIC--------------------------------
     **/

    //<editor-fold desc="16.07-get-manager-statistic-in-month">

    /**
     * @param date
     * @param branchId
     * @return
     * @throws Exception
     * @apiNote 16.07-get-manager-statistic
     * @author HuuNT - 2021.11.19
     */
    @CrossOrigin
    @RequestMapping(value = "/manager-statistic", method = RequestMethod.GET)
    public ResponseEntity<?> getManagerStatistic(@RequestParam(value = "date")
                                                 @DateTimeFormat(pattern = Constant.DATE_PATTERN) Date date,
                                                 @RequestParam(value = "branchId") int branchId) throws Exception {
        return statisticService.getManagerStatistic(date, branchId);
    }
    //</editor-fold>

    //<editor-fold desc="16.08-get-admin-statistic-in-month">

    /**
     * @param date
     * @return
     * @throws Exception
     * @author HuuNT - 2021.11.19
     * @apiNote 16.08-get-admin-statistic-in-month
     */
    @CrossOrigin
    @RequestMapping(value = "/admin-statistic", method = RequestMethod.GET)
    public ResponseEntity<?> getAdminStatistic(@RequestParam(value = "date")
                                               @DateTimeFormat(pattern = Constant.DATE_PATTERN) Date date) throws Exception {
        return statisticService.getAdminStatistic(date);
    }
    //</editor-fold>

    /**
     * -------------------------------FIREBASE--------------------------------
     **/

    //<editor-fold desc="Upload Image via Firebase">
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/image", method = RequestMethod.POST)

    public ImageResponseDTO upload(@RequestParam(value = "id") String id,
                                   @RequestBody ImageRequestDto base64) throws IOException, FirebaseAuthException {
        return fireBaseService.upload(base64, id);
    }
    //</editor-fold>
}