package cnpr.lcss.service;

import cnpr.lcss.dao.Class;
import cnpr.lcss.dao.*;
import cnpr.lcss.model.SessionClassDto;
import cnpr.lcss.model.SessionResponseDto;
import cnpr.lcss.repository.*;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    TeacherRepository teacherRepository;

    //<editor-fold desc="11.03-view-schedule">
    public ResponseEntity<?> viewSchedule(String date) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATETIME_PATTERN);
            Date datetimeStart = sdf.parse(date + Constant.DAY_START);
            Date datetimeEnd = sdf.parse(date + Constant.DAY_END);
            List<Session> sessions = sessionRepository.findByStartTimeAndAClass_Status(datetimeStart, datetimeEnd, Constant.CLASS_STATUS_STUDYING);
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
            List<SessionClassDto> sessionClassDtos = sessionList.stream().map(session -> session.convertToSessionClassDto()).collect(Collectors.toList());
            int pageTotal = page.getTotalPages();
            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);
            mapObj.put("pageTotal", pageTotal);
            mapObj.put("sessionClassList", sessionClassDtos);
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="11.08-update-session-in-class">
    public ResponseEntity<?> updateSessionInClass(HashMap<String, Object> reqBody) throws Exception {
        try {
            int sessionId = (int) reqBody.get("sessionId");
            int classId = (int) reqBody.get("classId");
            Integer newRoomId = (Integer) reqBody.get("newRoomId");
            Boolean changeAllRoom = (Boolean) reqBody.get("changeAllRoom");
            if (changeAllRoom == null) {
                changeAllRoom = Boolean.FALSE;
            }
            Integer newTeacherId = (Integer) reqBody.get("newTeacherId");
            Boolean changeAllTeacher = (Boolean) reqBody.get("changeAllTeacher");
            if (changeAllTeacher == null) {
                changeAllTeacher = Boolean.FALSE;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATETIME_PATTERN);
            Date newStartTime = (Date) sdf.parse((String) reqBody.get("newStartTime"));
            Boolean changeAllTime = (Boolean) reqBody.get("changeAllTime");
            if (changeAllTime == null) {
                changeAllTime = Boolean.FALSE;
            }
            Integer newShiftId = (Integer) reqBody.get("newShiftId");

            Session updateSession = sessionRepository.findBySessionId(sessionId);
            Class updateClass = classRepository.getById(classId);
            List<Session> sessionList = sessionRepository.findSessionByaClass_ClassId(classId);

            // CASE 1: Update Session with new Room ID
            if (newRoomId != null) {
                Room updateRoom = roomRepository.getById(newRoomId);
                if (changeAllRoom) {
                    for (int i = sessionList.indexOf(updateSession); i < sessionList.size(); i++) {
                        sessionList.get(i).setRoom(roomRepository.findByRoomId(newRoomId));
                    }
                    sessionRepository.saveAll(sessionList);
                } else {
                    updateSession.setRoom(roomRepository.findByRoomId(newRoomId));
                    sessionRepository.save(updateSession);
                }
            }

            // CASE 2: Update Session with new Teacher ID
            if (newTeacherId != null) {
                Teacher updateTeacher = teacherRepository.findByTeacherId(newTeacherId);
                if (changeAllTeacher) {
                    for (int i = sessionList.indexOf(updateSession); i < sessionList.size(); i++) {
                        sessionList.get(i).setTeacher(teacherRepository.findByTeacherId(newTeacherId));
                    }
                    sessionRepository.saveAll(sessionList);
                } else {
                    updateSession.setTeacher(teacherRepository.findByTeacherId(newTeacherId));
                    sessionRepository.save(updateSession);
                }
            }

            // CASE 3: Update Session with new Start Time
            if (newStartTime != null) {
                if (changeAllTime) {
                    String newStartTime_dayOfWeek = String.valueOf(newStartTime.getDay() + 1);
                    String[] daysOfWeek = updateClass.getShift().getDayOfWeek().split(Constant.SYMBOL_HYPHEN);
                    daysOfWeek = classService.convertDowToInteger(daysOfWeek);
                    boolean coincidence = false;
                    for (String dow : daysOfWeek) {
                        if (dow.equalsIgnoreCase(newStartTime_dayOfWeek)) {
                            coincidence = true;
                            break;
                        }
                    }
                    if (coincidence) {
                        try {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(newStartTime);
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(updateClass.getShift().getTimeStart().split(Constant.SYMBOL_COLON)[0]));
                            calendar.set(Calendar.MINUTE, Integer.valueOf(updateClass.getShift().getTimeStart().split(Constant.SYMBOL_COLON)[1]));
                            int totalSession = sessionList.indexOf(updateSession);
                            List<Date> dateList = new ArrayList<>();
                            while (totalSession < updateClass.getSlot()) {
                                if (classService.isDaysInShift(daysOfWeek, calendar)) {
                                    sessionList.get(totalSession).setAClass(updateClass);
                                    sessionList.get(totalSession).setStartTime(calendar.getTime());
                                    Date newDate = new Date();
                                    newDate.setDate(calendar.getTime().getDate());
                                    newDate.setTime(calendar.getTime().getTime() + updateClass.getShift().getDuration() * 60000);
                                    sessionList.get(totalSession).setEndTime(newDate);
                                    totalSession++;
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
                        if (newShiftId == null) {
                            throw new Exception(Constant.INVALID_NEW_SHIFT_ID);
                        } else {
                            String newStartTime_startTime = LocalTime.of(newStartTime.getHours(), newStartTime.getMinutes())
                                    .format(DateTimeFormatter.ofPattern(Constant.TIME_PATTERN));
                            String newShift_startTime = shiftRepository.findShift_TimeStartByShiftId(newShiftId);
                            if (!newStartTime_startTime.equalsIgnoreCase(newShift_startTime)) {
                                throw new Exception(Constant.INCOMPATIBLE_START_TIME);
                            } else {
                                try {
                                    Shift newShift = shiftRepository.findShiftByShiftId(newShiftId);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(newStartTime);
                                    calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(newShift.getTimeStart().split(Constant.SYMBOL_COLON)[0]));
                                    calendar.set(Calendar.MINUTE, Integer.valueOf(newShift.getTimeStart().split(Constant.SYMBOL_COLON)[1]));
                                    int totalSession = sessionList.indexOf(updateSession);
                                    daysOfWeek = classService.convertDowToInteger(newShift.getDayOfWeek().split(Constant.SYMBOL_HYPHEN));
                                    List<Date> dateList = new ArrayList<>();
                                    while (totalSession < updateClass.getSlot()) {
                                        if (classService.isDaysInShift(daysOfWeek, calendar)) {
                                            sessionList.get(totalSession).setAClass(updateClass);
                                            sessionList.get(totalSession).setStartTime(calendar.getTime());
                                            Date newDate = new Date();
                                            newDate.setDate(calendar.getTime().getDate());
                                            newDate.setTime(calendar.getTime().getTime() + newShift.getDuration() * 60000);
                                            sessionList.get(totalSession).setEndTime(newDate);
                                            totalSession++;
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
                            }
                        }
                    }
                } else { // changeAllTime = FALSE
                    boolean coincidence = false;
                    for (int i = sessionList.indexOf(updateSession); i < sessionList.size(); i++) {
                        if (sessionList.get(i).getStartTime().compareTo(newStartTime) == 0) {
                            coincidence = true;
                            break;
                        }
                    }
                    if (coincidence) {
                        throw new Exception(Constant.INVALID_NEW_SESSION);
                    } else {
                        String newStartTime_startTime = newStartTime.getHours() + Constant.SYMBOL_COLON + newStartTime.getMinutes();
                        Shift updateClass_Shift = shiftRepository.findByClassList_ClassId(classId);
                        updateSession.setStartTime(newStartTime);
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
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
