package cnpr.lcss.service;

import cnpr.lcss.dao.Account;
import cnpr.lcss.dao.Staff;
import cnpr.lcss.repository.AccountRepository;
import cnpr.lcss.repository.StaffRepository;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StaffService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    StaffRepository staffRepository;

    //<editor-fold desc="1.16-delete-staff">
    public ResponseEntity<?> deleteStaffOrManager(String username) throws Exception {
        try {
            Staff staff = staffRepository.findByAccount_Username(username);
            if (staff == null) {
                throw new IllegalArgumentException(Constant.INVALID_USERNAME);
            } else {
                Account account = staff.getAccount();
                if (account.getIsAvailable()) {
                    account.setIsAvailable(false);
                    accountRepository.save(account);
                    return ResponseEntity.ok(Boolean.TRUE);
                } else {
                    return ResponseEntity.ok(Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
