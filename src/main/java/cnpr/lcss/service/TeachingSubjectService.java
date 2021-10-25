package cnpr.lcss.service;

import cnpr.lcss.dao.TeachingSubject;
import cnpr.lcss.model.TeachingSubjectDto;
import cnpr.lcss.repository.SubjectRepository;
import cnpr.lcss.repository.TeacherRepository;
import cnpr.lcss.repository.TeachingSubjectRepository;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeachingSubjectService {
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    TeachingSubjectRepository teachingSubjectRepository;

    //<editor-fold desc="6.02-search-teaching-subject-by-teacher-username-and-is-available">
    public ResponseEntity<?> searchTeachingSubjectByTeacherUsernameAndIsAvailable(String teacherUsername) throws Exception {
        try {
            List<TeachingSubjectDto> teachingSubjectList;
            if (!teacherRepository.existsByAccount_Username(teacherUsername)) {
                throw new Exception(Constant.INVALID_USERNAME);
            } else {
                teachingSubjectList = teachingSubjectRepository
                        .findByTeacher_Account_UsernameAndSubject_IsAvailableIsTrue(teacherUsername)
                        .stream().map(teachingSubject -> teachingSubject.convertToTeachingSubjectDto()).collect(Collectors.toList());
            }
            return ResponseEntity.status(HttpStatus.OK).body(teachingSubjectList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="6.04-create-new-teaching-subject">
    public ResponseEntity<?> createNewTeachingSubject(HashMap<String, Object> reqBody) throws Exception {
        try {
            String teacherUsername = reqBody.get("teacherUsername").toString();
            Integer subjectId = Integer.parseInt(reqBody.get("subjectId").toString());

            Boolean existedTeachingSubject = teachingSubjectRepository.existsByTeacher_Account_UsernameAndSubject_SubjectId(teacherUsername, subjectId);
            if (existedTeachingSubject) {
                throw new Exception(Constant.EXISTED_TEACHING_SUBJECT);
            } else {
                TeachingSubject newTeachingSubject = new TeachingSubject();
                newTeachingSubject.setTeacher(teacherRepository.findTeacherByAccount_Username(teacherUsername));
                newTeachingSubject.setSubject(subjectRepository.findBySubjectId(subjectId));
                teachingSubjectRepository.save(newTeachingSubject);
            }
            return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
