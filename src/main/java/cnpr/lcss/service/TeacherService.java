package cnpr.lcss.service;

import cnpr.lcss.dao.Account;
import cnpr.lcss.dao.Class;
import cnpr.lcss.dao.Session;
import cnpr.lcss.dao.Teacher;
import cnpr.lcss.model.TeacherDto;
import cnpr.lcss.model.TeachingBranchBasicInfoDto;
import cnpr.lcss.model.TeachingSubjectBasicInfoDto;
import cnpr.lcss.repository.AccountRepository;
import cnpr.lcss.repository.TeacherRepository;
import cnpr.lcss.repository.TeachingBranchRepository;
import cnpr.lcss.repository.TeachingSubjectRepository;
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
}
