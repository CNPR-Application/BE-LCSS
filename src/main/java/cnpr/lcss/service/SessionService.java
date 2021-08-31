package cnpr.lcss.service;

import cnpr.lcss.dao.Session;
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
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<Session> page = sessionRepository.findByStartTimeAndAClass_Status(date, Constant.CLASS_STATUS_STUDYING, pageable);
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
