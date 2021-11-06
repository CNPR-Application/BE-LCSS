package cnpr.lcss.service;

import cnpr.lcss.dao.Attendance;
import cnpr.lcss.dao.Class;
import cnpr.lcss.dao.Notification;
import cnpr.lcss.dao.StudentInClass;
import cnpr.lcss.repository.AttendanceRepository;
import cnpr.lcss.repository.ClassRepository;
import cnpr.lcss.repository.NotificationRepository;
import cnpr.lcss.util.Constant;
import com.google.common.collect.Iterables;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Component
@EnableScheduling
public class SchedulerService {
    @Autowired
    ClassRepository classRepository;
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    NotificationRepository notificationRepository;
    FirebaseApp firebaseApp;

    //<editor-fold desc="9.12-scan-all-classes-to-update-class-status-to-finished">
    @Scheduled(cron = Constant.CRON_EVERY_DAY_AT_MIDNIGHT, zone = Constant.TIMEZONE)
    public void scanAndUpdateClasses() {
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of(Constant.TIMEZONE));

        //<editor-fold desc="UPDATE CLASS STATUS & SEND NOTIFICATION TO STUDENTS TO REMIND TO GIVE FEEDBACK">
        /**
         * UPDATE CLASS STATUS
         * Classes which last Session is over → Class_Status = FINISHED
         */
        List<Class> studyingClassList = classRepository.findByStatus(Constant.CLASS_STATUS_STUDYING);
        List<Class> justFinishedClassList = new ArrayList<>();
        for (Class aClass : studyingClassList) {
            if (currentDate.isAfter(ZonedDateTime.ofInstant(Iterables.getLast(aClass.getSessionList()).getEndTime().toInstant(), ZoneId.of(Constant.TIMEZONE)))) {
                justFinishedClassList.add(aClass);
                aClass.setStatus(Constant.CLASS_STATUS_FINISHED);
            }
        }
        classRepository.saveAll(studyingClassList);

        /**
         * SEND NOTIFICATION TO STUDENTS TO REMIND TO GIVE FEEDBACK
         * Classes which Status is FINISHED,
         * send Notifications to all Students belong to that class to remind feedback
         */
        for (Class aClass : justFinishedClassList) {
            List<StudentInClass> studentInClassList = aClass.getStudentInClassList();
            for (StudentInClass sic : studentInClassList) {
                Notification newNoti = new Notification();
                newNoti.setSenderUsername(Constant.ACCOUNT_SYSTEM);
                newNoti.setReceiverUsername(sic.getStudent().getAccount());
                newNoti.setTitle(String.format(Constant.FEEDBACK_TITLE, aClass.getClassName()));
                newNoti.setBody(String.format(Constant.FEEDBACK_BODY, aClass.getClassName()));
                newNoti.setIsRead(Boolean.FALSE);
                newNoti.setCreatingDate(Date.from(currentDate.toInstant()));
                newNoti.setLastModified(Date.from(currentDate.toInstant()));
                if (sic.getStudent().getAccount() != null) {
                    if (sic.getStudent().getAccount().getToken() != null) {
                        Message message = Message.builder()
                                .setToken(sic.getStudent().getAccount().getToken())
                                .setNotification(new com.google.firebase.messaging.Notification(newNoti.getTitle(), newNoti.getBody()))
                                .putData("content", newNoti.getTitle())
                                .putData("body", newNoti.getBody())
                                .build();
                        String response = "";
                        try {
                            response = FirebaseMessaging.getInstance().send(message);
                        } catch (FirebaseMessagingException e) {
                            e.printStackTrace();
                        }
                        notificationRepository.save(newNoti);
                    }
                }
            }
        }
        //</editor-fold>

        //<editor-fold desc="UPDATE ATTENDANCE ISREOPEN">
        /**
         * UPDATE ATTENDANCE ISREOPEN
         * Attendances which Closing_Date is over → Attendance_Reopen = FALSE
         */
        List<Attendance> isReopenAttendanceList = attendanceRepository.findByIsReopenIsTrue();
        for (Attendance attendance : isReopenAttendanceList) {
            if (currentDate.isAfter(ZonedDateTime.ofInstant(attendance.getClosingDate().toInstant(), ZoneId.of(Constant.TIMEZONE)))) {
                attendance.setIsReopen(Boolean.FALSE);
            }
        }
        attendanceRepository.saveAll(isReopenAttendanceList);
        //</editor-fold>
    }
    //</editor-fold>
}
