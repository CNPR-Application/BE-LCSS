package cnpr.lcss.controller;

import cnpr.lcss.dao.Attendance;
import cnpr.lcss.dao.Class;
import cnpr.lcss.repository.AttendanceRepository;
import cnpr.lcss.repository.ClassRepository;
import cnpr.lcss.util.Constant;
import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@Service
@EnableScheduling
public class SchedulerService {
    @Autowired
    ClassRepository classRepository;
    @Autowired
    AttendanceRepository attendanceRepository;

    //<editor-fold desc="9.12-scan-all-classes-to-update-class-status-to-finished">

    /**
     * @throws Exception
     * @author LamHNT - 2021.10.21
     * @apiNote 9.12-scan-all-classes-to-update-class-status-to-finished
     */
    @Scheduled(cron = Constant.CRON_EVERY_DAY_AT_MIDNIGHT, zone = Constant.TIMEZONE)
    public void scanAndUpdateClasses() {
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of(Constant.TIMEZONE));

        /**
         * UPDATE CLASS STATUS
         * Classes which last Session is over → Class_Status = FINISHED
         */
        List<Class> studyingClassList = classRepository.findByStatus(Constant.CLASS_STATUS_STUDYING);
        for (Class aClass : studyingClassList) {
            if (currentDate.compareTo(ZonedDateTime.ofInstant(Iterables.getLast(aClass.getSessionList()).getEndTime().toInstant(), ZoneId.of(Constant.TIMEZONE))) > 0) {
                aClass.setStatus(Constant.CLASS_STATUS_FINISHED);
            }
        }
        classRepository.saveAll(studyingClassList);

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
    }
    //</editor-fold>
}
