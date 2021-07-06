package cnpr.lcss.service;

import cnpr.lcss.dao.RegisteringGuest;
import cnpr.lcss.model.RegisteringGuestRequestDto;
import cnpr.lcss.repository.BranchRepository;
import cnpr.lcss.repository.CurriculumRepository;
import cnpr.lcss.repository.RegisteringGuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.text.Normalizer;
import java.util.Date;

@Service
public class RegisteringGuestService {

    /**
     * -----GUEST STATUS-----
     **/
    private static final String STATUS_PENDING = "pending";
    /**
     * -----PATTERN-----
     **/
    private static final String PHONE_PATTERN = "(84|0[3|5|7|8|9])+([0-9]{8})\\b";
    private static final String NAME_PATTERN = "[A-Za-z ]*";
    /**
     * -----ERROR MSG-----
     **/
    private static final String INVALID_CUSTOMER_NAME = "Invalid customer name!";
    private static final String INVALID_PHONE = "Invalid phone number!";
    private static final String INVALID_CITY = "Invalid city!";
    private static final String INVALID_DESCRIPTION = "Invalid description!";
    private static final String INVALID_CURRICULUM = "Curriculum is non-exist or not available!";
    private static final String INVALID_BRANCH = "Branch is non-exist or not available!";

    @Autowired
    CurriculumRepository curriculumRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    RegisteringGuestRepository registeringGuestRepository;

    //<editor-fold desc="Convert from Unicode to normal string">
    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
    //</editor-fold>

    //<editor-fold desc="Create New Registering Guest">
    public ResponseEntity<?> createNewRegisteringGuest(RegisteringGuestRequestDto insGuest) throws Exception {
        try {
            RegisteringGuest newGuest = new RegisteringGuest();
            Date today = new Date();

            // Customer Name
            if (insGuest.getCustomerName() != null && !insGuest.getCustomerName().isEmpty()
                    && stripAccents(insGuest.getCustomerName()).matches(NAME_PATTERN)) {
                newGuest.setCustomerName(insGuest.getCustomerName());
            } else {
                throw new ValidationException(INVALID_CUSTOMER_NAME);
            }

            // Phone
            if (insGuest.getPhone() != null && !insGuest.getPhone().isEmpty()
                    && insGuest.getPhone().matches(PHONE_PATTERN)) {
                newGuest.setPhone(insGuest.getPhone());
            } else {
                throw new ValidationException(INVALID_PHONE);
            }

            // City
            if (insGuest.getCity() != null && !insGuest.getCity().isEmpty()) {
                newGuest.setCity(insGuest.getCity());
            } else {
                throw new ValidationException(INVALID_CITY);
            }

            // Booking Date
            newGuest.setBookingDate(today);

            // Description
            if (insGuest.getDescription() != null && !insGuest.getDescription().isEmpty()) {
                newGuest.setDescription(insGuest.getDescription());
            } else {
                throw new ValidationException(INVALID_DESCRIPTION);
            }

            // Status
            /**
             * Default status is "pending" for new Guest
             */
            newGuest.setStatus(STATUS_PENDING);

            // Curriculum Id
            if (curriculumRepository.existsByCurriculumId(insGuest.getCurriculumId())
                    && curriculumRepository.findIsAvailableByCurriculumId(insGuest.getCurriculumId())) {
                newGuest.setCurriculum(curriculumRepository.findOneByCurriculumId(insGuest.getCurriculumId()));
            } else {
                throw new ValidationException(INVALID_CURRICULUM);
            }

            // Branch Id
            if (branchRepository.existsBranchByBranchId(insGuest.getBranchId())
                    && branchRepository.findIsAvailableByBranchId(insGuest.getBranchId())) {
                newGuest.setBranch(branchRepository.findByBranchId(insGuest.getBranchId()));
            } else {
                throw new ValidationException(INVALID_BRANCH);
            }

            registeringGuestRepository.save(newGuest);

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
