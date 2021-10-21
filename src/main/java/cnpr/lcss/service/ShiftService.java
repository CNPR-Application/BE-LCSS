package cnpr.lcss.service;

import cnpr.lcss.dao.Shift;
import cnpr.lcss.model.ShiftDto;
import cnpr.lcss.model.ShiftPagingResponseDto;
import cnpr.lcss.model.ShiftRequestDto;
import cnpr.lcss.repository.ShiftRepository;
import cnpr.lcss.util.Constant;
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
    @Autowired
    ShiftRepository shiftRepository;
    SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_PATTERN);

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
            if (shiftRequestDto.getDayOfWeek().matches(Constant.DAY_OF_WEEK_PATTERN)) {
                if (shiftRequestDto.getTimeStart().matches(Constant.TIME_START_PATTERN)) {
                    if ((shiftRequestDto.getDuration() % 90 == 0)
                            && (0 < (shiftRequestDto.getDuration() / 90))
                            && ((shiftRequestDto.getDuration() / 90) < 3)) {
                        Shift shift = new Shift();

                        shift.setTimeStart(shiftRequestDto.getTimeStart());
                        shift.setTimeEnd(convertToTimeEnd(shiftRequestDto.getTimeStart(), shiftRequestDto.getDuration()));
                        if (!shift.getTimeEnd().matches(Constant.TIME_END_PATTERN)) {
                            throw new Exception(Constant.TIME_END_PATTERN_ERROR);
                        }
                        shift.setDayOfWeek(shiftRequestDto.getDayOfWeek());
                        shift.setDuration(shiftRequestDto.getDuration());
                        shift.setIsAvailable(true);

                        if (!shiftRepository.existsByDayOfWeekAndTimeStartAndDuration(shift.getDayOfWeek(), shift.getTimeStart(), shift.getDuration())) {
                            shiftRepository.save(shift);
                        } else {
                            throw new Exception(Constant.DUPLICATE_SHIFT);
                        }
                    } else {
                        throw new Exception(Constant.INVALID_DURATION);
                    }
                } else {
                    throw new Exception(Constant.TIME_START_PATTERN_ERROR);
                }
            } else {
                throw new Exception(Constant.INVALID_DAY_OF_WEEK);
            }

            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="13.01-search-shift-by-shift-id">
    public ResponseEntity<?> findShiftByShiftId(int shiftId) throws Exception {

        try {
            if (shiftRepository.existsById(shiftId)) {


                return ResponseEntity.ok(shiftRepository.findShiftByShiftId(shiftId));
            } else {
                throw new IllegalArgumentException(Constant.INVALID_SHIFT_ID);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="13.02-search-shift-by-dow-and-by-time-start-containing">
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

    //<editor-fold desc="13.04-delete-shift-by-id">
    public ResponseEntity<?> deleteShiftByShiftId(int shiftId) throws Exception {
        try {
            if (!shiftRepository.existsById(shiftId)) {
                throw new IllegalArgumentException(Constant.INVALID_SHIFT_ID);
            } else {
                Shift deleteShift = shiftRepository.findShiftByShiftId(shiftId);
                //check shift if there are class lists, if is empty delete shift
                if (deleteShift.getIsAvailable() && deleteShift.getClassList().isEmpty()) {
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

    //<editor-fold desc="13.05-get-all-shift-by-isAvailable">
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

    //<editor-fold desc="13.06-Revival Shift by Id">
    public ResponseEntity<?> revivalShiftbyShiftId(int shiftID) throws Exception {
        try {
            if (!shiftRepository.existsById(shiftID)) {
                throw new IllegalArgumentException(Constant.INVALID_SHIFT_ID);
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
