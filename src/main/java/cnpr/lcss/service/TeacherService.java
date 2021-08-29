package cnpr.lcss.service;

import cnpr.lcss.dao.Teacher;
import cnpr.lcss.model.TeacherDto;
import cnpr.lcss.model.TeachingBranchBasicInfoDto;
import cnpr.lcss.model.TeachingSubjectBasicInfoDto;
import cnpr.lcss.repository.TeacherRepository;
import cnpr.lcss.repository.TeachingBranchRepository;
import cnpr.lcss.repository.TeachingSubjectRepository;
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

    //<editor-fold desc="1.11-search-teachers-by-branch-id-and-by-subject-id">
    public ResponseEntity<?> findTeachersByBranchIdAndSubjectId(int branchId, int subjectId, int pageNo, int pageSize) throws Exception {
        try {
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<Teacher> teacherPage = teacherRepository.findByTeachingBranchList_Branch_BranchIdAndTeachingSubjectList_Subject_SubjectId(branchId, subjectId, pageable);
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
            int totalPage = teacherPage.getTotalPages();

            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);
            mapObj.put("totalPage", totalPage);
            mapObj.put("teacherList", teacherDtoList);
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
