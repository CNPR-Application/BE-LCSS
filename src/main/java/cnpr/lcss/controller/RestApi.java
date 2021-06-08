package cnpr.lcss.controller;

import cnpr.lcss.dao.Branch;
import cnpr.lcss.model.LoginRequestDto;
import cnpr.lcss.model.LoginResponseDto;
import cnpr.lcss.service.AccountService;
import cnpr.lcss.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class RestApi {

    @Autowired
    AccountService accountService;
    @Autowired
    BranchService branchService;
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
     * @apiNote AS9.0 - Search Branch by Branch ID
     * @author HuuNT - 08.June.2021
     * @param branchId: int
     * @return branchdto
     * @throws Exception
     */
    @RequestMapping(value = "admin/branches/{branchId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Branch findByBranchId(@PathVariable int branchId) {
        return branchService.getBranchByBranchId(branchId);
    }


}
