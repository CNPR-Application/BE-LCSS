package cnpr.lcss.service;

import cnpr.lcss.model.RegisteringGuestRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RegisteringGuestService {

    //<editor-fold desc="Create New Registering Guest">
    public ResponseEntity<?> createNewRegisteringGuest(RegisteringGuestRequestDto insGuest) throws Exception {
        try {
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
