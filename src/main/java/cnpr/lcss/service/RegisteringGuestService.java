package cnpr.lcss.service;

import cnpr.lcss.dao.RegisteringGuest;
import cnpr.lcss.model.RegisteringGuestRequestDto;
import cnpr.lcss.model.RegisteringGuestSearchResponseDto;
import cnpr.lcss.repository.BranchRepository;
import cnpr.lcss.repository.CurriculumRepository;
import cnpr.lcss.repository.RegisteringGuestRepository;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegisteringGuestService {
    @Autowired
    CurriculumRepository curriculumRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    RegisteringGuestRepository registeringGuestRepository;
    @Autowired
    NotificationService notificationService;

    //<editor-fold desc="Convert from Unicode to normal string">
    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
    //</editor-fold>

    //<editor-fold desc="7.01-search-guest-by-branchid-and-name">
    public ResponseEntity<?> findRegisterGuestByBranchIdAndCustomerName(int branchId, String customerName, String phone, String curriculumName, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<RegisteringGuest> page = registeringGuestRepository.findRegisteringGuestByBranch_BranchIdAndCustomerNameContainingAndPhoneContainingAndCurriculum_CurriculumNameContaining(branchId, customerName, phone, curriculumName, pageable);
        List<RegisteringGuestSearchResponseDto> responseDtos = page.getContent().stream()
                .map(registeringGuest -> registeringGuest.convertToDto()).collect(Collectors.toList());
        HashMap<String, Object> mapObj = new LinkedHashMap<>();
        mapObj.put("pageNo", pageNo);
        mapObj.put("pageSize", pageSize);
        mapObj.put("totalPage", page.getTotalPages());
        mapObj.put("registeringGuestSearchResponseDtos", responseDtos);
        return ResponseEntity.ok(mapObj);
    }
    //</editor-fold>

    //<editor-fold desc="7.05-search-guest-by-status">
    public ResponseEntity<?> findRegisterGuestByBranchIdAndStatus(int branchId, String status, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<RegisteringGuest> page = registeringGuestRepository.findRegisteringGuestByBranch_BranchIdAndStatusContaining(branchId, status, pageable);
        List<RegisteringGuestSearchResponseDto> responseDtos = page.getContent().stream()
                .map(registeringGuest -> registeringGuest.convertToDto()).collect(Collectors.toList());
        HashMap<String, Object> mapObj = new LinkedHashMap<>();
        mapObj.put("pageNo", pageNo);
        mapObj.put("pageSize", pageSize);
        mapObj.put("totalPage", page.getTotalPages());
        mapObj.put("registeringGuestSearchResponseDtos", responseDtos);
        return ResponseEntity.ok(mapObj);
    }
    //</editor-fold>

    //<editor-fold desc="7.07-register-guest">
    public ResponseEntity<?> createNewRegisteringGuest(RegisteringGuestRequestDto insGuest) throws Exception {
        try {
            RegisteringGuest newGuest = new RegisteringGuest();
            Date today = new Date();

            // Customer Name
            if (insGuest.getCustomerName() != null && !insGuest.getCustomerName().isEmpty()) {
                newGuest.setCustomerName(insGuest.getCustomerName());
            } else {
                throw new ValidationException(Constant.INVALID_NAME);
            }

            // Phone
            if (insGuest.getPhone() != null && !insGuest.getPhone().isEmpty()
                    && insGuest.getPhone().matches(Constant.PHONE_PATTERN)) {
                newGuest.setPhone(insGuest.getPhone());
            } else {
                throw new ValidationException(Constant.INVALID_PHONE_PATTERN);
            }

            // City
            if (insGuest.getCity() != null && !insGuest.getCity().isEmpty()) {
                newGuest.setCity(insGuest.getCity());
            } else {
                throw new ValidationException(Constant.INVALID_CITY);
            }

            // Booking Date
            newGuest.setBookingDate(today);

            // Description
            newGuest.setDescription(insGuest.getDescription());

            // Status
            /**
             * Default status is "pending" for new Guest
             */
            newGuest.setStatus(Constant.STATUS_PENDING);

            // Curriculum Id
            if (curriculumRepository.existsByCurriculumId(insGuest.getCurriculumId())
                    && curriculumRepository.findIsAvailableByCurriculumId(insGuest.getCurriculumId())) {
                newGuest.setCurriculum(curriculumRepository.findOneByCurriculumId(insGuest.getCurriculumId()));
            } else {
                throw new ValidationException(Constant.INVALID_CURRICULUM_ID);
            }

            // Branch Id
            if (branchRepository.existsBranchByBranchId(insGuest.getBranchId())
                    && branchRepository.findIsAvailableByBranchId(insGuest.getBranchId())) {
                newGuest.setBranch(branchRepository.findByBranchId(insGuest.getBranchId()));
            } else {
                throw new ValidationException(Constant.INVALID_BRANCH_ID);
            }

            registeringGuestRepository.save(newGuest);
            notificationService.createNotificationToStaffForReloading(insGuest.getBranchId());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="7.08-update-guest">
    public ResponseEntity<?> updateGuest(int guestId, Map<String, String> cussAtt) throws Exception {
        try {
            String status = cussAtt.get("status");
            String description = cussAtt.get("description");
            RegisteringGuest updateGuest;

            if (registeringGuestRepository.existsById(guestId)) {
                updateGuest = registeringGuestRepository.getById(guestId);
                if (status.equalsIgnoreCase(Constant.STATUS_PENDING)
                        || status.equalsIgnoreCase(Constant.STATUS_CONTACTED)
                        || status.equalsIgnoreCase(Constant.STATUS_CANCELED)) {
                    updateGuest.setStatus(status);
                } else {
                    throw new ValidationException(Constant.INVALID_GUEST_STATUS);
                }
            } else {
                throw new ValidationException(Constant.INVALID_GUEST_ID);
            }
            updateGuest.setDescription(description);
            registeringGuestRepository.save(updateGuest);
            notificationService.createNotificationToStaffForReloading(updateGuest.getBranch().getBranchId());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
