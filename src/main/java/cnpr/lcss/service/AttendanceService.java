package cnpr.lcss.service;

import cnpr.lcss.dao.Attendance;
import cnpr.lcss.model.AllStudentAttendanceInASessionDto;
import cnpr.lcss.model.StudentAttendanceInAClassDto;
import cnpr.lcss.model.UpdateAttendanceDto;
import cnpr.lcss.repository.AttendanceRepository;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.threeten.bp.temporal.ChronoField;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    @Autowired
    AttendanceRepository attendanceRepository;

    //<editor-fold desc="12.01-view-student-attendance-in-a-class">
    public ResponseEntity<?> viewStudentAttendanceInAClass(String studentUsername, int classId, int pageNo, int pageSize) throws Exception {
        /**
         * View Attendance list of a Student in a specified Class
         */
        try {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<Attendance> attendancePage = attendanceRepository.findStudentAttendanceInAClass(studentUsername, classId, pageable);
            List<StudentAttendanceInAClassDto> attendanceList = attendancePage.getContent().stream()
                    .map(attendance -> attendance.convertToStudentAttendanceInAClassDto()).collect(Collectors.toList());
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);
            mapObj.put("totalPage", attendancePage.getTotalPages());
            mapObj.put("attendanceList", attendanceList);
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="12.02-view-all-student-attendance-in-a-session">
    public ResponseEntity<?> viewAllStudentAttendanceInASession(int sessionId, int pageNo, int pageSize) throws Exception {
        /**
         * For Teacher usage
         * View all Student Attendance in a specified Session
         */
        try {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<Attendance> attendancePage = attendanceRepository.findAllStudentAttendanceInASession(sessionId, pageable);
            List<AllStudentAttendanceInASessionDto> attendanceList = attendancePage.getContent().stream()
                    .map(attendance -> attendance.convertToAllStudentAttendanceInASessionDto()).collect(Collectors.toList());
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);
            mapObj.put("totalPage", attendancePage.getTotalPages());
            mapObj.put("attendanceList", attendanceList);
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="12.03-update-attendance">
    public ResponseEntity<?> updateAttendance(List<UpdateAttendanceDto> updateAttendanceList) throws Exception {
        /**
         * Update Attendance info by List<UpdateAttendanceDto>
         * Update Attendance_CheckingDate to the latest request time
         */
        try {
            ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
            ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
            for (UpdateAttendanceDto dto : updateAttendanceList) {
                int attendanceId = dto.getAttendanceId();
                String status = dto.getStatus();
                Attendance updateAttendance = attendanceRepository.getById(attendanceId);
                if (!status.equalsIgnoreCase(Constant.ATTENDANCE_STATUS_NOT_YET)
                        && !status.equalsIgnoreCase(Constant.ATTENDANCE_STATUS_ABSENT)
                        && !status.equalsIgnoreCase(Constant.ATTENDANCE_STATUS_PRESENT)) {
                    throw new Exception(Constant.INVALID_ATTENDANCE_STATUS);
                } else {
                    updateAttendance.setStatus(status.toLowerCase());
                }
                updateAttendance.setCheckingDate(Date.from(currentTime.toInstant()));
                attendanceRepository.save(updateAttendance);
            }
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="12.04-reopen-attendance-in-a-session">
    public ResponseEntity<?> reopenAttendanceInASession(HashMap<String, Object> reqBody) throws Exception {
        try {
            Integer sessionId = (Integer) reqBody.get("sessionId");
            String reopenReason = (String) reqBody.get("reopenReason");
            if (reopenReason == null || reopenReason.isEmpty()) {
                reopenReason = Constant.NOT_AVAILABLE_INFO;
            }

            ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of(Constant.TIMEZONE));
            currentDate = currentDate.withHour(23).withMinute(59).withSecond(59).withNano(LocalTime.MAX.getNano());
            List<Attendance> attendanceListInASession = attendanceRepository.findBySession_SessionId(sessionId);
            for (Attendance attendance : attendanceListInASession) {
                attendance.setIsReopen(Boolean.TRUE);
                attendance.setClosingDate(Date.from(currentDate.plusDays(1).toInstant()));
                attendance.setReopenReason(reopenReason);
            }
            attendanceRepository.saveAll(attendanceListInASession);

            return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}