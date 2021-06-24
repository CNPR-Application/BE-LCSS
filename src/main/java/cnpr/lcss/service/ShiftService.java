package cnpr.lcss.service;

import cnpr.lcss.dao.Shift;
import cnpr.lcss.model.ShiftDto;
import cnpr.lcss.model.ShiftRequestDto;
import cnpr.lcss.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.regex.PatternSyntaxException;

@Service
public class ShiftService {

    @Autowired
    ShiftRepository shiftRepository;

    private final String DAY_OF_WEEK_PATTERN = "(MON-WED-FRI)|(TUE-THU-SAT)|(SAT-SUN)";
    private final String TIME_START_PATTERN = "(08:00)|(09:30)|(14:00)|(15:30)|(18:00)|(19:30)";
    private final String TIME_END_PATTERN = "(09:30)|(14:00)|(15:30)|(18:00)|(19:30)|(21:00)";

    private final String DAY_OF_WEEK_PATTERN_ERROR = "DayOfWeek must be [MON-WED-FRI ,TUE-THU-SAT ,SAT-SUN]!";
    private final String TIME_START_PATTERN_ERROR = "TimeStart must be [08:00, 09:30, 14:00, 15:30, 18:00, 19:30]!";
    private final String TIME_END_PATTERN_ERROR = "TimeEnd must be [09:30, 14:00, 15:30, 18:00, 19:30, 21:00]!";

    // Create New Shift
    public ResponseEntity<?> createNewShift(ShiftRequestDto shiftRequestDto) throws Exception {
        try {
            if (shiftRequestDto.getDayOfWeek().matches(DAY_OF_WEEK_PATTERN)) {
                if (shiftRequestDto.getTimeStart().matches(TIME_START_PATTERN)) {
                    if (shiftRequestDto.getTimeEnd().matches(TIME_END_PATTERN)) {
                        Shift dto = new Shift();

                        dto.setDescription(shiftRequestDto.getDayOfWeek()
                                + ", " + shiftRequestDto.getTimeStart()
                                + ", " + shiftRequestDto.getTimeEnd());

                        shiftRepository.save(dto);
                    } else {
                        throw new Exception(TIME_END_PATTERN_ERROR);
                    }
                } else {
                    throw new Exception(TIME_START_PATTERN_ERROR);
                }
            } else {
                throw new Exception(DAY_OF_WEEK_PATTERN_ERROR);
            }

            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
