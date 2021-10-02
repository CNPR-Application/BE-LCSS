package cnpr.lcss.service;

import cnpr.lcss.dao.Booking;
import cnpr.lcss.model.BookingRequestDto;
import cnpr.lcss.model.BookingSearchResponseDto;
import cnpr.lcss.repository.*;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    ClassRepository classRepository;
    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    StudentInClassRepository studentInClassRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SubjectRepository subjectRepository;

    //<editor-fold desc="autoMapping Booking">
    public List<BookingSearchResponseDto> autoMapping(Page<Booking> bookingList) {
        List<BookingSearchResponseDto> bookingSearchResponseDtos = bookingList.getContent().stream().map(booking -> booking.convertToSearchDto()).collect(Collectors.toList());

        for (BookingSearchResponseDto bookingDto : bookingSearchResponseDtos) {
            bookingDto.setSubjectName(subjectRepository.findSubject_SubjectNameBySubjectId(bookingDto.getSubjectId()));
        }
        return bookingSearchResponseDtos;
    }
    //</editor-fold>

    //<editor-fold desc="8.01 Search Booking By Class Id and Phone and Status in A BRANCH">
    public ResponseEntity<?> findBookingByClassIdandPhoneAndStatus(int classId, String status, int pageNo, int pageSize) {
        // pageNo starts at 0
        // always set first page = 1 ---> pageNo - 1
        HashMap<String, Object> mapObj = new LinkedHashMap();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        try {
            Page<Booking> bookingList;
            int pageTotal;
            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);
            //CASE 1
            if (classId != 0) {
                bookingList = bookingRepository.findBookingByaClass_ClassIdAndStatusContainingAllIgnoreCase(classId, status, pageable);
                pageTotal = bookingList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(bookingList));
            }
            //Case 2
            //class ID=0 then get all by status
            if (classId == 0) {
                bookingList = bookingRepository.findBookingByStatusContainingAllIgnoreCase( status, pageable);
                pageTotal = bookingList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(bookingList));
            }
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="8.02 Search Booking By Student UserName">
    public ResponseEntity<?> findBookingByStudentUsername(String studentUsername, int pageNo, int pageSize) {
        // pageNo starts at 0
        // always set first page = 1 ---> pageNo - 1
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        HashMap<String, Object> mapObj = new LinkedHashMap();
        try {
            Page<Booking> bookingList;
            int pageTotal;
            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);
            //get booking by username
            bookingList = bookingRepository.findBookingByStudent_Account_Username(studentUsername, pageable);
            pageTotal = bookingList.getTotalPages();
            mapObj.put("pageTotal", pageTotal);
            mapObj.put("classList", autoMapping(bookingList));
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="8.03 Get Booking Detail By Id">
    public BookingSearchResponseDto findBookingByBookingId(int bookingId) throws Exception {
        if (bookingRepository.existsBookingByBookingId(bookingId)) {
            Booking booking = bookingRepository.findBookingByBookingId(bookingId);
            BookingSearchResponseDto bookingSearchResponseDto = booking.convertToSearchDto();
            //get subject Name
            String subjectName = (subjectRepository.findBySubjectId(bookingSearchResponseDto.getSubjectId()).getSubjectName());
            bookingSearchResponseDto.setSubjectName(subjectName);

            return bookingSearchResponseDto;
        } else {
            throw new ValidationException(Constant.INVALID_BOOKING_ID);
        }
    }
    //</editor-fold>

    //<editor-fold desc="8.05-create-new-booking">
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createNewBooking(BookingRequestDto insBooking) throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(Constant.TIMEZONE));
        try {
            Booking newBooking = new Booking();
            newBooking.setAClass(classRepository.findClassByClassId(insBooking.getClassId()));
            int subjectId = classRepository.findSubjectIdByClassId(insBooking.getClassId());
            newBooking.setSubjectId(subjectId);
            float subjectPrice = subjectRepository.findSubject_SubjectPriceBySubjectId(subjectId);
            if (insBooking.getPayingPrice() >= subjectPrice) {
                newBooking.setPayingPrice(insBooking.getPayingPrice());
            } else {
                throw new ValidationException(Constant.INVALID_BOOKING_PAYING_PRICE);
            }
            newBooking.setPayingDate(calendar.getTime());
            newBooking.setDescription(insBooking.getDescription());
            if (insBooking.getStatus().equalsIgnoreCase(Constant.BOOKING_STATUS_PAID)
                    || insBooking.getStatus().equalsIgnoreCase(Constant.BOOKING_STATUS_CANCELED)) {
                newBooking.setStatus(insBooking.getStatus());
            } else {
                throw new ValidationException(Constant.INVALID_BOOKING_STATUS);
            }
            newBooking.setStudent(studentRepository.findByStudent_StudentUsername(insBooking.getStudentUsername()));
            if (branchRepository.findIsAvailableByBranchId(insBooking.getBranchId())) {
                newBooking.setBranch(branchRepository.findByBranchId(insBooking.getBranchId()));
            } else {
                throw new ValidationException(Constant.INVALID_BRANCH_AVAILABLE);
            }
            int bookingId;
            try {
                Booking booking = bookingRepository.save(newBooking);
                bookingId = booking.getBookingId();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(Constant.ERROR_GET_BOOKING_ID);
            }
            HashMap mapObj = new LinkedHashMap();
            mapObj.put("bookingId", bookingId);
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
