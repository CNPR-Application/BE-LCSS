package cnpr.lcss.service;

import cnpr.lcss.dao.Class;
import cnpr.lcss.model.ClassDto;
import cnpr.lcss.model.ClassRequestDto;
import cnpr.lcss.repository.*;
import cnpr.lcss.util.Constant;
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

    @Autowired
    ClassRepository classRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    StudentInClassRepository studentInClassRepository;

    //<editor-fold desc="Create New Class">
    public ResponseEntity<?> createNewClass(ClassRequestDto insClass) throws Exception {
        try {
            Class newClass = new Class();
            Date today = new Date();

            // Class Name
            if (insClass.getClassName() != null && !insClass.getClassName().isEmpty()) {
                newClass.setClassName(insClass.getClassName());
            } else {
                throw new ValidationException(Constant.INVALID_CLASS_NAME);
            }

            // Opening Date
            if (insClass.getOpeningDate() != null && insClass.getOpeningDate().getDate() >= today.getDate()) {
                newClass.setOpeningDate(insClass.getOpeningDate());
            } else {
                throw new ValidationException(Constant.INVALID_OPENING_DATE);
            }

            // Status
            /**
             * Default status is "waiting" for new Class
             */
            newClass.setStatus(Constant.CLASS_STATUS_WAITING);

            // Branch Id
            if (branchRepository.existsBranchByBranchId(insClass.getBranchId())
                    && branchRepository.findIsAvailableByBranchId(insClass.getBranchId())) {
                newClass.setBranch(branchRepository.findByBranchId(insClass.getBranchId()));
            } else {
                throw new ValidationException(Constant.INVALID_BRANCH_ID);
            }

            // Subject Id
            if (subjectRepository.existsSubjectBySubjectId(insClass.getSubjectId())
                    && subjectRepository.findIsAvailableBySubjectId(insClass.getSubjectId())) {
                newClass.setSubject(subjectRepository.findBySubjectId(insClass.getSubjectId()));
            } else {
                throw new ValidationException(Constant.INVALID_SUBJECT_ID);
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
                if ((subject_slotPerWeek == 2 && shift_dayOfWeek.matches(Constant.TWO_DAYS_OF_WEEK_PATTERN))
                        || subject_slotPerWeek == 3 && shift_dayOfWeek.matches(Constant.THREE_DAYS_OF_WEEK_PATTERN)) {
                    newClass.setShift(shiftRepository.findShiftByShiftId(insClass.getShiftId()));
                } else {
                    throw new ValidationException(Constant.INVALID_SLOT_PER_WEEK_AND_DAY_OF_WEEK);
                }
            } else {
                throw new ValidationException(Constant.INVALID_SHIFT_ID);
            }

            classRepository.save(newClass);

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Auto Mapping">
    public List<ClassDto> autoMapping(Page<Class> classList) {
        List<ClassDto> classDtoList = classList.getContent().stream().map(aClass -> aClass.convertToDto()).collect(Collectors.toList());

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
            if (aClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_WAITING) || aClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_CANCELED)) {
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
            // Count Student In Class by Class ID
            aClass.setNumberOfStudent(studentInClassRepository.countStudentInClassByAClass_ClassId(aClass.getClassId()));
        }
        return classDtoList;
    }
    //</editor-fold>

    //<editor-fold desc="Search all Class by Branch Id / Subject Id / Shift Id / Status - Paging">
    public ResponseEntity<?> searchAllClassByBranchIdAndSubjectIdAndShiftIdAndStatusPaging(int branchId, int subjectId, int shiftId, String status, int pageNo, int pageSize) throws Exception {
        HashMap<String, Object> mapObj = new LinkedHashMap();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        /**
         * 8 CASES:
         * (Branch ID is MUST EXIST)
         * CASE 1: Subject ID != 0
         * CASE 2: Subject ID != 0 && Shift ID != 0
         * CASE 3: Subject ID != 0 && Shift ID != 0 && Status != null/empty
         * CASE 4: Subject ID != 0 && Status != null/empty
         * CASE 5: Shift ID != 0
         * CASE 6: Shift ID != 0 && Status != 0
         * CASE 7: Status != 0
         * CASE 8: All = 0
         */
        try {
            Page<Class> classList;
            int pageTotal;
            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);

            // CASE 1
            if (branchId != 0 && subjectId != 0 && shiftId == 0 && (status.isEmpty() || status == null)) {
                classList = classRepository.findByBranch_BranchIdAndSubject_SubjectId(branchId, subjectId, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 2
            if (branchId != 0 && subjectId != 0 && shiftId != 0 && (status.isEmpty() || status == null)) {
                classList = classRepository.findByBranch_BranchIdAndSubject_SubjectIdAndShift_ShiftId(branchId, subjectId, shiftId, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 3
            if (branchId != 0 && subjectId != 0 && shiftId != 0 && !status.isEmpty() && status != null) {
                classList = classRepository.findByBranch_BranchIdAndSubject_SubjectIdAndShift_ShiftIdAndStatusContainingIgnoreCase(branchId, subjectId, shiftId, status, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 4
            if (branchId != 0 && subjectId != 0 && shiftId == 0 && !status.isEmpty() && status != null) {
                classList = classRepository.findByBranch_BranchIdAndSubject_SubjectIdAndStatusContainingAllIgnoreCase(branchId, subjectId, status, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 5
            if (branchId != 0 && subjectId == 0 && shiftId != 0 && (status.isEmpty() || status == null)) {
                classList = classRepository.findByBranch_BranchIdAndShift_ShiftId(branchId, shiftId, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 6
            if (branchId != 0 && subjectId == 0 && shiftId != 0 && !status.isEmpty() && status != null) {
                classList = classRepository.findByBranch_BranchIdAndShift_ShiftIdAndStatusContainingAllIgnoreCase(branchId, shiftId, status, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 7
            if (branchId != 0 && subjectId == 0 && shiftId == 0 && !status.isEmpty() && status != null) {
                classList = classRepository.findByBranch_BranchIdAndStatusContainingAllIgnoreCase(branchId, status, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 8
            if (branchId != 0 && subjectId == 0 && shiftId == 0 && (status.isEmpty() || status == null)) {
                classList = classRepository.findByBranch_BranchId(branchId, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            return ResponseEntity.ok(mapObj);
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
                    if (aClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_WAITING) || aClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_CANCELED)) {
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
                throw new ValidationException(Constant.INVALID_BRANCH_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Get Classes Statistic">
    public ResponseEntity<?> getClassesStatistic(int branchId) throws Exception {
        /**
         * if branch id = 0 => find all branches by status
         * if branch id != 0 => check existence => find by branch id and status
         */
        HashMap<String, Object> mapObj = new LinkedHashMap<>();
        try {
            if (branchId == 0) {
                mapObj.put("waitingClass", classRepository.countByStatusAllIgnoreCase(Constant.CLASS_STATUS_WAITING));
                mapObj.put("studyingClass", classRepository.countByStatusAllIgnoreCase(Constant.CLASS_STATUS_STUDYING));
                mapObj.put("finishedClass", classRepository.countByStatusAllIgnoreCase(Constant.CLASS_STATUS_FINISHED));
                mapObj.put("canceledClass", classRepository.countByStatusAllIgnoreCase(Constant.CLASS_STATUS_CANCELED));
            } else if (branchId != 0) {
                if (branchRepository.existsBranchByBranchId(branchId) && branchRepository.findIsAvailableByBranchId(branchId)) {
                    mapObj.put("waitingClass", classRepository.countDistinctByBranch_BranchIdAndStatusAllIgnoreCase(branchId, Constant.CLASS_STATUS_WAITING));
                    mapObj.put("studyingClass", classRepository.countDistinctByBranch_BranchIdAndStatusAllIgnoreCase(branchId, Constant.CLASS_STATUS_STUDYING));
                    mapObj.put("finishedClass", classRepository.countDistinctByBranch_BranchIdAndStatusAllIgnoreCase(branchId, Constant.CLASS_STATUS_FINISHED));
                    mapObj.put("canceledClass", classRepository.countDistinctByBranch_BranchIdAndStatusAllIgnoreCase(branchId, Constant.CLASS_STATUS_CANCELED));
                } else {
                    throw new Exception(Constant.INVALID_BRANCH_ID);
                }
            }
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}