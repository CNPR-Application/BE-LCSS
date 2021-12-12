package cnpr.lcss.service;

import cnpr.lcss.dao.Class;
import cnpr.lcss.dao.*;
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
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SessionService {
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    ClassRepository classRepository;
    @Autowired
    ClassService classService;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    ShiftService shiftService;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;

    //<editor-fold desc="Get Start Time of the insert Date">
    public Date getStartTimeOfTheDate(ZonedDateTime insDate) {
        LocalDateTime ldtCurrentDate = insDate.toLocalDateTime().toLocalDate().atTime(LocalTime.MIN);
        Date startTimeOfTheDate = Date.from(ldtCurrentDate.atZone(ZoneId.of(Constant.TIMEZONE)).toInstant());
        return startTimeOfTheDate;
    }
    //</editor-fold>

    //<editor-fold desc="Get End Time of the insert Date">
    public Date getEndTimeOfTheDate(ZonedDateTime insDate) {
        LocalDateTime ldtCurrentDate = insDate.toLocalDateTime().toLocalDate().atTime(LocalTime.MAX);
        Date endTimeOfTheDate = Date.from(ldtCurrentDate.atZone(ZoneId.of(Constant.TIMEZONE)).toInstant());
        return endTimeOfTheDate;
    }
    //</editor-fold>

    //<editor-fold desc="Validate Update Session's new Start Time">
    public void validateUpdateSession_NewStartTime(Date newStartTime, Integer newShiftId, Class updateClass, Boolean changeAllTime) throws Exception {
        String startTime = String.format("%02d", newStartTime.getHours()) + Constant.SYMBOL_COLON + String.format("%02d", newStartTime.getMinutes());

        String newStartTime_dayOfWeek = String.valueOf(newStartTime.getDay() + 1);
        String[] daysOfWeek = updateClass.getShift().getDayOfWeek().split(Constant.SYMBOL_HYPHEN);
        daysOfWeek = classService.convertDowToInteger(daysOfWeek);
        boolean isDayInShift = false;
        for (String dow : daysOfWeek) {
            if (dow.equalsIgnoreCase(newStartTime_dayOfWeek)) {
                isDayInShift = true;
                break;
            }
        }

        if (!startTime.matches(Constant.TIME_START_PATTERN)) {
            throw new Exception(Constant.TIME_START_PATTERN_ERROR);
        } else if (newShiftId == 0 && changeAllTime == Boolean.TRUE
                && (!updateClass.getShift().getTimeStart().equals(startTime) || isDayInShift == Boolean.FALSE)) {
            throw new Exception(Constant.INVALID_NEW_SHIFT_ID);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Check existence Session by Start Time And Teacher or Start Time and Room">
    public String existSessionByStartTimeAndTeacherOrStartTimeAndRoom(Integer roomId, Integer teacherId, Integer classId) {
        return "New start time not available!\n" +
                "Class [" + classRepository.findClassNameByClassId(classId) + " - ID: " + classId + "]\n" +
                "of Teacher [" + teacherRepository.findTeacherUsernameByTeacherId(teacherId) + " - ID: " + teacherId + "]\n" +
                "and Room [" + roomRepository.findRoomNameByRoomId(roomId) + " - ID: " + roomId + "] took this place!";
    }
    //</editor-fold>

    //<editor-fold desc="Update New Start Time">
    public void updateNewStartTime(Date newStartTime, boolean changeAllTime, int shiftId, List<Session> sessionList, Session updateSession, Class updateClass) throws Exception {
        try {
            if (changeAllTime) {
                // Update all sessions
                try {
                    String newStartTime_dayOfWeek = String.valueOf(newStartTime.getDay() + 1);
                    String[] daysOfWeek = classService.convertDowToInteger(updateClass.getShift().getDayOfWeek().split(Constant.SYMBOL_HYPHEN));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(newStartTime);
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(newStartTime.getHours()));
                    calendar.set(Calendar.MINUTE, Integer.valueOf(newStartTime.getMinutes()));
                    int noOfSession = sessionList.indexOf(updateSession);
                    while (noOfSession < updateClass.getSlot()) {
                        if (classService.isDaysInShift(daysOfWeek, calendar)) {
                            sessionList.get(noOfSession).setAClass(updateClass);
                            sessionList.get(noOfSession).setStartTime(calendar.getTime());
                            Date newDate = new Date();
                            newDate.setDate(calendar.getTime().getDate());
                            newDate.setTime(calendar.getTime().getTime() + updateClass.getShift().getDuration() * 60000);
                            sessionList.get(noOfSession).setEndTime(newDate);
                            noOfSession++;
                        }
                        calendar.add(Calendar.DATE, 1);
                    }
                    sessionRepository.saveAll(sessionList);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception(Constant.ERROR_GENERATE_SESSIONS);
                }

                for (int i = sessionList.indexOf(updateSession); i < sessionList.size(); i++) {
                    List<Attendance> attendanceList = attendanceRepository.findBySession_SessionId(sessionList.get(i).getSessionId());
                    for (Attendance attendance : attendanceList) {
                        attendance.setCheckingDate(sessionList.get(i).getStartTime());
                    }
                    attendanceRepository.saveAll(attendanceList);
                }
            } else {
                // Update 1 session
                try {
                    updateSession.setStartTime(newStartTime);
                    Shift updateClass_Shift = shiftRepository.findByClassList_ClassId(updateClass.getClassId());
                    Date newDate = new Date();
                    newDate.setDate(newStartTime.getDate());
                    newDate.setTime(newStartTime.getTime() + updateClass_Shift.getDuration() * 60000);
                    updateSession.setEndTime(newDate);
                    sessionRepository.save(updateSession);

                    List<Attendance> attendanceList = attendanceRepository.findBySession_SessionId(updateSession.getSessionId());
                    for (Attendance attendance : attendanceList) {
                        attendance.setCheckingDate(newStartTime);
                    }
                    attendanceRepository.saveAll(attendanceList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(Constant.ERROR_UPDATE_NEW_START_TIME);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Update New Room">
    public void updateNewRoom(Integer newRoomId, boolean changeAllRoom, List<Session> sessionList, Session updateSession) throws Exception {
        try {
            Room newRoom = roomRepository.findByRoomId(newRoomId);
            if (changeAllRoom == Boolean.TRUE) {
                // change room for all sessions
                for (Session ses : sessionList) {
                    if (ses.getSessionId().compareTo(updateSession.getSessionId()) >= 0) {
                        // TODO: check session's room available
                        ses.setRoom(newRoom);
                    }
                }
                sessionRepository.saveAll(sessionList);
            } else {
                // change room for 1 session
                updateSession.setRoom(newRoom);
                sessionRepository.save(updateSession);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(Constant.ERROR_UPDATE_NEW_ROOM);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Update New Teacher">
    public void updateNewTeacher(Integer newTeacherId, boolean changeAllTeacher, List<Session> sessionList, Session updateSession) throws Exception {
        try {
            Teacher newTeacher = teacherRepository.findByTeacherId(newTeacherId);
            if (changeAllTeacher == Boolean.TRUE) {
                // change teacher for all sessions
                List<Session> sessionsOfAllStartTime = new ArrayList<>();
                for (Session ses : sessionList) {
                    if (ses.getSessionId().compareTo(updateSession.getSessionId()) >= 0) {
                        // TODO: check session's teacher available
                        List<Session> sessionsOfStartTime = sessionRepository.findByStartTime(ses.getStartTime());
                        for (Session ss : sessionsOfStartTime) {
                            if (ss.getTeacher().getTeacherId() == newTeacherId
                                    && ss.getAClass().getClassId() != updateSession.getAClass().getClassId()) {
                                throw new Exception(existSessionByStartTimeAndTeacherOrStartTimeAndRoom(ss.getRoom().getRoomId(), ss.getTeacher().getTeacherId(), ss.getAClass().getClassId()));
                            }
                            sessionsOfAllStartTime.add(ss);
                        }
                    }
                }
                for (Session session : sessionsOfAllStartTime) {
                    session.setTeacher(newTeacher);
                }
                sessionRepository.saveAll(sessionList);
            } else {
                // change teacher for 1 session
                updateSession.setTeacher(newTeacher);
                sessionRepository.save(updateSession);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(Constant.ERROR_UPDATE_NEW_TEACHER);
        }
    }
    //</editor-fold>

    //<editor-fold desc="11.03-view-schedule">
    public ResponseEntity<?> viewSchedule(String date, int branchId) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATETIME_PATTERN);
            Date datetimeStart = sdf.parse(date + Constant.DAY_START);
            Date datetimeEnd = sdf.parse(date + Constant.DAY_END);
            List<Session> sessions = sessionRepository.findSessionByDateAndBranchIdAndStatus(datetimeStart, datetimeEnd, Constant.CLASS_STATUS_STUDYING, branchId);
            List<SessionResponseDto> sessionList = sessions.stream().map(Session::convertToSessionResponseDto).collect(Collectors.toList());
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            mapObj.put("sessionList", sessionList);
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="11.04-view-session-of-class">
    public ResponseEntity<?> viewSessionOfaClass(int classId, int pageNo, int pageSize) throws Exception {
        try {
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<Session> page = sessionRepository.findByaClass_ClassId(classId, pageable);
            List<Session> sessionList = page.getContent();
            List<SessionClassDto> sessionClassDtos = sessionList.stream()
                    .map(session -> session.convertToSessionClassDto()).collect(Collectors.toList());
            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);
            mapObj.put("totalPage", page.getTotalPages());
            mapObj.put("sessionList", sessionClassDtos);
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="11.06-search-student-schedule">
    public ResponseEntity<?> searchStudentSchedule(String studentUsername, Date srchDate) throws Exception {
        try {
            ZonedDateTime currentDate = ZonedDateTime.ofInstant(srchDate.toInstant(), ZoneId.of(Constant.TIMEZONE));
            ZonedDateTime startDateOfWeek = currentDate.with(DayOfWeek.MONDAY);
            ZonedDateTime endDateOfWeek = currentDate.with(DayOfWeek.SUNDAY);
            Integer insStudent = studentRepository.findStudentByAccount_Username(studentUsername).getId();
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            ZonedDateTime cursor = startDateOfWeek;
            while (cursor.isBefore(endDateOfWeek.plusDays(1))) {
                List<StudentScheduleDto> studentScheduleDtoList = sessionRepository
                        .findByStartTimeAndEndTimeAndStudentId(getStartTimeOfTheDate(cursor), getEndTimeOfTheDate(cursor), insStudent)
                        .stream().map(session -> session.convertToStudentScheduleDto()).collect(Collectors.toList());
                DateAndScheduleDto dateAndStudentScheduleDto = new DateAndScheduleDto();
                dateAndStudentScheduleDto.setDatetime(Constant.convertToUTC7TimeZone(Date.from(cursor.toInstant())));
                dateAndStudentScheduleDto.setSessionList(studentScheduleDtoList);
                mapObj.put(cursor.getDayOfWeek().name(), dateAndStudentScheduleDto);
                cursor = cursor.plusDays(1);
            }
            return ResponseEntity.status(HttpStatus.OK).body(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="11.07-search-teacher-schedule">
    public ResponseEntity<?> searchTeacherSchedule(String teacherUsername, Date srchDate) throws Exception {
        try {
            ZonedDateTime currentDate = ZonedDateTime.ofInstant(srchDate.toInstant(), ZoneId.of(Constant.TIMEZONE));
            ZonedDateTime startDateOfWeek = currentDate.with(DayOfWeek.MONDAY);
            ZonedDateTime endDateOfWeek = currentDate.with(DayOfWeek.SUNDAY);
            Integer insTeacher = teacherRepository.findTeacherByAccount_Username(teacherUsername).getTeacherId();
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            ZonedDateTime cursor = startDateOfWeek;
            while (cursor.isBefore(endDateOfWeek.plusDays(1))) {
                List<TeacherScheduleDto> teacherScheduleDtoList = sessionRepository
                        .findByStartTimeAndEndTimeAndTeacherId(getStartTimeOfTheDate(cursor), getEndTimeOfTheDate(cursor), insTeacher)
                        .stream().map(session -> session.convertToTeacherScheduleDto()).collect(Collectors.toList());
                DateAndScheduleDto dateAndTeacherSchedule = new DateAndScheduleDto();
                dateAndTeacherSchedule.setDatetime(Constant.convertToUTC7TimeZone(Date.from(cursor.toInstant())));
                dateAndTeacherSchedule.setSessionList(teacherScheduleDtoList);
                mapObj.put(cursor.getDayOfWeek().name(), dateAndTeacherSchedule);
                cursor = cursor.plusDays(1);
            }
            return ResponseEntity.status(HttpStatus.OK).body(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="11.08-update-session-in-class">
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> updateSessionInClass(HashMap<String, Object> reqBody) throws Exception {
        try {
            Integer sessionId = Integer.parseInt(reqBody.get("sessionId").toString());
            Integer classId = Integer.parseInt(reqBody.get("classId").toString());
            Session updateSession = sessionRepository.findBySessionId(sessionId);
            Class updateClass = classRepository.findClassByClassId(classId);
            List<Session> sessionList = sessionRepository.findSessionByaClass_ClassId(classId);
            String insNewRoomId = reqBody.get("newRoomId").toString();
            Boolean changeAllRoom = Boolean.valueOf(reqBody.get("changeAllRoom").toString());
            String insNewTeacherId = reqBody.get("newTeacherId").toString();
            Boolean changeAllTeacher = Boolean.valueOf(reqBody.get("changeAllTeacher").toString());
            String insNewStartTime = reqBody.get("newStartTime").toString();
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATETIME_PATTERN);
            Date newStartTime = sdf.parse(insNewStartTime);
            Boolean changeAllTime = Boolean.valueOf(reqBody.get("changeAllTime").toString());
            Integer newShiftId = Integer.parseInt(reqBody.get("newShiftId").toString());

            if (!newStartTime.equals(updateSession.getStartTime())) {
                validateUpdateSession_NewStartTime(newStartTime, newShiftId, updateClass, changeAllTime);

                if (!changeAllTime) {
                    // change new start time for 1 session
                    List<Session> sessionsOfStartTime = sessionRepository.findByStartTime(newStartTime);
                    for (Session ses : sessionsOfStartTime) {
                        if (ses.getAClass().getClassId() != updateClass.getClassId()) {
                            // different class
                            if (ses.getRoom().getRoomId().equals(updateSession.getRoom().getRoomId())
                                    || ses.getTeacher().getTeacherId() == updateSession.getTeacher().getTeacherId()) {
                                // same room OR same teacher
                                throw new Exception(existSessionByStartTimeAndTeacherOrStartTimeAndRoom(ses.getRoom().getRoomId(), ses.getTeacher().getTeacherId(), ses.getAClass().getClassId()));
                            }
                        } else {
                            // same class
                            if (ses.getRoom().getRoomId().equals(updateSession.getRoom().getRoomId())
                                    && ses.getTeacher().getTeacherId() == updateSession.getTeacher().getTeacherId())
                                // same room AND same teacher AND
                                // not the first session of class
                                if (!ses.getSessionId().equals(sessionRepository.findSessionByaClass_ClassId(updateClass.getClassId()).get(0).getSessionId())) {
                                    throw new Exception(Constant.INVALID_NEW_SESSION);
                                }
                        }
                    }
                    updateNewStartTime(newStartTime, Boolean.FALSE, newShiftId, sessionList, updateSession, updateClass);
                    updateNewRoom(Integer.parseInt(insNewRoomId), changeAllRoom, sessionList, updateSession);
                    updateNewTeacher(Integer.parseInt(insNewTeacherId), changeAllTeacher, sessionList, updateSession);
                } else {
                    // change all time by new start time
                    Integer noOfSession = sessionList.indexOf(updateSession);
                    List<Session> sessionsOfStartTime = sessionRepository.findByStartTime(newStartTime);
                    for (Session ses : sessionsOfStartTime) {
                        if (ses.getRoom().getRoomId().equals(insNewRoomId)
                                && ses.getTeacher().getTeacherId() == Integer.parseInt(insNewTeacherId)
                                && ses.getAClass().getClassId() != updateClass.getClassId()) {
                            throw new Exception(existSessionByStartTimeAndTeacherOrStartTimeAndRoom(ses.getRoom().getRoomId(), ses.getTeacher().getTeacherId(), ses.getAClass().getClassId()));
                        } else if (ses.getRoom().getRoomId().equals(insNewRoomId)
                                && ses.getTeacher().getTeacherId() == Integer.parseInt(insNewTeacherId)
                                && ses.getAClass().getClassId() == updateClass.getClassId()) {
                            throw new Exception(Constant.INVALID_NEW_SESSION);
                        }
                    }
                    updateNewStartTime(newStartTime, Boolean.TRUE, newShiftId, sessionList, updateSession, updateClass);
                    updateNewRoom(Integer.parseInt(insNewRoomId), changeAllRoom, sessionList, updateSession);
                    updateNewTeacher(Integer.parseInt(insNewTeacherId), changeAllTeacher, sessionList, updateSession);
                }
            } else {
                updateNewRoom(Integer.parseInt(insNewRoomId), changeAllRoom, sessionList, updateSession);
                updateNewTeacher(Integer.parseInt(insNewTeacherId), changeAllTeacher, sessionList, updateSession);
            }
            return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
