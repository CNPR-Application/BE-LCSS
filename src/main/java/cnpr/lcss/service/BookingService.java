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
import org.springframework.jdbc.core.JdbcTemplate;
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
    StudentRepository studentRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    ClassRepository classRepository;
    @Autowired
    StudentInClassRepository studentInClassRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    //<editor-fold desc="Create New Booking">
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createNewBooking(BookingRequestDto insBooking) throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(Constant.TIMEZONE));
        Date today = calendar.getTime();

        try {
            Booking newBooking = new Booking();

            //<editor-fold desc="Insert data to Booking">
            /**
             * Insert data to Booking
             */

            // Class ID
            // Check existence
            if (classRepository.existsById(insBooking.getClassId())) {
                newBooking.setAClass(classRepository.findClassByClassId(insBooking.getClassId()));
            } else {
                throw new IllegalArgumentException(Constant.INVALID_CLASS_ID);
            }

            // Subject ID
            int subjectId = classRepository.findSubjectIdByClassId(insBooking.getClassId());
            newBooking.setSubjectId(subjectId);

            // Paying Price
            // GREATER or EQUAL to Subject's Price
            Float subjectPrice = subjectRepository.findSubject_SubjectPriceBySubjectId(subjectId);
            if (insBooking.getPayingPrice() >= subjectPrice) {
                newBooking.setPayingPrice(insBooking.getPayingPrice());
            } else {
                throw new ValidationException(Constant.INVALID_BOOKING_PAYING_PRICE);
            }

            // Paying Date
            newBooking.setPayingDate(today);

            // Description
            newBooking.setDescription(insBooking.getDescription());

            // Status
            if (insBooking.getStatus().equalsIgnoreCase(Constant.BOOKING_STATUS_PAID)
                    || insBooking.getStatus().equalsIgnoreCase(Constant.BOOKING_STATUS_CANCELED)) {
                newBooking.setStatus(insBooking.getStatus());
            } else {
                throw new ValidationException(Constant.INVALID_BOOKING_STATUS);
            }

            // Student Username
            // Check existence
            if (studentRepository.existsByAccount_Username(insBooking.getStudentUsername())) {
                newBooking.setStudent(studentRepository.findByStudent_StudentUsername(insBooking.getStudentUsername()));
            } else {
                throw new ValidationException(Constant.INVALID_STUDENT_USERNAME);
            }

            // Branch ID
            // Check existence
            if (branchRepository.existsById(insBooking.getBranchId())) {
                // Check is available
                if (branchRepository.findIsAvailableByBranchId(insBooking.getBranchId())) {
                    newBooking.setBranch(branchRepository.findByBranchId(insBooking.getBranchId()));
                } else {
                    throw new ValidationException(Constant.INVALID_BRANCH_AVAILABLE);
                }
            } else {
                throw new ValidationException(Constant.INVALID_BRANCH_ID);
            }

            // Insert new Booking to DB
            int bookingId;
            try {
                Booking booking = bookingRepository.save(newBooking);
                bookingId = booking.getBookingId();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(Constant.ERROR_GET_BOOKING_ID);
            }
            //</editor-fold>

            HashMap mapObj = new LinkedHashMap();
            mapObj.put("bookingId", bookingId);
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

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
    public ResponseEntity<?> findBookingByClassIdandPhoneAndStatus(int branchId, int classId, String status, int pageNo, int pageSize) {
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
                bookingList = bookingRepository.findBookingByaClass_ClassIdAndBranch_BranchIdAndStatusContainingAllIgnoreCase(classId, branchId, status, pageable);
                pageTotal = bookingList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(bookingList));
            }
            //Case 2
            if (classId == 0) {
                bookingList = bookingRepository.findBookingByBranch_BranchIdAndStatusContainingAllIgnoreCase(branchId, status, pageable);
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
}
