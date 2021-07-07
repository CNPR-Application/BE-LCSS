package cnpr.lcss.controller;

import cnpr.lcss.dao.Branch;
import cnpr.lcss.model.*;
import cnpr.lcss.service.*;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class RestApi {

    @Autowired
    AccountService accountService;
    @Autowired
    CurriculumService curriculumService;
    @Autowired
    BranchService branchService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    SubjectDetailService subjectDetailService;
    @Autowired
    ShiftService shiftService;
    @Autowired
    FirebaseService fireBaseService;
    @Autowired
    ClassService classService;
    @Autowired
    RegisteringGuestService registeringGuestService;

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

    //<editor-fold desc="1.0-check-login">

    /**
     * @param loginRequestDto
     * @return
     * @throws Exception
     * @apiNote 1.0-check-login
     * @author LamHNT - 2021.06.03
     */
    @CrossOrigin
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> checkLogin(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
        return accountService.checkLogin(loginRequestDto);
    }
    //</editor-fold>

    //<editor-fold desc="2.0-search-account-like-username-paging">
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

    //<editor-fold desc="3.1-search-info-by-username">

    /**
     * @param username
     * @return
     * @apiNote 3.1-search-info-by-username
     * @author LamHNT - 2021.06.26
     */
    @CrossOrigin
    @RequestMapping(value = "/accounts/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> searchInfoByUsername(@PathVariable String username) throws Exception {
        return accountService.searchInfoByUsername(username);
    }
    //</editor-fold>

    //<editor-fold desc="4.0-create-new-account">

    /**
     * @param newAccount
     * @return
     * @throws Exception
     * @apiNote 4.0 Create New Account
     * @author LamHNT - 2021.06.30
     */
    @CrossOrigin
    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity<?> createNewAccount(@RequestBody NewAccountRequestDto newAccount) throws Exception {
        return accountService.createNewAccount(newAccount);
    }
    //</editor-fold>

    //<editor-fold desc="5.0-update-account">

    /**
     * @param username
     * @param insAcc
     * @return
     * @throws Exception
     * @apiNote 5.0-update-account
     * @author LamHNT - 2021.06.27
     */
    @CrossOrigin
    @RequestMapping(value = "/accounts", params = "username", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAccount(@RequestParam String username,
                                           @RequestBody AccountRequestDto insAcc) throws Exception {
        return accountService.updateAccount(username, insAcc);
    }
    //</editor-fold>

    //<editor-fold desc="5.1-update-role">

    /**
     * @param username
     * @param role
     * @return
     * @throws Exception
     * @apiNote 5.1-update-role
     * @author LamHNT - 2021.07.01
     */
    @CrossOrigin
    @RequestMapping(value = "/admin/role", method = RequestMethod.PUT)
    public ResponseEntity<?> updateRoleByUsername(@RequestParam(value = "username") String username,
                                                  @RequestBody String role) throws Exception {
        return accountService.updateRole(username, role);
    }
    //</editor-fold>

    //<editor-fold desc="6.0 Delete Account">

    /**
     * @param keyword
     * @return
     * @throws Exception
     * @apiNote 6.0 - Delete Account by User Name
     * @author HuuNT - 2021.06.30
     */
    @CrossOrigin
    @RequestMapping(value = "/accounts", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAccountByUserName(@RequestParam(value = "username") String keyword) throws Exception {
        return accountService.deleteByUserName(keyword);
    }
    //</editor-fold>

    /**
     * -------------------------------BRANCH--------------------------------
     */

    //<editor-fold desc="8.0-search-branch-by-branch-name">

    /**
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 8.0-search-branch-by-branch-name
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

    //<editor-fold desc="9.0 - Search Branch by Branch ID">

    /**
     * @param branchId: int
     * @return branchdto
     * @throws Exception
     * @apiNote 9.0 - Search Branch by Branch ID
     * @author HuuNT - 08.June.2021
     */
    @CrossOrigin
    @RequestMapping(value = "admin/branches/{branchId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Branch findByBranchId(@PathVariable int branchId) {
        return branchService.findBranchByBranchId(branchId);
    }
    //</editor-fold>

    //<editor-fold desc="10.0-delete-branch-by-id">

    /**
     * @param branchId
     * @return
     * @throws Exception
     * @apiNote 10.0-delete-branch-by-id
     * @author HuuNT- 2021.06.08
     */
    @CrossOrigin
    @RequestMapping(value = "/admin/branches/{branchId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBranchByBranchId(@PathVariable int branchId) throws Exception {
        return branchService.deleteByBranchId(branchId);
    }
    //</editor-fold>

    //<editor-fold desc="11.0 - Create new branch">

    /**
     * @param
     * @return true/false
     * @throws Exception
     * @apiNote 11.0 - Create new branch
     * @author HuuNT - 12.06.2021
     * @body new Branch
     */
    @CrossOrigin
    @RequestMapping(value = "/admin/branches", method = RequestMethod.POST)
    public ResponseEntity<?> createNewBranch(@RequestBody BranchRequestDto newBranch) throws Exception {
        return branchService.createNewBranch(newBranch);
    }
    //</editor-fold>

    //<editor-fold desc="12.0 - Update Branch by Branch Id">

    /**
     * @param branchId
     * @param insBranch
     * @return
     * @throws Exception
     * @apiNote 12.0 - Update Branch by Branch Id
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

    //<editor-fold desc="13.0-search-curriculum-by-curriculum-name">

    /**
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 13.0-search-curriculum-by-curriculum-name
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

    //<editor-fold desc="14.0-search-curriculum-by-curriculum-code">

    /**
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 14.0-search-curriculum-by-curriculum-code
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

    //<editor-fold desc="15.0-get-curriculum-details-by-curriculum-id">

    /**
     * @param curriculumId
     * @return
     * @throws Exception
     * @apiNote 15.0-get-curriculum-details-by-curriculum-id
     * @author LamHNT - 2021.06.09
     */
    @CrossOrigin
    @RequestMapping(value = "/curriculums/{curriculumId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCurriculumDetails(@PathVariable int curriculumId) throws Exception {
        return curriculumService.findOneByCurriculumId(curriculumId);
    }
    //</editor-fold>

    //<editor-fold desc="16.0-delete-curriculum-by-curriculum-id">

    /**
     * @param curriculumId
     * @return
     * @throws Exception
     * @apiNote 16.0-delete-curriculum-by-curriculum-id
     * @author LamHNT - 2021.06.10
     */
    @CrossOrigin
    @RequestMapping(value = "/curriculums/{curriculumId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCurriculumByCurriculumId(@PathVariable int curriculumId) throws Exception {
        return curriculumService.deleteByCurriculumId(curriculumId);
    }
    //</editor-fold>

    //<editor-fold desc="17.0-create-curriculum">

    /**
     * @param newCur
     * @return
     * @throws Exception
     * @apiNote 17.0-create-curriculum
     * @author LamHNT - 2021.06.11
     */
    @CrossOrigin
    @RequestMapping(value = "/curriculums", method = RequestMethod.POST)
    public ResponseEntity<?> createNewCurriculum(@RequestBody CurriculumRequestDto newCur) throws Exception {
        return curriculumService.createNewCurriculum(newCur);
    }
    //</editor-fold>

    //<editor-fold desc="18.0-edit-curriculum-by-curriculum-id">

    /**
     * @param curriculumId
     * @param insCur
     * @return
     * @throws Exception
     * @apiNote 18.0-edit-curriculum-by-curriculum-id
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

    //<editor-fold desc="19.0-search-subject-by-subject-name">

    /**
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 19.0-search-subject-by-subject-name
     * @author HuuNT - 2021.06.21
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

    //<editor-fold desc="20.0-search-subject-by-subject-code">

    /**
     * @param keyword
     * @param isAvailable
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 20.0-search-subject-by-subject-code
     * @author HuuNT - 2021.06.21
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

    //<editor-fold desc="21.0-search-subject-by-curriculum-id">

    /**
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 21.0-search-subject-by-curriculum-id
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

    //<editor-fold desc="22.0-search-subject-by-subject-id">

    /**
     * @param subjectId
     * @return
     * @throws Exception
     * @apiNote 22.0-search-subject-by-subject-id
     * @author HuuNT - 2021.06.22 / LamHNT - 2021.06.23
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects/{subjectId}", method = RequestMethod.GET)
    public ResponseEntity<?> searchSubjectAndCurriculumInfoBySubjectId(@PathVariable int subjectId) throws Exception {
        return subjectService.findSubjectAndCurriculumBySubjectId(subjectId);
    }
    //</editor-fold>

    //<editor-fold desc="23.0-delete-subject-included-subject-detail">

    /**
     * @param subjectId
     * @return
     * @throws Exception
     * @apiNote 23.0-delete-subject-included-subject-detail
     * @author HuuNT - 2021.06.22
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects/{subjectId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSubjectBySubjectId(@PathVariable int subjectId) throws Exception {
        return subjectService.deleteSubjectBySubjectId(subjectId);
    }
    //</editor-fold>

    //<editor-fold desc="24.0-create-subject">

    /**
     * @param
     * @param
     * @param
     * @return
     * @apiNote 24.0-create-subject
     * @author HuuNT - 2021.06.17 / LamHNT - 2021.06.023
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects", method = RequestMethod.POST)
    public ResponseEntity<?> createNewSubject(@RequestBody SubjectCreateRequestDto newSub) throws Exception {
        return subjectService.createNewSubject(newSub);
    }
    //</editor-fold>

    //<editor-fold desc="25.0-update-subject-by-subject-id">

    /**
     * @param subjectId
     * @param subjectUpdateRequestDto
     * @return
     * @throws Exception
     * @apiNote 25.0-update-subject-by-subject-id
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

    //<editor-fold desc="26.0-search-subject-detail-by-subject-id">

    /**
     * @param subjectId
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 26.0-search-subject-detail-by-subject-id
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

    //<editor-fold desc="27.0-delete-subject-detail-by-subject-detail-id">

    /**
     * @param subjectDetailId
     * @return
     * @throws Exception
     * @apiNote 27.0-delete-subject-detail-by-subject-detail-id
     * @author LamHNT - 2021.06.21
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects/details/{subjectDetailId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSubjectDetailBySubjectDetailId(@PathVariable int subjectDetailId) throws Exception {
        return subjectDetailService.deleteSubjectDetailBySubjectDetailId(subjectDetailId);
    }
    //</editor-fold>

    //<editor-fold desc="28.0-create-new-subject-detail">

    /**
     * @param newSubjectDetail
     * @return
     * @throws Exception
     * @apiNote 28.0-create-new-subject-detail
     * @author LamHNT - 2021.06.20
     */
    @CrossOrigin
    @RequestMapping(value = "/subjects/details", method = RequestMethod.POST)
    public ResponseEntity<?> createNewSubjectDetail(@RequestBody SubjectDetailRequestDto newSubjectDetail) throws Exception {
        return subjectDetailService.createNewSubjectDetail(newSubjectDetail);
    }
    //</editor-fold>

    //<editor-fold desc="29.0-update-subject-detail-by-subject-detail-id">

    /**
     * @param subjectDetailId
     * @param subjectDetailUpdateRequestDto
     * @return
     * @throws Exception
     * @apiNote 29.0-update-subject-detail-by-subject-detail-id
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
     * -------------------------------REGISTERING GUEST-------------------------------
     */

    //<editor-fold desc="41-register-guest">

    /**
     * @param insGuest
     * @return
     * @throws Exception
     * @apiNote 41-register-guest
     * @author LamHNT - 2021.07.05
     */
    @CrossOrigin
    @RequestMapping(value = "/guests", method = RequestMethod.POST)
    public ResponseEntity<?> createNewRegisteringGuest(@RequestBody RegisteringGuestRequestDto insGuest) throws Exception {
        return registeringGuestService.createNewRegisteringGuest(insGuest);
    }
    //</editor-fold>

    /**
     * -------------------------------CLASS-------------------------------
     */

    //<editor-fold desc="49-search-class-by-subject-id-shift-id-status-paging">

    /**
     * @param branchId
     * @param subjectId
     * @param shiftId
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 49-search-class-by-subject-id-shift-id-status-paging
     * @author LamHNT & HuuNT - 2021.07.07
     */
    @CrossOrigin
    @RequestMapping(value = "/classes", method = RequestMethod.GET)
    public ResponseEntity<?> searchAllClassByBranchIdAndSubjectIdAndShiftIdAndStatusPaging(@RequestParam(value = "branchId") int branchId,
                                                                                           @RequestParam(value = "subjectId") int subjectId,
                                                                                           @RequestParam(value = "shiftId") int shiftId,
                                                                                           @RequestParam(value = "status") String status,
                                                                                           @RequestParam(value = "pageNo") int pageNo,
                                                                                           @RequestParam(value = "pageSize") int pageSize) throws Exception {
        return classService.searchAllClassByBranchIdAndSubjectIdAndShiftIdAndStatusPaging(branchId, subjectId, shiftId, status, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="53-create-new-class">

    /**
     * @param classRequestDto
     * @return
     * @throws Exception
     * @apiNote 53-create-new-class
     * @author LamHNT - 2021.07.05
     */
    @CrossOrigin
    @RequestMapping(value = "/classes", method = RequestMethod.POST)
    public ResponseEntity<?> createNewClass(@RequestBody ClassRequestDto classRequestDto) throws Exception {
        return classService.createNewClass(classRequestDto);
    }
    //</editor-fold>

    /**
     * -------------------------------SHIFT--------------------------------
     */

    //<editor-fold desc="71.0-search-shift-by-shift-id">

    /**
     * @param shiftId
     * @return
     * @throws Exception
     * @apiNote 71.0-search-shift-by-shift-id
     * @author HuuNT - 2021.06.26
     */
    @CrossOrigin
    @RequestMapping(value = "/shifts/{shiftId}", method = RequestMethod.GET)
    public ResponseEntity<?> searchShiftByShiftId(@PathVariable int shiftId) throws Exception {
        return shiftService.findShiftByShiftId(shiftId);
    }
    //</editor-fold>

    //<editor-fold desc="72.0-search-shift-by-dow-and-by-time-start-containing">

    /**
     * @param dayOfWeek
     * @param timeStart
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 72.0-search-shift-by-dow-and-by-time-start-containing
     * @author HuuNT - 2021.06.24 / LamHNT - 2021.06.26
     */
    @CrossOrigin
    @RequestMapping(value = "/shifts", params = "dayOfWeek", method = RequestMethod.GET)
    public ShiftPagingResponseDto searchShiftByDayOfWeekContainingOrTimeStartContaining(@RequestParam(value = "dayOfWeek") String dayOfWeek,
                                                                                        @RequestParam(value = "timeStart") String timeStart,
                                                                                        @RequestParam(value = "pageNo") int pageNo,
                                                                                        @RequestParam(value = "pageSize") int pageSize) {
        // pageNo starts at 0
        return shiftService.searchShiftByDayOfWeekContainingOrTimeStartContaining(dayOfWeek, timeStart, pageNo, pageSize);
    }
    //</editor-fold>

    //<editor-fold desc="73.0-create-new-shift">

    /**
     * @param shiftRequestDto
     * @return
     * @throws Exception
     * @apiNote 73.0-create-new-shift
     * @author LamHNT - 2021.06.24
     */
    @CrossOrigin
    @RequestMapping(value = "/shifts", method = RequestMethod.POST)
    public ResponseEntity<?> createNewShift(@RequestBody ShiftRequestDto shiftRequestDto) throws Exception {
        return shiftService.createNewShift(shiftRequestDto);
    }

    //</editor-fold>

    //<editor-fold desc="74.0-delete-shift-by-id">

    /**
     * @param shiftId
     * @return
     * @throws Exception
     * @apiNote 74.0-Delete-Shift
     * @author HuuNT - 2021.06.26
     */
    @CrossOrigin
    @RequestMapping(value = "/shifts/{shiftId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteShiftByShiftId(@PathVariable int shiftId) throws Exception {
        return shiftService.deleteShiftByShiftId(shiftId);
    }
    //</editor-fold>

    //<editor-fold desc="75.0-get-all-shift-by-isAvailable">


    /**
     * @param isAvailable
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     * @apiNote 75.0-get-all-shift-by-isAvailable
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

    //<editor-fold desc="76.0-Revival Shift by Id">

    /**
     * @param shiftId
     * @return
     * @throws Exception
     * @apiNote 76.0-revival shift by id
     * @author HuuNT - 2021.06.26
     */
    @CrossOrigin
    @RequestMapping(value = "/shifts/{shiftId}", method = RequestMethod.PUT)
    public ResponseEntity<?> revivalShiftByShiftId(@PathVariable int shiftId) throws Exception {
        return shiftService.revivalShiftbyShiftId(shiftId);
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