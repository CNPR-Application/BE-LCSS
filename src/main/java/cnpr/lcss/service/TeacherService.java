package cnpr.lcss.service;

import cnpr.lcss.dao.*;
import cnpr.lcss.dao.Class;
import cnpr.lcss.model.*;
import cnpr.lcss.repository.*;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    TeachingBranchRepository teachingBranchRepository;
    @Autowired
    TeachingSubjectRepository teachingSubjectRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    ShiftRepository shiftRepository;

    //<editor-fold desc="Mapping Teaching Branch And Teaching Subject To Teacher">
    private List<TeacherDto> mapTeachingBranchAndTeachingSubjectToTeacher(Page<Teacher> teacherPage) {
        List<TeacherDto> teacherDtoList = teacherPage.getContent().stream().map(teacher -> teacher.convertToTeacherDto()).collect(Collectors.toList());
        // Mapping Teaching Branch and Teaching Subject to each Teacher
        for (TeacherDto aTeacher : teacherDtoList) {
            // Mapping one by one Teaching Branch to aTeacher
            List<TeachingBranchBasicInfoDto> teachingBranchBasicInfoDtoList
                    = teachingBranchRepository.findByTeacher_TeacherId(aTeacher.getTeacherId())
                    .stream().map(teachingBranch -> teachingBranch.convertToTeachingBranchBasicInfoDto()).collect(Collectors.toList());
            aTeacher.setTeachingBranchList(teachingBranchBasicInfoDtoList);

            // Mapping one by one Teaching Subject to aTeacher
            List<TeachingSubjectBasicInfoDto> teachingSubjectBasicInfoDtoList
                    = teachingSubjectRepository.findByTeacher_TeacherId(aTeacher.getTeacherId())
                    .stream().map(teachingSubject -> teachingSubject.convertToTeachingBranchBasicInfoDto()).collect(Collectors.toList());
            aTeacher.setTeachingSubjectList(teachingSubjectBasicInfoDtoList);
        }
        return teacherDtoList;
    }
    //</editor-fold>

    //<editor-fold desc="1.11-search-teachers-by-branch-id-and-by-subject-id">
    public ResponseEntity<?> findTeachersByBranchIdAndSubjectId(int branchId, int subjectId, int pageNo, int pageSize) throws Exception {
        try {
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<Teacher> teacherPage;
            int totalPage;
            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);

            /**
             * CASE 1: Branch ID != 0 && Subject ID != 0
             * CASE 2: Branch ID == 0 && Subject ID != 0
             * CASE 3: Branch ID != 0 && Subject ID == 0
             * CASE 4: Branch ID == 0 && Subject ID == 0
             */
            //<editor-fold desc="CASE 1: Branch ID != 0 && Subject ID != 0">
            // CASE 1: Branch ID != 0 && Subject ID != 0
            if (branchId != 0 && subjectId != 0) {
                teacherPage = teacherRepository.findByTeachingBranchList_Branch_BranchIdAndTeachingSubjectList_Subject_SubjectId(branchId, subjectId, pageable);
                totalPage = teacherPage.getTotalPages();
                mapObj.put("totalPage", totalPage);
                mapObj.put("teacherList", mapTeachingBranchAndTeachingSubjectToTeacher(teacherPage));
            }
            //</editor-fold>

            //<editor-fold desc="CASE 2: Branch ID == 0 && Subject ID != 0">
            // CASE 2: Branch ID == 0 && Subject ID != 0
            if (branchId == 0 && subjectId != 0) {
                teacherPage = teacherRepository.findByTeachingSubjectList_Subject_SubjectId(subjectId, pageable);
                totalPage = teacherPage.getTotalPages();
                mapObj.put("totalPage", totalPage);
                mapObj.put("teacherList", mapTeachingBranchAndTeachingSubjectToTeacher(teacherPage));
            }
            //</editor-fold>

            //<editor-fold desc="CASE 3: Branch ID != 0 && Subject ID == 0">
            // CASE 3: Branch ID != 0 && Subject ID == 0
            if (branchId != 0 && subjectId == 0) {
                teacherPage = teacherRepository.findByTeachingBranchList_Branch_BranchId(branchId, pageable);
                totalPage = teacherPage.getTotalPages();
                mapObj.put("totalPage", totalPage);
                mapObj.put("teacherList", mapTeachingBranchAndTeachingSubjectToTeacher(teacherPage));
            }
            //</editor-fold>

            //<editor-fold desc="CASE 4: Branch ID == 0 && Subject ID == 0">
            // CASE 4: Branch ID == 0 && Subject ID == 0
            if (branchId == 0 && subjectId == 0) {
                teacherPage = teacherRepository.findAllTeacher(pageable);
                totalPage = teacherPage.getTotalPages();
                mapObj.put("totalPage", totalPage);
                mapObj.put("teacherList", mapTeachingBranchAndTeachingSubjectToTeacher(teacherPage));
            }
            //</editor-fold>
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="1.15-delete-teacher">
    public ResponseEntity<?> deleteTeacher(String username) throws Exception {
        try {
            Account account = accountRepository.findOneByUsername(username);
            if (account == null) {
                throw new IllegalArgumentException(Constant.INVALID_USERNAME);
            } else {
                Teacher teacher = account.getTeacher();
                Boolean teacherClassAbleToDelete = true;
                // check each class of this teacher,
                // is there any class with status waiting or studying,
                // if so, it should not be deleted
                List<Session> sessionList = teacher.getSessionList();
                for (Session session : sessionList) {
                    Class aClass = session.getAClass();
                    if (aClass.getStatus().matches(Constant.CLASS_STATUS_WAITING)
                            || aClass.getStatus().matches(Constant.CLASS_STATUS_STUDYING))
                        teacherClassAbleToDelete = false;
                }
                if (!teacherClassAbleToDelete) {
                    throw new IllegalArgumentException(Constant.ERROR_DELETE_TEACHER_CLASS);
                }

                if (teacherClassAbleToDelete) {
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

    //<editor-fold desc="1.19-get-available-teacher-for-opening-class">
    public ResponseEntity<?> getAvailableTeachersForOpeningClass(int branchId, int shiftId, String openingDate, int subjectId) throws Exception {
        try {
            if (!branchRepository.existsById(branchId)) {
                throw new IllegalArgumentException(Constant.INVALID_BRANCH_ID);
            }

            String datetimeStart, dateTimeEnd;
            try {
                Shift insShift = shiftRepository.findShiftByShiftId(shiftId);
                if (insShift == null)
                    throw new IllegalArgumentException(Constant.INVALID_SHIFT_ID);
                datetimeStart = openingDate + " " + insShift.getTimeStart() + ":00";
                dateTimeEnd = openingDate + " " + insShift.getTimeEnd() + ":00";
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
                throw new IllegalArgumentException(Constant.INVALID_SHIFT_ID);
            }

            List<Teacher> teacherQuery = teacherRepository.findAvailableTeachersForOpeningClass(branchId, datetimeStart, dateTimeEnd, subjectId);
            List<TeacherBasisDetailDto> teacherBasisDetailDtoList = teacherQuery.stream().map(Teacher::convertToTeacherBasisDetailDto).collect(Collectors.toList());
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            mapObj.put("teacherList", teacherBasisDetailDtoList);
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
