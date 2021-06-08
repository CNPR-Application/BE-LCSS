package cnpr.lcss.controller;

import cnpr.lcss.model.CurriculumPagingResponseDto;
import cnpr.lcss.model.LoginRequestDto;
import cnpr.lcss.model.LoginResponseDto;
import cnpr.lcss.service.AccountService;
import cnpr.lcss.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponseDto checkLogin(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
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
}
