package cnpr.lcss.service;

import cnpr.lcss.dao.Room;
import cnpr.lcss.dao.Shift;
import cnpr.lcss.model.RoomAndBranchDto;
import cnpr.lcss.model.RoomDto;
import cnpr.lcss.repository.BranchRepository;
import cnpr.lcss.repository.RoomRepository;
import cnpr.lcss.repository.ShiftRepository;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ShiftRepository shiftRepository;

    //<editor-fold desc="14.01-get-available-rooms-for-opening-class">

    /**
     * Find Rooms by Branch ID and Shift ID
     * Which are Available from the Date (openingDate)
     * In order to select rooms for class that about to start
     */
    public ResponseEntity<?> getAvailableRoomsForOpeningClass(int branchId, int shiftId, String openingDate) throws Exception {
        try {
            if (!branchRepository.existsById(branchId)) {
                throw new IllegalArgumentException(Constant.INVALID_BRANCH_ID);
            }

            String datetimeStart, dateTimeEnd;
            try {
                Shift insShift = shiftRepository.findShiftByShiftId(shiftId);
                datetimeStart = openingDate + " " + insShift.getTimeStart() + ":00";
                dateTimeEnd = openingDate + " " + insShift.getTimeEnd() + ":00";
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
                throw new IllegalArgumentException(Constant.INVALID_SHIFT_ID);
            }

            List<Room> roomQuery = roomRepository.findAvailableRoomsForOpeningClass(branchId, datetimeStart, dateTimeEnd);
            List<RoomAndBranchDto> roomList = roomQuery.stream().map(Room::convertToRoomAndBranchDto).collect(Collectors.toList());
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            mapObj.put("roomList", roomList);
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="14.02-get-all-room-in-branch">
    public ResponseEntity<?> getAvailableRoomsInBranch(int branchId, boolean isAvailable, int pageNo, int pageSize) throws Exception {
        try {
            if (!branchRepository.existsById(branchId)) {
                throw new IllegalArgumentException(Constant.INVALID_BRANCH_ID);
            }
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<Room> rooms = roomRepository.findAllByBranch_BranchIdAndAndIsAvailable(branchId, isAvailable, pageable);
            List<RoomDto> roomList = rooms.stream().map(Room::convertToRoomDto).collect(Collectors.toList());
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);
            mapObj.put("totalPage", rooms.getTotalPages());
            mapObj.put("roomList", roomList);
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
