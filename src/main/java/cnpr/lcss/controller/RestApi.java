package cnpr.lcss.controller;

import cnpr.lcss.dao.Branch;
import cnpr.lcss.model.*;
import cnpr.lcss.service.*;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class RestApi {
    @Autowired
    AccountService accountService;
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

    //<editor-fold desc="Welcome Page">

    /**
     * @return
     * @apiNote welcome page
     */
    @CrossOrigin
    @RequestMapping(value = "/")

    public String welcome() {
        return "Welcome to LCSS - Language Center Support System!";
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
     * @param insAcc
     * @return
     * @throws Exception
     * @apiNote 1.06-update-account
     * @author LamHNT - 2021.06.27
     */
    @CrossOrigin
    @RequestMapping(value = "/accounts", params = "username", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAccount(@RequestParam String username,
                                           @RequestBody AccountRequestDto insAcc) throws Exception {
        return accountService.updateAccount(username, insAcc);
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
     * @author HuuNT - 2021.06.09
     */
    @CrossOrigin
    @RequestMapping(value = "/admin/branches", params = "name", method = RequestMethod.GET)
    public BranchPagingResponseDto searchBranchByName(@RequestParam(value = "name") String keyword,
                                                      @RequestParam(value = "isAvailable") boolean isAvailable,
                                                      @RequestParam(value = "pageNo") int pageNo,
                                                      @RequestParam(value = "pageSize") int pageSize) {
        // pageNo starts at 0
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
    public CurriculumPagingResponseDto searchCurriculumByName(@RequestParam(value = "name") String keyword,
                                                              @RequestParam(value = "isAvailable") boolean isAvailable,
                                                              @RequestParam(value = "pageNo") int pageNo,
                                                              @RequestParam(value = "pageSize") int pageSize) {
        // pageNo starts at 0
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
    public CurriculumPagingResponseDto searchCurriculumByCode(@RequestParam(value = "code") String keyword,
                                                              @RequestParam(value = "isAvailable") boolean isAvailable,
                                                              @RequestParam(value = "pageNo") int pageNo,
                                                              @RequestParam(value = "pageSize") int pageSize) {
        // pageNo starts at 0
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
    public SubjectPagingResponseDto searchSubjectByCode(@RequestParam(value = "code") String keyword,
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
    public SubjectPagingResponseDto searchSubjectByCurriculumId(@RequestParam(value = "curriculumId") int keyword,
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
     * @author LamHNT - 2021.06.18
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects/details", method = RequestMethod.GET)
    public SubjectDetailPagingResponseDto findSubjectDetailBySubjectId(@RequestParam(value = "subjectId") int subjectId,
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
                                                 @RequestBody SubjectDetailUpdateRequestDto subjectDetailUpdateRequestDto) throws Exception {
        return subjectDetailService.updateSubjectDetail(subjectDetailId, subjectDetailUpdateRequestDto);
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
     * @author HuuNT 2021-07-08
     * @apiNote 7.01-search-guest-by-branchid-and-name
     */
    @CrossOrigin
    @RequestMapping(value = "/guests", method = RequestMethod.GET)
    public RegisteringGuestSearchPagingResponseDto findGuestByBranchIdAndName(@RequestParam(value = "branchId") int branchId,
                                                                              @RequestParam(value = "name") String name,
                                                                              @RequestParam(value = "phone") String phone,
                                                                              @RequestParam(value = "curriculumName") String curriculumName,
                                                                              @RequestParam(value = "pageNo") int pageNo,
                                                                              @RequestParam(value = "pageSize") int pageSize) {
        return registeringGuestService.findRegisterGuestByBranchIdAndCustomerName(branchId, name, phone, curriculumName, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="7.05-search guest by status">

    /**
     * @param branchId
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 7.05-search Guest by Status
     * @author HuuNT - 2021.07-09
     */
    @CrossOrigin
    @RequestMapping(value = "/guests", params = "status", method = RequestMethod.GET)
    public RegisteringGuestSearchPagingResponseDto findGuestByBranchIdAndStatus(@RequestParam(value = "branchId") int branchId,
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

    //<editor-fold desc="8.02-search-booking-by-student-id">

    /**
     * @param studentUsername
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 8.02-search-booking-by-studentid
     * @author HuuNT - 2021.07.09
     */
    @CrossOrigin
    @RequestMapping(value = "/bookings", method = RequestMethod.GET)
    public BookingSearchResponsePagingDto findBookingByStudentId(@RequestParam(value = "studentUsername") String studentUsername,
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
     * @author HuuNT - 2021.07.10
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
     *
     * @param username
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 9.03-search-class-by-username-status
     * @author HuuNT - 2021.18.09
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

    //<editor-fold desc="10.04-get-student-in-class-by-class-id">

    /**
     * @param classId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 10.04-get-student-in-class-by-class-id
     * @author HuuNT - 2021.07.19
     */
    @CrossOrigin
    @RequestMapping(value = "/student-in-class", method = RequestMethod.GET)
    public StudentInClassSearchPagingResponseDto getStudentInClass(@RequestParam(value = "classId") int classId,
                                                                   @RequestParam(value = "pageNo") int pageNo,
                                                                   @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return studentInClassService.findStudentInClassByClassId(classId, pageNo, pageSize);
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
    public ResponseEntity<?> viewSchedule(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws Exception {
        return sessionService.viewSchedule(date);
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
     * @author HuuNT - 2021.06.24 / LamHNT - 2021.06.26
     */
    @CrossOrigin
    @RequestMapping(value = "/shifts", params = "dayOfWeek", method = RequestMethod.GET)
    public ShiftPagingResponseDto searchShiftByDayOfWeekContainingOrTimeStartContaining(@RequestParam(value = "dayOfWeek") String dayOfWeek,
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
     * @author HuuNT 2021-06-26
     */
    @CrossOrigin
    @RequestMapping(value = "/shifts", method = RequestMethod.GET)
    public ShiftPagingResponseDto getAllShiftByIsAvailable(@RequestParam(value = "isAvailable") boolean isAvailable,
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
                                                              @RequestParam String openingDate) throws Exception {
        return roomService.getAvailableRoomsForOpeningClass(branchId, shiftId, openingDate);
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