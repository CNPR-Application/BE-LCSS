package cnpr.lcss.service;

import cnpr.lcss.dao.Notification;
import cnpr.lcss.dao.StudentInClass;
import cnpr.lcss.dao.Teacher;
import cnpr.lcss.repository.*;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClassRepository classRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    StudentInClassRepository studentInClassRepository;
    @Autowired
    TeacherRepository teacherRepository;

    //<editor-fold desc="15.02-create-notification-for-student-and-teacher-in-a-class">
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createNotificationInClass(HashMap<String, Object> reqBody) throws Exception {
        try {
            int classId = (int) reqBody.get("classId");
            String senderUsername = (String) reqBody.get("senderUsername");
            if (!senderUsername.equalsIgnoreCase(Constant.ACCOUNT_SYSTEM)) {
                try {
                    accountRepository.existsByUsername(senderUsername);
                } catch (IllegalArgumentException iae) {
                    throw new Exception(Constant.INVALID_USERNAME);
                }
            }
            String title = (String) reqBody.get("title");
            String body = (String) reqBody.get("body");

            List<String> usernameList = new ArrayList<>();
            /**
             * Class with Status: WAITING OR CANCELED
             * Find Student in Student In Class; Non-Teacher
             * Class with Status: STUDYING OR FINISHED
             * Find Teacher in Session
             */
            List<StudentInClass> studentInClassList = studentInClassRepository.findStudentInClassIsAvailableByClassId(classId);
            for (StudentInClass student : studentInClassList) {
                usernameList.add(student.getStudent().getAccount().getUsername());
            }
            String classStatus = classRepository.findStatusByClassId(classId);
            if (classStatus.equalsIgnoreCase(Constant.CLASS_STATUS_STUDYING) || classStatus.equalsIgnoreCase(Constant.CLASS_STATUS_FINISHED)) {
                List<Teacher> teacherList = teacherRepository.findDistinctBySessionList_AClass_ClassIdAndAccount_IsAvailableIsTrue(classId);
                for (Teacher aTeacher : teacherList) {
                    usernameList.add(aTeacher.getAccount().getUsername());
                }
            }
            ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
            ZonedDateTime today = ZonedDateTime.now(zoneId);
            try {
                for (String user : usernameList) {
                    Notification newNoti = new Notification();
                    newNoti.setSenderUsername(senderUsername.toLowerCase());
                    newNoti.setReceiverUsername(accountRepository.findOneByUsername(user));
                    newNoti.setTitle(title.trim());
                    newNoti.setBody(body.trim());
                    newNoti.setRead(Boolean.FALSE);
                    newNoti.setCreatingDate(Date.from(today.toInstant()));
                    newNoti.setLastModified(Date.from(today.toInstant()));
                    notificationRepository.save(newNoti);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(Constant.ERROR_GENERATE_NOTIFICATION);
            }
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
