package cnpr.lcss.controller;

import cnpr.lcss.model.LoginRequestDto;
import cnpr.lcss.model.LoginResponseDto;
import cnpr.lcss.service.AccountService;
import cnpr.lcss.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
