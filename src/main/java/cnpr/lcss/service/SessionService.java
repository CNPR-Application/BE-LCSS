package cnpr.lcss.service;

import cnpr.lcss.dao.Session;
import cnpr.lcss.model.SessionClassDto;
import cnpr.lcss.model.SessionClassPagingDto;
import cnpr.lcss.model.SessionResponseDto;
import cnpr.lcss.repository.SessionRepository;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {
    @Autowired
    SessionRepository sessionRepository;

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

    //<editor-fold desc="View Session Of a Class">
    public SessionClassPagingDto viewSessionOfaClass(int classId, int pageNo, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Session> page = sessionRepository.findByaClass_ClassId(classId, pageable);
        List<Session> sessionList = page.getContent();
        List<SessionClassDto> sessionClassDtos = sessionList.stream().map(session -> session.convertToSessionClassDto()).collect(Collectors.toList());
        int pageTotal = page.getTotalPages();
        SessionClassPagingDto sessionClassPagingDto = new SessionClassPagingDto( pageNo, pageSize, pageTotal,sessionClassDtos);
        return sessionClassPagingDto;
    }
    //</editor-fold>
}
