package cnpr.lcss.service;

import cnpr.lcss.dao.Session;
import cnpr.lcss.model.SessionResponseDto;
import cnpr.lcss.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    public ResponseEntity<?> viewSchedule(Date date, int pageNo, int pageSize) throws Exception {
        try {
            // FE sends date
            // Return Session which has same Date & Session's Class has Status = studying
            int year = date.getYear() + 1900;
            // range 0-11 aka Jan-Dec
            int month = date.getMonth() + 1;
            // range 1-31
            int day = date.getDate();
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<Session> page = sessionRepository.findSessionByDateAndClass_ClassStatusIsStudying(year, month, day, pageable);
            int totalPage = page.getTotalPages();
            List<Session> sessionList = page.getContent();
            List<SessionResponseDto> sessionResponseDtoList = sessionList.stream().map(Session::convertToSessionResponseDto).collect(Collectors.toList());

            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            mapObj.put("pageNo", pageNo);
            mapObj.put("totalPage", totalPage);
            mapObj.put("sessionList", sessionResponseDtoList);

            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
