package cnpr.lcss.controller;

import cnpr.lcss.dao.Branch;
import cnpr.lcss.model.*;
import cnpr.lcss.service.AccountService;
import cnpr.lcss.service.BranchService;
import cnpr.lcss.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestApi {

    @Autowired
    AccountService accountService;
    @Autowired
    CurriculumService curriculumService;
    @Autowired
    BranchService branchService;

    /**
     * @return
     * @apiNote welcome page
     */
    @CrossOrigin
    @RequestMapping(value = "/")
    public String welcome() {
        return "Welcome to LCSS - Language Center Support System!";
    }

    /*-------------------------------ACCOUNT--------------------------------*/

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

    /*-------------------------------BRANCH--------------------------------*/

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
                                                      @RequestParam(value = "pageNo") int pageNo,
                                                      @RequestParam(value = "pageSize") int pageSize) {
        // pageNo starts at 0
        return branchService.findByBranchNameContainingIgnoreCase(keyword, pageNo, pageSize);
    }

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

    /**
     * @param
     * @return true/false
     * @throws Exception
     * @apiNote 11.0 - Create new branch
     * @author HuuNT -12.06.2021
     * @body new Branch
     */
    @CrossOrigin
    @RequestMapping(value = "/admin/branches", method = RequestMethod.POST)
    public ResponseEntity<?> createNewBranch(@RequestBody BranchRequestDto newBranch) throws Exception {
        return branchService.createNewBranch(newBranch);
    }

    /*-------------------------------CURRICULUM--------------------------------*/

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
                                                              @RequestParam(value = "pageNo") int pageNo,
                                                              @RequestParam(value = "pageSize") int pageSize) {
        // pageNo starts at 0
        return curriculumService.findByCurriculumNameContains(keyword, pageNo, pageSize);
    }

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
                                                              @RequestParam(value = "pageNo") int pageNo,
                                                              @RequestParam(value = "pageSize") int pageSize) {
        // pageNo starts at 0
        return curriculumService.findByCurriculumCodeContains(keyword, pageNo, pageSize);
    }

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
}