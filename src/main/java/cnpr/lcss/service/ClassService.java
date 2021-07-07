package cnpr.lcss.service;

import cnpr.lcss.dao.Class;
import cnpr.lcss.model.ClassDto;
import cnpr.lcss.model.ClassRequestDto;
import cnpr.lcss.repository.BranchRepository;
import cnpr.lcss.repository.ClassRepository;
import cnpr.lcss.repository.ShiftRepository;
import cnpr.lcss.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassService {

    /**
     * -----CLASS CONSTANT VARIABLE-----
     */
    private static final String CLASS_STATUS_WAITING = "waiting";
    private static final String CLASS_STATUS_STUDYING = "studying";
    private static final String CLASS_STATUS_FINISHED = "finished";
    private static final String CLASS_STATUS_CANCELED = "canceled";
    /**
     * -----ERROR MSG-----
     */
    private static final String INVALID_CLASS_NAME = "Class Name is null or empty!";
    private static final String INVALID_OPENING_DATE = "Class Opening Date is null or in the past!";
    private static final String INVALID_BRANCH_ID = "Branch Id is non-exist or not available!";
    private static final String INVALID_SUBJECT_ID = "Subject Id is non-exist or not available!";
    private static final String INVALID_SHIFT_ID = "Shift Id is non-exist or not available!";
    private static final String INVALID_SLOT_PER_WEEK_AND_DAY_OF_WEEK = "Subject's slot per week incompatible with Shift's days of week!";
    /**
     * -----PATTERN-----
     */
    private static final String TWO_DAYS_OF_WEEK_PATTERN = "(((\\d)[-])+(\\d|[C][N])){1}";
    private static final String THREE_DAYS_OF_WEEK_PATTERN = "(((\\d)[-]){2})+(\\d|[C][N]){1}";
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
            // Check existence & is available
            if (shiftRepository.existsById(insClass.getShiftId())
                    && shiftRepository.findIsAvailableByShiftId(insClass.getShiftId())) {
                // Check compatibility of subject's slot per week & shift's day of week
                /**
                 * If (slot per week) of (insert subject) = 2 => (day of week) of (shift) = 2
                 * Else if (slot per week) of (insert subject) = 3 => (day of week) of (shift) = 3
                 * Else throw new Exception
                 */
                int subject_slotPerWeek = subjectRepository.findSlotPerWeekBySubjectId(insClass.getSubjectId());
                String shift_dayOfWeek = shiftRepository.findShift_DayOfWeekByShiftId(insClass.getShiftId());
                if ((subject_slotPerWeek == 2 && shift_dayOfWeek.matches(TWO_DAYS_OF_WEEK_PATTERN))
                        || subject_slotPerWeek == 3 && shift_dayOfWeek.matches(THREE_DAYS_OF_WEEK_PATTERN)) {
                    newClass.setShift(shiftRepository.findShiftByShiftId(insClass.getShiftId()));
                } else {
                    throw new ValidationException(INVALID_SLOT_PER_WEEK_AND_DAY_OF_WEEK);
                }
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

    //<editor-fold desc="Search all Class by Branch Id / Subject Id / Shift Id / Status - Paging">
    public ResponseEntity<?> searchAllClassByBranchIdAndSubjectIdAndShiftIdAndStatusPaging(int branchId, int subjectId, int shiftId, String status, int pageNo, int pageSize) throws Exception {
        try {
            if (branchRepository.existsBranchByBranchId(branchId)) {
                if (subjectRepository.existsSubjectBySubjectId(subjectId)) {
                    if (shiftRepository.existsById(shiftId)) {
                        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

                        HashMap<String, Object> mapObj = new LinkedHashMap();
                        Page<Class> classList = classRepository.findByBranch_BranchIdAndSubject_SubjectIdAndShift_ShiftIdAndStatus(branchId, subjectId, shiftId, status, pageable);
                        List<ClassDto> classDtoList = classList.getContent().stream().map(aClass -> aClass.convertToDto()).collect(Collectors.toList());
                        int pageTotal = classList.getTotalPages();

                        for (ClassDto aClass : classDtoList) {
                            // Subject Name
                            aClass.setSubjectName(subjectRepository.findSubject_SubjectNameBySubjectId(aClass.getSubjectId()));
                            // Branch Name
                            aClass.setBranchName(branchRepository.findBranch_BranchNameByBranchId(aClass.getBranchId()));
                            // Shift Description
                            String description = shiftRepository.findShift_DayOfWeekByShiftId(aClass.getShiftId())
                                    + " (" + shiftRepository.findShift_TimeStartByShiftId(aClass.getShiftId())
                                    + " - " + shiftRepository.findShift_TimeEndByShiftId(aClass.getShiftId()) + ")";
                            aClass.setShiftDescription(description);
                            // Teacher AND Room
                            if (aClass.getStatus().equalsIgnoreCase(CLASS_STATUS_WAITING) || aClass.getStatus().equalsIgnoreCase(CLASS_STATUS_CANCELED)) {
                                aClass.setTeacherId(0);
                                aClass.setTeacherName(null);
                                aClass.setRoomNo(0);
                            } else {
                                // TODO: create connection between Session and Teacher
                                // TODO: check validation of Status
                                // Temporary set to 0 or null
                                aClass.setTeacherId(0);
                                aClass.setTeacherName(null);
                                aClass.setRoomNo(0);
                            }
                        }

                        mapObj.put("pageNo", pageNo);
                        mapObj.put("pageSize", pageSize);
                        mapObj.put("pageTotal", pageTotal);
                        mapObj.put("classList", classDtoList);
                        return ResponseEntity.ok(mapObj);
                    } else {
                        throw new ValidationException(INVALID_SHIFT_ID);
                    }
                } else {
                    throw new ValidationException(INVALID_SUBJECT_ID);
                }
            } else {
                throw new ValidationException(INVALID_BRANCH_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Get All Class By BranchId and Status">
    public ResponseEntity<?> searchAllClassByBranchIdAndStatusPaging(int branchId, String status, int pageNo, int pageSize) throws Exception {
        try {
            if (branchRepository.existsBranchByBranchId(branchId)) {
                        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

                        HashMap<String, Object> mapObj = new LinkedHashMap();
                        Page<Class> classList = classRepository.findClassByBranch_BranchIdAndStatus(branchId, status, pageable);
                        List<ClassDto> classDtoList = classList.getContent().stream().map(aClass -> aClass.convertToDto()).collect(Collectors.toList());
                        int pageTotal = classList.getTotalPages();

                        for (ClassDto aClass : classDtoList) {
                            // Subject Name
                            aClass.setSubjectName(subjectRepository.findSubject_SubjectNameBySubjectId(aClass.getSubjectId()));
                            // Branch Name
                            aClass.setBranchName(branchRepository.findBranch_BranchNameByBranchId(aClass.getBranchId()));
                            // Shift Description
                            String description = shiftRepository.findShift_DayOfWeekByShiftId(aClass.getShiftId())
                                    + " (" + shiftRepository.findShift_TimeStartByShiftId(aClass.getShiftId())
                                    + " - " + shiftRepository.findShift_TimeEndByShiftId(aClass.getShiftId()) + ")";
                            aClass.setShiftDescription(description);
                            // Teacher AND Room
                            if (aClass.getStatus().equalsIgnoreCase(CLASS_STATUS_WAITING) || aClass.getStatus().equalsIgnoreCase(CLASS_STATUS_CANCELED)) {
                                aClass.setTeacherId(0);
                                aClass.setTeacherName(null);
                                aClass.setRoomNo(0);
                            } else {
                                // TODO: create connection between Session and Teacher
                                // TODO: check validation of Status
                                // Temporary set to 0 or null
                                aClass.setTeacherId(0);
                                aClass.setTeacherName(null);
                                aClass.setRoomNo(0);
                            }
                        }

                        mapObj.put("pageNo", pageNo);
                        mapObj.put("pageSize", pageSize);
                        mapObj.put("pageTotal", pageTotal);
                        mapObj.put("classList", classDtoList);
                        return ResponseEntity.ok(mapObj);

            } else {
                throw new ValidationException(INVALID_BRANCH_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
