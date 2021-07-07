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

    /**
     * -----PATTERN-----
     */
    private static final String DAY_OF_WEEK_PATTERN = "((\\d)[-])+(\\d|[C][N])";
    private static final String TIME_START_PATTERN = "(08:00)|(09:30)|(14:00)|(15:30)|(18:00)|(19:30)";
    private static final String TIME_END_PATTERN = "(09:30)|(11:00)|(15:30)|(17:00)|(19:30)|(21:00)";
    /**
     * -----ERROR MSG-----
     */
    private static final String DAY_OF_WEEK_PATTERN_ERROR = "DayOfWeek must be 2 days or more, separated by [-]! (2-4-6, 3-5, 7-CN)";
    private static final String TIME_START_PATTERN_ERROR = "TimeStart must be [08:00, 09:30, 14:00, 15:30, 18:00, 19:30]!";
    private static final String TIME_END_PATTERN_ERROR = "TimeEnd must be [09:30, 11:00, 15:30, 17:00, 19:30, 21:00]!";
    private static final String DURATION_ERROR = "Duration must be multiples of 90 and larger than 0!";
    private static final String SHIFT_EXISTED_ERROR = "This shift is already defined!";
    private static final String SHIFT_ID_NOT_EXIST = "Shift ID does not exist!";
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
                        shift.setIsAvailable(true);

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

    //<editor-fold desc="Find Shift by Shift Id">
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
    //</editor-fold>

    //<editor-fold desc="Get All Shift By isAvailable">
    public ShiftPagingResponseDto findAllShiftByIsAvailable(boolean isAvailable, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Shift> page = shiftRepository.findByIsAvailable(isAvailable, pageable);
        List<Shift> shiftList = page.getContent();
        List<ShiftDto> shiftDtoList = shiftList.stream().map(shift -> shift.convertToDto()).collect(Collectors.toList());
        int pageTotal = page.getTotalPages();

        ShiftPagingResponseDto shiftPagingResponseDto = new ShiftPagingResponseDto(shiftDtoList, pageNo, pageSize, pageTotal);
        return shiftPagingResponseDto;
    }
    //</editor-fold>

    //<editor-fold desc="Delete Shift By Id">
    public ResponseEntity<?> deleteShiftByShiftId(int shiftId) throws Exception {
        try {
            if (!shiftRepository.existsById(shiftId)) {
                throw new IllegalArgumentException(SHIFT_ID_NOT_EXIST);
            } else {
                Shift deleteShift = shiftRepository.findShiftByShiftId(shiftId);
                if (deleteShift.getIsAvailable()) {
                    deleteShift.setIsAvailable(false);
                    shiftRepository.save(deleteShift);
                    return ResponseEntity.ok(Boolean.TRUE);
                } else {
                    return ResponseEntity.ok(Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Revival Shift by ID">
    public ResponseEntity<?> revivalShiftbyShiftId(int shiftID) throws Exception {
        try {
            if (!shiftRepository.existsById(shiftID)) {
                throw new IllegalArgumentException(SHIFT_ID_NOT_EXIST);
            } else {
                Shift updateShift = shiftRepository.findShiftByShiftId(shiftID);
                //nếu shift có isAvailable là false, sửa thành true, trả response là true
                if (updateShift.getIsAvailable() == false) {
                    updateShift.setIsAvailable(true);
                    shiftRepository.save(updateShift);
                    return ResponseEntity.ok(Boolean.TRUE);

                }//còn lại là response false
                else {
                    return ResponseEntity.ok(Boolean.FALSE);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }
    //</editor-fold>

}
