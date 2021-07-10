package cnpr.lcss.service;

import cnpr.lcss.dao.Booking;
import cnpr.lcss.dao.StudentInClass;
import cnpr.lcss.model.BookingRequestDto;
import cnpr.lcss.model.BookingSearchResponseDto;
import cnpr.lcss.model.BookingSearchResponsePagingDto;
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
import java.sql.SQLException;
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
    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<?> createNewBooking(BookingRequestDto insBooking) throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(Constant.TIMEZONE));
        Date today = calendar.getTime();

        try {
            Booking newBooking = new Booking();

            //<editor-fold desc="Insert data to Booking">
            /**
             * Insert data to Booking
             */

            // Subject ID
            // Check existence
            if (subjectRepository.existsSubjectBySubjectId(insBooking.getSubjectId())) {
                // Check is available
                if (subjectRepository.findIsAvailableBySubjectId(insBooking.getSubjectId())) {
                    newBooking.setSubject(subjectRepository.findBySubjectId(insBooking.getSubjectId()));
                } else {
                    throw new ValidationException(Constant.INVALID_SUBJECT_AVAILABLE);
                }
            } else {
                throw new ValidationException(Constant.INVALID_SUBJECT_ID);
            }

            // Paying Price
            // GREATER or EQUAL to Subject's Price
            Float subjectPrice = subjectRepository.findSubject_SubjectPriceBySubjectId(insBooking.getSubjectId());
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

            // Student ID
            // Check existence
            if (studentRepository.existsById(insBooking.getStudentId())) {
                newBooking.setStudent(studentRepository.findById(insBooking.getStudentId()));
            } else {
                throw new ValidationException(Constant.INVALID_STUDENT_ID);
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

            // Shift ID
            // Check existence
            if (shiftRepository.existsById(insBooking.getShiftId())) {
                // Check is available
                if (shiftRepository.findIsAvailableByShiftId(insBooking.getShiftId())) {
                    newBooking.setShift(shiftRepository.findShiftByShiftId(insBooking.getShiftId()));
                } else {
                    throw new ValidationException(Constant.INVALID_SHIFT_AVAILABLE);
                }
            } else {
                throw new ValidationException(Constant.INVALID_SHIFT_ID);
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

            //<editor-fold desc="Insert data to Student In Class">

            /**
             * Insert data to Student In Class
             */
            StudentInClass newStdInClass = new StudentInClass();

            // Set Booking ID into New Student In Class
            try {
                newStdInClass.setBooking(bookingRepository.findBookingByBookingId(bookingId));
            } catch (Exception e) {
                throw new Exception(Constant.INVALID_BOOKING_ID);
            }

            // Class ID
            // Check existence
            if (classRepository.existsById(insBooking.getClassId())) {
                // Class Status must be waiting
                if (classRepository.findStatusByClassId(insBooking.getClassId()).equalsIgnoreCase(Constant.CLASS_STATUS_WAITING)) {
                    newStdInClass.setAClass(classRepository.findClassByClassId(insBooking.getClassId()));
                } else {
                    throw new ValidationException(Constant.INVALID_CLASS_STATUS_NOT_WAITING);
                }
            } else {
                bookingRepository.delete(bookingRepository.findBookingByBookingId(bookingId));
                throw new ValidationException(Constant.INVALID_CLASS_ID);
            }

            // Student ID
            // Student is validated above
            newStdInClass.setStudent(studentRepository.findById(insBooking.getStudentId()));

            // Teacher Rating
            newStdInClass.setTeacherRating(0);

            // Subject Rating
            newStdInClass.setSubjectRating(0);

            // Feedback
            newStdInClass.setFeedback(null);

            HashMap mapObj = new LinkedHashMap();
            mapObj.put("bookingId", bookingId);

            // Insert new Student In Class to DB
            try {
                studentInClassRepository.save(newStdInClass);
                return ResponseEntity.ok(mapObj);
            } catch (Exception e) {
                bookingRepository.delete(bookingRepository.findBookingByBookingId(bookingId));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constant.ERROR_SAVE_STUDENT_IN_CLASS);
            }
            //</editor-fold>
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Search Booking By  Student Id">
    public BookingSearchResponsePagingDto findBookingByStudentUsername(String studentUsername, int pageNo, int pageSize) {
        // pageNo starts at 0
        // always set first page = 1 ---> pageNo - 1
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Booking> page = bookingRepository.findBookingByStudent_Account_Username(studentUsername,pageable) ;
        List<Booking> bookingList = page.getContent();
        List<BookingSearchResponseDto> bookingSearchResponseDtoList = bookingList.stream().map(booking -> booking.convertToSearchDto()).collect(Collectors.toList());
        int pageTotal = page.getTotalPages();

        BookingSearchResponsePagingDto bookingSearchResponsePagingDto = new BookingSearchResponsePagingDto(pageNo, pageSize, pageTotal, bookingSearchResponseDtoList);

        return bookingSearchResponsePagingDto;
    }
    //</editor-fold>

    //<editor-fold desc="Get Booking Detail By Id">
    public BookingSearchResponseDto findBookingByBookingId(int bookingId) throws Exception {
        if (bookingRepository.existsBookingByBookingId(bookingId)) {
            Booking booking = bookingRepository.findBookingByBookingId(bookingId);
            BookingSearchResponseDto bookingSearchResponseDto = booking.convertToSearchDto();
            return bookingSearchResponseDto;
        } else {
            throw new ValidationException(Constant.INVALID_BOOKING_ID);
        }
    }
    //</editor-fold>
}
