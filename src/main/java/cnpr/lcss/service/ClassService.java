package cnpr.lcss.service;

import cnpr.lcss.dao.Class;
import cnpr.lcss.model.ClassRequestDto;
import cnpr.lcss.repository.BranchRepository;
import cnpr.lcss.repository.ClassRepository;
import cnpr.lcss.repository.ShiftRepository;
import cnpr.lcss.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.Date;

@Service
public class ClassService {

    /**
     * -----CLASS CONSTANT VARIABLE-----
     */
    private static final String CLASS_STATUS_WAITING = "waiting";

    /**
     * -----ERROR MSG-----
     */
    private static final String INVALID_CLASS_NAME = "Class Name is null or empty!";
    private static final String INVALID_OPENING_DATE = "Class Opening Date is null or in the past!";
    private static final String INVALID_BRANCH_ID = "Branch Id is non-exist or not available!";
    private static final String INVALID_SUBJECT_ID = "Subject Id is non-exist or not available!";
    private static final String INVALID_SHIFT_ID = "Shift Id is non-exist or not available!";

    @Autowired
    ClassRepository classRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    ShiftRepository shiftRepository;

    //<editor-fold desc="Create New Class">
    public ResponseEntity<?> createNewClass(ClassRequestDto insClass) throws Exception {
        try {
            Class newClass = new Class();
            Date today = new Date();

            // Class Name
            if (insClass.getClassName() != null && !insClass.getClassName().isEmpty()) {
                newClass.setClassName(insClass.getClassName());
            } else {
                throw new ValidationException(INVALID_CLASS_NAME);
            }

            // Opening Date
            if (insClass.getOpeningDate() != null && insClass.getOpeningDate().getDate() >= today.getDate()) {
                newClass.setOpeningDate(insClass.getOpeningDate());
            } else {
                throw new ValidationException(INVALID_OPENING_DATE);
            }

            // Status
            /**
             * Default status is "waiting" for new Class
             */
            newClass.setStatus(CLASS_STATUS_WAITING);

            // Branch Id
            if (branchRepository.existsBranchByBranchId(insClass.getBranchId())
                    && branchRepository.findIsAvailableByBranchId(insClass.getBranchId())) {
                newClass.setBranch(branchRepository.findByBranchId(insClass.getBranchId()));
            } else {
                throw new ValidationException(INVALID_BRANCH_ID);
            }

            // Subject Id
            if (subjectRepository.existsSubjectBySubjectId(insClass.getSubjectId())
                    && subjectRepository.findIsAvailableBySubjectId(insClass.getSubjectId())) {
                newClass.setSubject(subjectRepository.findBySubjectId(insClass.getSubjectId()));
            } else {
                throw new ValidationException(INVALID_SUBJECT_ID);
            }

            // Slot
            /**
             * Slot of Class = Slot of inserted Subject
             */
            newClass.setSlot(subjectRepository.findSlotBySubjectId(insClass.getSubjectId()));

            // Shift Id
            if (shiftRepository.existsById(insClass.getShiftId())
                    && shiftRepository.findIsAvailableByShiftId(insClass.getShiftId())) {
                newClass.setShift(shiftRepository.findShiftByShiftId(insClass.getShiftId()));
            } else {
                throw new ValidationException(INVALID_SHIFT_ID);
            }

            classRepository.save(newClass);

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
