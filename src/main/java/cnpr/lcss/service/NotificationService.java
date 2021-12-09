package cnpr.lcss.service;

import cnpr.lcss.dao.Account;
import cnpr.lcss.dao.Notification;
import cnpr.lcss.dao.StudentInClass;
import cnpr.lcss.dao.Teacher;
import cnpr.lcss.model.NotiAndEmailToGroupRequestDto;
import cnpr.lcss.model.NotificationDto;
import cnpr.lcss.repository.*;
import cnpr.lcss.util.Constant;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
    FirebaseApp firebaseApp;

    //<editor-fold desc="15.01-create-notification-for-all-in-a-branch">
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createNotificationInBranch(HashMap<String, Object> reqBody) throws Exception {
        try {
            int branchId = (int) reqBody.get("branchId");
            String senderUsername = (String) reqBody.get("senderUsername");
            if (!senderUsername.equalsIgnoreCase(Constant.ACCOUNT_SYSTEM)) {
                if (!accountRepository.existsByUsername(senderUsername)) {
                    throw new Exception(Constant.INVALID_USERNAME);
                }
            }
            String title = (String) reqBody.get("title");
            String body = (String) reqBody.get("body");
            List<Account> staffList = accountRepository.findAvailableStaffAndManagerByBranchId(Constant.ROLE_ADMIN, branchId);
            List<Account> teacherList = accountRepository.findAvailableTeacherByBranchId(Constant.ROLE_TEACHER, branchId);
            List<Account> studentList = accountRepository.findAvailableStudentByBranchId(Constant.ROLE_STUDENT, branchId);
            List<Account> receiverList = new ArrayList<>();
            receiverList.addAll(staffList);
            receiverList.addAll(teacherList);
            receiverList.addAll(studentList);
            ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
            ZonedDateTime today = ZonedDateTime.now(zoneId);
            try {
                for (Account receiver : receiverList) {
                    Notification newNotification = new Notification();
                    newNotification.setSenderUsername(senderUsername.toLowerCase());
                    newNotification.setReceiverUsername(receiver);
                    newNotification.setTitle(title.trim());
                    newNotification.setBody(body.trim());
                    newNotification.setIsRead(Boolean.FALSE);
                    newNotification.setCreatingDate(Date.from(today.toInstant()));
                    newNotification.setLastModified(Date.from(today.toInstant()));
                    // Send notification to token's device
                    // who has token's device will get a noti
                    if (receiver.getToken() != null) {
                        Message message = com.google.firebase.messaging.Message.builder()
                                .setToken(receiver.getToken())
                                .setNotification(new com.google.firebase.messaging.Notification(newNotification.getTitle(), newNotification.getBody()))
                                .putData("content", newNotification.getTitle())
                                .putData("body", newNotification.getBody())
                                .build();
                        String response = "";
                        try {
                            response = FirebaseMessaging.getInstance().send(message);
                        } catch (Exception e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage() + " " + response);
                        }
                    }
                    notificationRepository.save(newNotification);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(Constant.ERROR_GENERATE_NOTIFICATION);
            }
            return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="15.02-create-notification-for-student-and-teacher-in-a-class">
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createNotificationInClass(HashMap<String, Object> reqBody) throws Exception {
        try {
            int classId = (int) reqBody.get("classId");
            String senderUsername = (String) reqBody.get("senderUsername");
            if (!senderUsername.equalsIgnoreCase(Constant.ACCOUNT_SYSTEM)) {
                if (!accountRepository.existsByUsername(senderUsername)) {
                    throw new Exception(Constant.INVALID_USERNAME);
                }
            }
            String title = (String) reqBody.get("title");
            String body = (String) reqBody.get("body");

            List<String> usernameList = new ArrayList<>();
            /**
             * Class with Status: WAITING or CANCELED
             * Find Student in Student In Class; Non-Teacher
             * Class with Status: STUDYING or FINISHED
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
                //first loop to first user who has token to send Noti first
                for (String user : usernameList) {
                    Account receiver = accountRepository.findOneByUsername(user);
                    //users have token
                    if (receiver.getToken() != null) {
                        Notification newNoti = new Notification();
                        newNoti.setSenderUsername(senderUsername.toLowerCase());
                        newNoti.setReceiverUsername(receiver);
                        newNoti.setTitle(title.trim());
                        newNoti.setBody(body.trim());
                        newNoti.setIsRead(Boolean.FALSE);
                        newNoti.setCreatingDate(Date.from(today.toInstant()));
                        newNoti.setLastModified(Date.from(today.toInstant()));
                        Message message = com.google.firebase.messaging.Message.builder()
                                .setToken(receiver.getToken())
                                .setNotification(new com.google.firebase.messaging.Notification(newNoti.getTitle(), newNoti.getBody()))
                                .putData("content", newNoti.getTitle())
                                .putData("body", newNoti.getBody())
                                .build();
                        String response = "";
                        try {
                            response = FirebaseMessaging.getInstance().send(message);
                        } catch (Exception e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage() + " " + response);
                        }

                        notificationRepository.save(newNoti);
                    }
                }
                //second loop to find users dont have token to store noti in db
                for (String user : usernameList) {
                    Account receiver = accountRepository.findOneByUsername(user);
                    //users dont have token
                    if (receiver.getToken() == null) {
                        Notification newNoti = new Notification();
                        newNoti.setSenderUsername(senderUsername.toLowerCase());
                        newNoti.setReceiverUsername(receiver);
                        newNoti.setTitle(title.trim());
                        newNoti.setBody(body.trim());
                        newNoti.setIsRead(Boolean.FALSE);
                        newNoti.setCreatingDate(Date.from(today.toInstant()));
                        newNoti.setLastModified(Date.from(today.toInstant()));
                        notificationRepository.save(newNoti);
                    }
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

    //<editor-fold desc="15.03-create-notification-for-a-person">
    public ResponseEntity<?> createNotificationForPerson(HashMap<String, Object> reqBody) throws Exception {
        try {
            String senderUsername = (String) reqBody.get("senderUsername");
            String receiverUsername = (String) reqBody.get("receiverUsername");
            if (!senderUsername.equalsIgnoreCase(Constant.ACCOUNT_SYSTEM)) {
                if (!accountRepository.existsByUsername(senderUsername))
                    throw new Exception(Constant.INVALID_USERNAME);
            }
            String title = (String) reqBody.get("title");
            String body = (String) reqBody.get("body");
            ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
            ZonedDateTime today = ZonedDateTime.now(zoneId);
            if (!accountRepository.existsByUsername(receiverUsername)) {
                throw new Exception((Constant.INVALID_USERNAME));
            }
            Account receiverAcc = accountRepository.findOneByUsername(receiverUsername);
            Notification newNoti = new Notification();
            newNoti.setSenderUsername(senderUsername.toLowerCase());
            newNoti.setReceiverUsername(receiverAcc);
            newNoti.setTitle(title.trim());
            newNoti.setBody(body.trim());
            newNoti.setIsRead(Boolean.FALSE);
            newNoti.setCreatingDate(Date.from(today.toInstant()));
            newNoti.setLastModified(Date.from(today.toInstant()));
            // Send notification to token's device
            // who has token's device will get a noti
            if (receiverAcc.getToken() != null) {
                Message message = com.google.firebase.messaging.Message.builder()
                        .setToken(receiverAcc.getToken())
                        .setNotification(new com.google.firebase.messaging.Notification(newNoti.getTitle(), newNoti.getBody()))
                        .putData("content", newNoti.getTitle())
                        .putData("body", newNoti.getBody())
                        .build();
                String response = "";
                try {
                    response = FirebaseMessaging.getInstance().send(message);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage() + " " + response);
                }
            }
            notificationRepository.save(newNoti);
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="15.04-create-notification-for-staff-and-manager-in-a-branch">
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createNotificationToStaff(HashMap<String, Object> reqBody) throws Exception {
        try {
            int branchId = (int) reqBody.get("branchId");
            String senderUsername = (String) reqBody.get("senderUsername");
            if (!senderUsername.equalsIgnoreCase(Constant.ACCOUNT_SYSTEM)) {
                if (!accountRepository.existsByUsername(senderUsername)) {
                    throw new Exception(Constant.INVALID_USERNAME);
                }
            }
            String title = (String) reqBody.get("title");
            String body = (String) reqBody.get("body");
            List<Account> staffList = accountRepository.findAvailableStaffAndManagerByBranchId(Constant.ROLE_ADMIN, branchId);
            ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
            ZonedDateTime today = ZonedDateTime.now(zoneId);
            try {
                //first loop to first user who has token to send Noti first
                for (Account staff : staffList) {
                    if (staff.getToken() != null) {
                        Notification newNotification = new Notification();
                        newNotification.setSenderUsername(senderUsername.toLowerCase());
                        newNotification.setReceiverUsername(staff);
                        newNotification.setTitle(title.trim());
                        newNotification.setBody(body.trim());
                        newNotification.setIsRead(Boolean.FALSE);
                        newNotification.setCreatingDate(Date.from(today.toInstant()));
                        newNotification.setLastModified(Date.from(today.toInstant()));
                        // Send notification to token's device
                        Message message = com.google.firebase.messaging.Message.builder()
                                .setToken(staff.getToken())
                                .setNotification(new com.google.firebase.messaging.Notification(newNotification.getTitle(), newNotification.getBody()))
                                .putData("content", newNotification.getTitle())
                                .putData("body", newNotification.getBody())
                                .build();
                        String response = "";
                        try {
                            response = FirebaseMessaging.getInstance().send(message);
                        } catch (Exception e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage() + " " + response);
                        }
                        notificationRepository.save(newNotification);
                    }

                }
                //second loop to find users dont have token to store noti in db
                for (Account staff : staffList) {
                    //users dont have token
                    if (staff.getToken() == null) {
                        Notification newNoti = new Notification();
                        newNoti.setSenderUsername(senderUsername.toLowerCase());
                        newNoti.setReceiverUsername(staff);
                        newNoti.setTitle(title.trim());
                        newNoti.setBody(body.trim());
                        newNoti.setIsRead(Boolean.FALSE);
                        newNoti.setCreatingDate(Date.from(today.toInstant()));
                        newNoti.setLastModified(Date.from(today.toInstant()));
                        notificationRepository.save(newNoti);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(Constant.ERROR_GENERATE_NOTIFICATION);
            }
            return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="15.05-get-all-notification">
    public ResponseEntity<?> getAllNotificationByUserName(String userName, int pageNo, int pageSize) {
        try {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            if (accountRepository.existsByUsername(userName) || userName.equalsIgnoreCase(Constant.ACCOUNT_SYSTEM)) {
                Page<Notification> notificationPage = notificationRepository.getAllByReceiverUsername_UsernameContainingIgnoreCaseOrderByCreatingDateDesc(userName, pageable);
                List<NotificationDto> notificationDtoList = notificationPage.getContent().stream()
                        .map(notification -> notification.convertToNotificationDto()).collect(Collectors.toList());
                mapObj.put("pageNo", pageNo);
                mapObj.put("pageSize", pageSize);
                mapObj.put("totalPage", notificationPage.getTotalPages());
                mapObj.put("notificationList", notificationDtoList);
            }
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="15.06-update-notification">
    public ResponseEntity<?> updateNotification(int notificationId, HashMap<String, Object> reqBody) throws Exception {
        try {
            Boolean isRead = (Boolean) reqBody.get("isRead");
            Notification updateNotification = notificationRepository.getById(notificationId);
            updateNotification.setIsRead(isRead);
            ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
            ZonedDateTime modifyTime = ZonedDateTime.now(zoneId);
            updateNotification.setLastModified(Date.from(modifyTime.toInstant()));
            notificationRepository.save(updateNotification);
            return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="15.07-send-noti-and-email-to-group-person">
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createNotificationAndSendEmailToGroup(NotiAndEmailToGroupRequestDto notiAndEmailToGroupRequestDto) throws Exception {
        try {
            // For Notification
            String senderUsername = notiAndEmailToGroupRequestDto.getSenderUsername();
            if (!senderUsername.equalsIgnoreCase(Constant.ACCOUNT_SYSTEM)) {
                if (!accountRepository.existsByUsername(senderUsername)) {
                    throw new Exception(Constant.INVALID_USERNAME);
                }
            }
            String title = notiAndEmailToGroupRequestDto.getTitle();
            String body = notiAndEmailToGroupRequestDto.getBody();
            // For Email
            String className = notiAndEmailToGroupRequestDto.getClassName();
            String oldOpeningDate = notiAndEmailToGroupRequestDto.getOldOpeningDate();
            String newOpeningDate = notiAndEmailToGroupRequestDto.getNewOpeningDate();
            ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
            ZonedDateTime today = ZonedDateTime.now(zoneId);

            for (String username : notiAndEmailToGroupRequestDto.getUsername()) {
                Account account = accountRepository.findOneByUsername(username);
                if (account != null) {
                    if (account.getToken() != null) {
                        Notification newNotification = new Notification();
                        newNotification.setSenderUsername(senderUsername.toLowerCase());
                        newNotification.setReceiverUsername(account);
                        newNotification.setTitle(title.trim());
                        newNotification.setBody(body.trim());
                        newNotification.setIsRead(Boolean.FALSE);
                        newNotification.setCreatingDate(Date.from(today.toInstant()));
                        newNotification.setLastModified(Date.from(today.toInstant()));
                        // Send notification to token's device
                        Message message = com.google.firebase.messaging.Message.builder()
                                .setToken(account.getToken())
                                .setNotification(new com.google.firebase.messaging.Notification(newNotification.getTitle(), newNotification.getBody()))
                                .putData("content", newNotification.getTitle())
                                .putData("body", newNotification.getBody())
                                .build();
                        String response = "";
                        try {
                            response = FirebaseMessaging.getInstance().send(message);
                        } catch (Exception e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage() + " " + response);
                        }
                        notificationRepository.save(newNotification);
                    }
                } else {
                    throw new Exception(Constant.INVALID_USERNAME);
                }

                boolean checkGmail = false;
                SendEmailService sendEmailService = new SendEmailService();
                checkGmail = sendEmailService.sendMailToGroup(account.getEmail(), account.getName(), className, oldOpeningDate, newOpeningDate);
                if (checkGmail = false) {
                    throw new Exception(Constant.ERROR_EMAIL_SENDING);
                }
            }
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="create-notification-for-staff-and-manager-in-a-branch FOR RELOADING">

    public void createNotificationToStaffForReloading(int branchId) throws Exception {
        try {
            String title = Constant.SYSTEM_NOTI_TITLE_FOR_RELOADING;
            String body = Constant.SYSTEM_NOTI_BODY_FOR_RELOADING;
            List<Account> staffList = accountRepository.findAvailableStaffAndManagerByBranchId(Constant.ROLE_ADMIN, branchId);
            try {
                //first loop to first user who has token to send Noti first
                for (Account staff : staffList) {
                    if (staff.getToken() != null) {
                        // Send notification to token's device
                        Message message = com.google.firebase.messaging.Message.builder()
                                .setToken(staff.getToken())
                                .setNotification(new com.google.firebase.messaging.Notification(title, body))
                                .putData("content",title)
                                .putData("body", body)
                                .build();
                        String response = "";
                        try {
                            response = FirebaseMessaging.getInstance().send(message);
                        } catch (Exception e) {
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(Constant.ERROR_GENERATE_NOTIFICATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>

}


