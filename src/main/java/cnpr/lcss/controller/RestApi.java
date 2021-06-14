package cnpr.lcss.controller;

import cnpr.lcss.model.CurriculumPagingResponseDto;
import cnpr.lcss.model.CurriculumRequestDto;
import cnpr.lcss.model.LoginRequestDto;
import cnpr.lcss.service.AccountService;
import cnpr.lcss.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestApi {

    @Autowired
    AccountService accountService;
    @Autowired
    CurriculumService curriculumService;

    /**
     * @return
     * @apiNote welcome page
     */
    @RequestMapping(value = "/")
    public String welcome() {
        return "Welcome to LCSS - Language Center Support System!";
    }

    /**
     * @param loginRequestDto
     * @return
     * @throws Exception
     * @apiNote 1.0-check-login
     * @author LamHNT - 2021.06.03
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> checkLogin(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
        return accountService.checkLogin(loginRequestDto);
    }

    /**
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     * @apiNote 13.0-search-curriculum-by-curriculum-name
     * @author LamHNT - 2021.06.08
     */
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
    @RequestMapping(value = "/curriculums/{curriculumId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCurriculumByCurriculumId(@PathVariable int curriculumId,
                                                            @RequestBody CurriculumRequestDto insCur) throws Exception {
        return curriculumService.updateCurriculum(curriculumId, insCur);
    }
}
