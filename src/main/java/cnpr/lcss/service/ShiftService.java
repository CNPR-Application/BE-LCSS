package cnpr.lcss.service;

import cnpr.lcss.dao.Shift;
import cnpr.lcss.model.ShiftDto;
import cnpr.lcss.model.ShiftPagingResponseDto;
import cnpr.lcss.model.ShiftRequestDto;
import cnpr.lcss.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiftService {

    private final String DAY_OF_WEEK_PATTERN = "((\\d)[-])+(\\d|[C][N])";
    private final String TIME_START_PATTERN = "(08:00)|(09:30)|(14:00)|(15:30)|(18:00)|(19:30)";
    private final String TIME_END_PATTERN = "(09:30)|(11:00)|(15:30)|(17:00)|(19:30)|(21:00)";
    private final String DAY_OF_WEEK_PATTERN_ERROR = "DayOfWeek must be 2 days or more, separated by [-]! (2-4-6, 3-5, 7-CN)";
    private final String TIME_START_PATTERN_ERROR = "TimeStart must be [08:00, 09:30, 14:00, 15:30, 18:00, 19:30]!";
    private final String TIME_END_PATTERN_ERROR = "TimeEnd must be [09:30, 11:00, 15:30, 17:00, 19:30, 21:00]!";
    private final String DURATION_ERROR = "Duration must be multiples of 90 and larger than 0!";
    private final String SHIFT_EXISTED_ERROR = "This shift is already defined!";
    private final String SHIFT_ID_NOT_EXIST = "Shift ID does not exist!";
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    @Autowired
    ShiftRepository shiftRepository;

    //<editor-fold desc="Convert to Time End">
    public String convertToTimeEnd(String timeStart, int duration) throws ParseException {
        String timeEnd;

        Date d = sdf.parse(timeStart);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, duration);

        return timeEnd = sdf.format(cal.getTime());
    }
    //</editor-fold>

    //<editor-fold desc="Create New Shift">
    public ResponseEntity<?> createNewShift(ShiftRequestDto shiftRequestDto) throws Exception {
        try {
            if (shiftRequestDto.getDayOfWeek().matches(DAY_OF_WEEK_PATTERN)) {
                if (shiftRequestDto.getTimeStart().matches(TIME_START_PATTERN)) {
                    if ((shiftRequestDto.getDuration() % 90 == 0)
                            && (0 < (shiftRequestDto.getDuration() / 90))
                            && ((shiftRequestDto.getDuration() / 90) < 3)) {
                        Shift shift = new Shift();

                        shift.setTimeStart(shiftRequestDto.getTimeStart());
                        shift.setTimeEnd(convertToTimeEnd(shiftRequestDto.getTimeStart(), shiftRequestDto.getDuration()));
                        if (!shift.getTimeEnd().matches(TIME_END_PATTERN)) {
                            throw new Exception(TIME_END_PATTERN_ERROR);
                        }
                        shift.setDayOfWeek(shiftRequestDto.getDayOfWeek());
                        shift.setDuration(shiftRequestDto.getDuration());
                        shift.setAvailable(true);

                        if (!shiftRepository.existsByDayOfWeekAndTimeStartAndDuration(shift.getDayOfWeek(), shift.getTimeStart(), shift.getDuration())) {
                            shiftRepository.save(shift);
                        } else {
                            throw new Exception(SHIFT_EXISTED_ERROR);
                        }
                    } else {
                        throw new Exception(DURATION_ERROR);
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
    //</editor-fold>

    //<editor-fold desc="Search Shift by Day Of Week or Time Start containing">
    public ShiftPagingResponseDto searchShiftByDayOfWeekContainingOrTimeStartContaining(String dayOfWeek, String timeStart, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Shift> page = shiftRepository.findShiftByDayOfWeekContainingOrTimeStartContaining(dayOfWeek, timeStart, pageable);
        List<Shift> shiftList = page.getContent();
        List<ShiftDto> shiftDtoList = shiftList.stream().map(shift -> shift.convertToDto()).collect(Collectors.toList());

        int pageTotal = page.getTotalPages();

        ShiftPagingResponseDto shiftPagingResponseDto = new ShiftPagingResponseDto(shiftDtoList, pageNo, pageSize, pageTotal);

        return shiftPagingResponseDto;
    }
    //</editor-fold>

    // Find Shift by Shift Id
   public ResponseEntity<?> findShiftByShiftId(int shiftId) throws Exception {

        try {
            if (shiftRepository.existsById(shiftId)) {


            return ResponseEntity.ok(shiftRepository.findShiftByShiftId(shiftId));
            } else {
                throw new IllegalArgumentException(SHIFT_ID_NOT_EXIST);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Get All Shifts
/*    public ShiftPagingResponseDto findAllShift(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Shift> page = shiftRepository.findAll(pageable);
        List<Shift> shiftList = page.getContent();
        List<ShiftDto> shiftDtoList = shiftList.stream().map(shift -> shift.convertToDto()).collect(Collectors.toList());
        int pageTotal = page.getTotalPages();

        ShiftPagingResponseDto shiftPagingResponseDto = new ShiftPagingResponseDto(pageNo, pageSize, pageTotal, shiftDtoList);
        return shiftPagingResponseDto;
    }*/
}
