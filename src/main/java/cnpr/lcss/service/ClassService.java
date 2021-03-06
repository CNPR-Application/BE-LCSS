package cnpr.lcss.service;

import cnpr.lcss.dao.Class;
import cnpr.lcss.dao.*;
import cnpr.lcss.model.*;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClassService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    ClassRepository classRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    StudentInClassRepository studentInClassRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    StudentRepository studentRepository;

    //<editor-fold desc="Auto Mapping">
    public List<ClassDto> autoMapping(Page<Class> classList) {
        List<ClassDto> classDtoList = classList.getContent().stream().map(aClass -> aClass.convertToClassDto()).collect(Collectors.toList());

        for (ClassDto aClass : classDtoList) {
            if (aClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_WAITING) || aClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_CANCELED)) {
                aClass.setTeacherId(0);
                aClass.setTeacherUsername(null);
                aClass.setTeacherName(null);
                int numberOfStudent = (int) bookingRepository.countWaitingBookingByClassIdAndStatusIsPaid(aClass.getClassId(), Constant.BOOKING_STATUS_PAID);
                aClass.setNumberOfStudent(numberOfStudent);
            } else {
                // CLASS_STATUS: STUDYING || FINISHED
                List<Session> sessionList = sessionRepository.findSessionByaClass_ClassId(aClass.getClassId());
                Teacher teacher = sessionList.get(0).getTeacher();
                aClass.setTeacherId(teacher.getTeacherId());
                aClass.setTeacherUsername(teacher.getAccount().getUsername());
                aClass.setTeacherName(teacher.getAccount().getName());
                int numberOfStudent = studentInClassRepository.countStudentInClassByAClass_ClassId(aClass.getClassId());
                aClass.setNumberOfStudent(numberOfStudent);
            }
            aClass.setManagerId(aClass.getManagerId());
            aClass.setManagerUsername(aClass.getManagerUsername());
        }
        return classDtoList;
    }
    //</editor-fold>

    //<editor-fold desc="Convert CN to 1">
    public String[] convertDowToInteger(String[] daysOfWeek) {
        String[] daysOfWeekCopy = new String[daysOfWeek.length];
        // Copy the old one to the new one
        System.arraycopy(daysOfWeek, 0, daysOfWeekCopy, 0, daysOfWeek.length);
        // Replace the last element from "CN" to "1"
        if (daysOfWeekCopy[daysOfWeek.length - 1].equalsIgnoreCase("CN")) {
            daysOfWeekCopy[daysOfWeek.length - 1] = "1";
        }
        // Append
        daysOfWeek = daysOfWeekCopy;
        return daysOfWeek;
    }
    //</editor-fold>

    //<editor-fold desc="Is Days In Shift">
    public boolean isDaysInShift(String[] daysOfWeek, Calendar calendar) {
        return Arrays.stream(convertDowToInteger(daysOfWeek))
                .anyMatch(dayOfWeek -> calendar.get(Calendar.DAY_OF_WEEK) == Integer.valueOf(dayOfWeek));
    }
    //</editor-fold>

    //<editor-fold desc="Create a new Class">
    public Integer createANewClass(ClassRequestDto insClass) throws Exception {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.of(Constant.TIMEZONE));
        Class newClass = new Class();

        newClass.setClassName(insClass.getClassName());
        // Default Status for new Class - "waiting"
        newClass.setStatus(Constant.CLASS_STATUS_WAITING);
        newClass.setBranch(branchRepository.findByBranchId(insClass.getBranchId()));
        newClass.setSubject(subjectRepository.findBySubjectId(insClass.getSubjectId()));
        // Slot of Class = Slot of inserted Subject
        newClass.setSlot(subjectRepository.findSlotBySubjectId(insClass.getSubjectId()));
        /**
         * Check compatibility of subject's slot per week & shift's day of week
         * If (slot per week) of (insert subject) = 2 => (day of week) of (shift) = 2
         * Else if (slot per week) of (insert subject) = 3 => (day of week) of (shift) = 3
         * Else throw new Exception
         */
        int subject_slotPerWeek = subjectRepository.findSlotPerWeekBySubjectId(insClass.getSubjectId());
        String shift_dayOfWeek = shiftRepository.findShift_DayOfWeekByShiftId(insClass.getShiftId());
        if ((subject_slotPerWeek == 2 && shift_dayOfWeek.matches(Constant.TWO_DAYS_OF_WEEK_PATTERN))
                || subject_slotPerWeek == 3 && shift_dayOfWeek.matches(Constant.THREE_DAYS_OF_WEEK_PATTERN)) {
            newClass.setShift(shiftRepository.findShiftByShiftId(insClass.getShiftId()));
        } else {
            throw new ValidationException(Constant.INVALID_SLOT_PER_WEEK_AND_DAY_OF_WEEK);
        }
        // Check whether Opening Date is a day in Shift
        if (insClass.getOpeningDate() != null
                && !ZonedDateTime.ofInstant(insClass.getOpeningDate().toInstant(), ZoneId.of(Constant.TIMEZONE)).isBefore(today)) {
            Date openingDate = insClass.getOpeningDate();
            // Sunday = 0
            int openingDayOfWeek = openingDate.getDay() + 1;
            Shift shift = shiftRepository.findShiftByShiftId(insClass.getShiftId());
            String[] shiftDaysOfWeek = shift.getDayOfWeek().split("-");
            shiftDaysOfWeek = convertDowToInteger(shiftDaysOfWeek);
            boolean coincidence = false;
            for (String day : shiftDaysOfWeek) {
                if (day.equalsIgnoreCase(Integer.toString(openingDayOfWeek))) {
                    coincidence = true;
                }
            }
            if (!coincidence) {
                throw new ValidationException(Constant.INVALID_OPENING_DAY_VS_DAY_IN_SHIFT);
            }
            // Set time start as same as time start of shift
            openingDate.setHours(Integer.parseInt(shiftRepository.findShift_TimeStartByShiftId(insClass.getShiftId()).substring(0, 1)));
            newClass.setOpeningDate(openingDate);
        } else {
            throw new ValidationException(Constant.INVALID_OPENING_DATE);
        }
        // Creator is Account-Username
        newClass.setStaff(staffRepository.findByAccount_Username(insClass.getCreator()));
        newClass.setRoom(null);

        // Get Booking ID
        int classId;
        try {
            Class aClass = classRepository.save(newClass);
            classId = aClass.getClassId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(Constant.ERROR_GET_CLASS_ID);
        }
        return classId;
    }
    //</editor-fold>

    //<editor-fold desc="9.01-search-class-by-subject-id-shift-id-status-paging">
    public ResponseEntity<?> searchAllClassByBranchIdAndSubjectIdAndShiftIdAndStatusPaging(int branchId, int subjectId, int shiftId, String status, int pageNo, int pageSize) throws Exception {
        HashMap<String, Object> mapObj = new LinkedHashMap();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        /**
         * 8 CASES:
         * (Branch ID is MUST EXIST)
         * CASE 1: Subject ID != 0
         * CASE 2: Subject ID != 0 && Shift ID != 0
         * CASE 3: Subject ID != 0 && Shift ID != 0 && Status != null/empty
         * CASE 4: Subject ID != 0 && Status != null/empty
         * CASE 5: Shift ID != 0
         * CASE 6: Shift ID != 0 && Status != null/empty
         * CASE 7: Status != null/empty
         * CASE 8: All = 0
         */
        try {
            Page<Class> classList;
            int pageTotal;
            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);

            // CASE 1
            if (branchId != 0 && subjectId != 0 && shiftId == 0 && (status.isEmpty() || status == null)) {
                classList = classRepository.findByBranch_BranchIdAndSubject_SubjectId(branchId, subjectId, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("totalPage", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 2
            if (branchId != 0 && subjectId != 0 && shiftId != 0 && (status.isEmpty() || status == null)) {
                classList = classRepository.findByBranch_BranchIdAndSubject_SubjectIdAndShift_ShiftId(branchId, subjectId, shiftId, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("totalPage", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 3
            if (branchId != 0 && subjectId != 0 && shiftId != 0 && !status.isEmpty() && status != null) {
                classList = classRepository.findByBranch_BranchIdAndSubject_SubjectIdAndShift_ShiftIdAndStatusContainingIgnoreCase(branchId, subjectId, shiftId, status, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("totalPage", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 4
            if (branchId != 0 && subjectId != 0 && shiftId == 0 && !status.isEmpty() && status != null) {
                classList = classRepository.findByBranch_BranchIdAndSubject_SubjectIdAndStatusContainingAllIgnoreCase(branchId, subjectId, status, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("totalPage", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 5
            if (branchId != 0 && subjectId == 0 && shiftId != 0 && (status.isEmpty() || status == null)) {
                classList = classRepository.findByBranch_BranchIdAndShift_ShiftId(branchId, shiftId, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("totalPage", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 6
            if (branchId != 0 && subjectId == 0 && shiftId != 0 && !status.isEmpty() && status != null) {
                classList = classRepository.findByBranch_BranchIdAndShift_ShiftIdAndStatusContainingAllIgnoreCase(branchId, shiftId, status, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("totalPage", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 7
            if (branchId != 0 && subjectId == 0 && shiftId == 0 && !status.isEmpty() && status != null) {
                classList = classRepository.findByBranch_BranchIdAndStatusContainingAllIgnoreCase(branchId, status, pageable);
                mapObj.put("totalPage", classList.getTotalPages());
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 8
            if (branchId != 0 && subjectId == 0 && shiftId == 0 && (status.isEmpty() || status == null)) {
                classList = classRepository.findByBranch_BranchId(branchId, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("totalPage", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }

            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>xxxxxxxx

    //<editor-fold desc="9.02-Get-all-class-by-branchId-status">
    public ResponseEntity<?> searchAllClassByBranchIdAndStatusPaging(int branchId, String status, int pageNo, int pageSize) throws Exception {
        try {
            if (branchRepository.existsBranchByBranchId(branchId)) {
                Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

                HashMap<String, Object> mapObj = new LinkedHashMap();
                Page<Class> classList = classRepository.findClassByBranch_BranchIdAndStatus(branchId, status, pageable);
                mapObj.put("pageNo", pageNo);
                mapObj.put("pageSize", pageSize);
                mapObj.put("totalPage", classList.getTotalPages());
                mapObj.put("classList", autoMapping(classList));
                return ResponseEntity.ok(mapObj);
            } else {
                throw new ValidationException(Constant.INVALID_BRANCH_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.03-search-class-of-student-and-teacher-by-username-and-status">
    public ResponseEntity<?> searchClassByUsernameAndStatusPaging(String username, String status, int pageNo, int pageSize) throws Exception {
        try {
            if (accountRepository.existsByUsername(username)) {
                Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
                HashMap<String, Object> mapObj = new LinkedHashMap();
                //get a student by username
                Student student = studentRepository.findByStudent_StudentUsername(username);
                //initial a arraylist to store classIDs
                List<Integer> list = new ArrayList();
                //with status: studying and finished
                if (status.equalsIgnoreCase(Constant.CLASS_STATUS_STUDYING) || status.equalsIgnoreCase(Constant.CLASS_STATUS_FINISHED)) {
                    //get a list student in class by student Id
                    List<StudentInClass> studentInClassList = studentInClassRepository.findStudentInClassByStudent_Id(student.getId());
                    //get a classIDList by Student In Class list
                    for (StudentInClass studentInClass : studentInClassList) {
                        list.add(studentInClass.getAClass().getClassId());
                    }
                }
                //with status: waiting and canceled
                if (status.equalsIgnoreCase(Constant.CLASS_STATUS_WAITING) || status.equalsIgnoreCase(Constant.CLASS_STATUS_CANCELED)) {
                    //get booking list by student ID
                    List<Booking> bookingList = bookingRepository.findBookingByStudent_Id(student.getId());
                    //get a class ID list by booking list
                    for (Booking booking : bookingList) {
                        list.add(booking.getAClass().getClassId());
                    }
                }
                //Get classes with CLASSID LIST and STATUS
                Page<Class> classList = classRepository.findClassByClassIdIsInAndStatusOrderByOpeningDateDesc(list, status, pageable);
                List<ClassStudentSearchDto> classStudentSearchDtoList = classList.getContent().stream().map(aClass -> aClass.convertToStudentSearchDto()).collect(Collectors.toList());
                int pageTotal = classList.getTotalPages();
                for (ClassStudentSearchDto aClass : classStudentSearchDtoList) {
                    // Subject Name
                    aClass.setSubjectName(subjectRepository.findSubject_SubjectNameBySubjectId(aClass.getSubjectId()));
                    // Branch Name
                    aClass.setBranchName(branchRepository.findBranch_BranchNameByBranchId(aClass.getBranchId()));
                    // Shift Description
                    String description = shiftRepository.findShift_DayOfWeekByShiftId(aClass.getShiftId())
                            + " (" + shiftRepository.findShift_TimeStartByShiftId(aClass.getShiftId())
                            + "-" + shiftRepository.findShift_TimeEndByShiftId(aClass.getShiftId()) + ")";
                    aClass.setShiftDescription(description);
                    //STATUS: waiting and canceled
                    // Teacher is no need to query if status are WAITING OR CANCELED
                    if (aClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_WAITING) || aClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_CANCELED)) {
                        aClass.setTeacherId(0);
                        aClass.setTeacherName(null);
                    } else {
                        //STATUS: studying v?? finished
                        //get list session
                        List<Session> sessionList = sessionRepository.findSessionByaClass_ClassId(aClass.getClassId());
                        //get teacher
                        Teacher teacher = sessionList.get(0).getTeacher();
                        aClass.setTeacherId(teacher.getTeacherId());
                        aClass.setTeacherName(teacher.getAccount().getName());

                        //Get field suspend
                        StudentInClass studentInClass = studentInClassRepository.findByStudent_IdAndAClass_ClassId(student.getId(), aClass.getClassId());
                        if (studentInClass.getSuspend() == null) {
                            aClass.setSuspend(null);
                        } else {
                            aClass.setSuspend(studentInClass.getSuspend());
                        }
                        aClass.setStudentInClassId(studentInClass.getStudentInClassId());
                    }
                    //ROOM
                    //find room by ID with class status not equal WAITING
                    if (!aClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_WAITING)) {
                        Room room = roomRepository.findByRoomId(aClass.getRoomId());
                        //room name and ID
                        aClass.setRoomName(room.getRoomName());
                        aClass.setRoomId(room.getRoomId());
                    }

                }
                mapObj.put("pageNo", pageNo);
                mapObj.put("pageSize", pageSize);
                mapObj.put("totalPage", pageTotal);
                mapObj.put("classList", classStudentSearchDtoList);
                return ResponseEntity.ok(mapObj);
            } else {
                throw new ValidationException(Constant.INVALID_USERNAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.04-search-class-detail-by-class-id">
    public ResponseEntity<?> searchClassDetailByClassId(Integer classId) throws Exception {
        try {
            if (!classRepository.existsById(classId)) {
                throw new Exception(Constant.INVALID_CLASS_ID);
            } else {
                Class aClass = classRepository.findClassByClassId(classId);
                HashMap<String, Object> mapObj = new LinkedHashMap<>();
                mapObj.put("classId", aClass.getClassId());
                mapObj.put("className", aClass.getClassName());
                mapObj.put("openingDate", aClass.getOpeningDate());
                mapObj.put("status", aClass.getStatus());
                mapObj.put("slot", aClass.getSlot());
                mapObj.put("subjectId", aClass.getSubject().getSubjectId());
                mapObj.put("subjectName", aClass.getSubject().getSubjectName());
                mapObj.put("branchId", aClass.getBranch().getBranchId());
                mapObj.put("branchName", aClass.getBranch().getBranchName());
                mapObj.put("shiftId", aClass.getShift().getShiftId());
                mapObj.put("shiftDescription", shiftRepository.findShift_DayOfWeekByShiftId(aClass.getShift().getShiftId())
                        + " (" + shiftRepository.findShift_TimeStartByShiftId(aClass.getShift().getShiftId())
                        + "-" + shiftRepository.findShift_TimeEndByShiftId(aClass.getShift().getShiftId()) + ")");
                mapObj.put("teacherId", teacherRepository.findTeacherIdByClassId(aClass.getClassId()));
                mapObj.put("teacherName", teacherRepository.findTeacherNameByClassId(aClass.getClassId()));
                mapObj.put("roomNo", roomRepository.findRoomIdByClassId(aClass.getClassId()));
                int numberofStudent = 0;
                if (aClass.getStatus().matches(Constant.CLASS_STATUS_STUDYING) || aClass.getStatus().matches(Constant.CLASS_STATUS_FINISHED)) {
                    numberofStudent = studentInClassRepository.countStudentInClassByAClass_ClassId(aClass.getClassId());
                }
                if (aClass.getStatus().matches(Constant.CLASS_STATUS_WAITING) || aClass.getStatus().matches(Constant.CLASS_STATUS_CANCELED)) {
                    numberofStudent = (int) bookingRepository.countWaitingBookingByClassIdAndStatusIsPaid(aClass.getClassId(), Constant.BOOKING_STATUS_PAID);

                }
                mapObj.put("numberOfStudent", numberofStudent);
                return ResponseEntity.status(HttpStatus.OK).body(mapObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.05_search_class_of_teacher_by_username">
    public ResponseEntity<?> searchClassByTeacherUsernameAndStatusPaging(String username, String status, int pageNo, int pageSize) throws Exception {
        try {
            if (accountRepository.existsByUsername(username)) {
                Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
                HashMap<String, Object> mapObj = new LinkedHashMap();
                //get a teacher by username
                Teacher teacher = teacherRepository.findTeacherByAccount_Username(username);
                //initial a arraylist to store classIDs
                List<Integer> list = new ArrayList();
                /**
                 * ONLY STATUS: STUDYING AND FINISHED
                 * Classes which status: Waiting dont have teacher yet, then it can be canceled (change status)
                 */
                if (status.matches(Constant.CLASS_STATUS_STUDYING) || status.matches(Constant.CLASS_STATUS_FINISHED)) {
                    //get a list student in class by student Id
                    List<Session> sessionList = sessionRepository.findSessionByTeacher_TeacherId(teacher.getTeacherId());
                    //get a classIDList by Student In Class list
                    for (Session session : sessionList) {
                        list.add(session.getAClass().getClassId());
                    }
                }
                //Get classes with CLASSID LIST and STATUS
                Page<Class> classList = classRepository.findClassByClassIdIsInAndStatusOrderByOpeningDateDesc(list, status, pageable);
                List<ClassTeacherSearchDto> classSearchDtoList = classList.getContent().stream().map(aClass -> aClass.convertToTeacherSearchDto()).collect(Collectors.toList());
                int pageTotal = classList.getTotalPages();
                for (ClassTeacherSearchDto aClass : classSearchDtoList) {
                    // Subject Name
                    aClass.setSubjectName(subjectRepository.findSubject_SubjectNameBySubjectId(aClass.getSubjectId()));
                    // Branch Name
                    aClass.setBranchName(branchRepository.findBranch_BranchNameByBranchId(aClass.getBranchId()));
                    // Shift Description
                    String description = shiftRepository.findShift_DayOfWeekByShiftId(aClass.getShiftId())
                            + " (" + shiftRepository.findShift_TimeStartByShiftId(aClass.getShiftId())
                            + "-" + shiftRepository.findShift_TimeEndByShiftId(aClass.getShiftId()) + ")";
                    aClass.setShiftDescription(description);
                    //ROOM
                    //find room by ID
                    if (!aClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_WAITING)) {
                        Room room = roomRepository.findByRoomId(aClass.getRoomId());
                        //room name and ID
                        aClass.setRoomName(room.getRoomName());
                        aClass.setRoomId(room.getRoomId());
                    }
                }
                mapObj.put("pageNo", pageNo);
                mapObj.put("pageSize", pageSize);
                mapObj.put("totalPage", pageTotal);
                mapObj.put("classList", classSearchDtoList);
                return ResponseEntity.ok(mapObj);
            } else {
                throw new ValidationException(Constant.INVALID_USERNAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.06-create-new-class">
    public ResponseEntity<?> createNewClass(ClassRequestDto insClass) throws Exception {
        try {
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            mapObj.put("classId", createANewClass(insClass));
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.07-edit-class">
    public ResponseEntity<?> editClass(int classId, Map<String, Object> reqBody) throws Exception {
        try {
            ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of(Constant.TIMEZONE));
            // Find Class by Class ID
            Class editClass = classRepository.findClassByClassId(classId);

            // Get RequestBody
            String className = (String) reqBody.get("className");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String reqDate = (String) reqBody.get("openingDate");
            Date openingDate;
            if (reqDate == null) {
                openingDate = editClass.getOpeningDate();
            } else {
                openingDate = sdf.parse(reqDate);
            }
            Integer roomId = (Integer) reqBody.get("roomId");
            String status = (String) reqBody.get("status");


            /**
             * If status = waiting
             * ??? Edit: className, openingDate, status, roomNo
             * If status != waiting
             * ??? Edit: status
             */
            // Status = waiting
            if (editClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_WAITING)) {
                // Class Name
                if (className != null && !className.isEmpty() && !className.equals(editClass.getClassName())) {
                    editClass.setClassName(className);
                }
                // Opening Date
                try {
                    if (openingDate != null
                            && (openingDate.compareTo(editClass.getOpeningDate()) >= 0)
                            && (openingDate.compareTo(Date.from(currentDate.toInstant())) >= 0)) {
                        // Check whether Opening Date is a day in Shift
                        // Sunday = 0
                        int openingDayOfWeek = openingDate.getDay() + 1;
                        Shift shift = shiftRepository.findShiftByShiftId(editClass.getShift().getShiftId());
                        String[] shiftDaysOfWeek = shift.getDayOfWeek().split(Constant.SYMBOL_HYPHEN);
                        shiftDaysOfWeek = convertDowToInteger(shiftDaysOfWeek);
                        boolean coincidence = false;
                        for (String day : shiftDaysOfWeek) {
                            if (day.equalsIgnoreCase(Integer.toString(openingDayOfWeek))) {
                                coincidence = true;
                            }
                        }
                        if (!coincidence) {
                            throw new ValidationException(Constant.INVALID_OPENING_DAY_VS_DAY_IN_SHIFT);
                        }
                        // Set time start same as time start of shift
                        openingDate.setHours(Integer.parseInt(shiftRepository.findShift_TimeStartByShiftId(editClass.getShift().getShiftId()).substring(0, 1)));
                        editClass.setOpeningDate(openingDate);
                    }
                } catch (Exception e) {
                    throw new ValidationException(Constant.INVALID_OPENING_DATE);
                }
                // Status
                if (status != null && !status.isEmpty() && !status.equals(editClass.getStatus())) {
                    editClass.setStatus(status);
                }
                // Room ID
                if (roomId != null && roomId != 0 && roomId != editClass.getRoom().getRoomId()) {
                    editClass.setRoom(roomRepository.findByRoomId(roomId));
                }
            } else {
                // Status
                if (status != null && !status.isEmpty() && !status.equals(editClass.getStatus())) {
                    editClass.setStatus(status);
                }
            }
            classRepository.save(editClass);
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.08-delete-class">
    public ResponseEntity<?> deleteClass(Integer classId, HashMap<String, Object> reqBody) throws Exception {
        try {
            if (!classRepository.existsById(classId)) {
                throw new Exception(Constant.INVALID_CLASS_ID);
            }
            Class delClass = classRepository.findClassByClassId(classId);
            String delReason = (String) reqBody.get("reason");
            if (delReason == null || delReason.isEmpty()) {
                throw new Exception(Constant.INVALID_DELETE_CLASS_REASON);
            }

            if (delClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_WAITING)
                    && bookingRepository.countBookingByClassId(classId) == 0) {
                delClass.setStatus(Constant.CLASS_STATUS_CANCELED);
                delClass.setCanceledReason(delReason.trim());
                classRepository.save(delClass);
            } else {
                throw new Exception(Constant.ERROR_DELETE_CLASS);
            }
            return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.09-get-classes-statistic">
    public ResponseEntity<?> getClassesStatistic(int branchId) throws Exception {
        /**
         * if branch id = 0 => find all branches by status
         * if branch id != 0 => check existence => find by branch id and status
         */
        HashMap<String, Object> mapObj = new LinkedHashMap<>();
        try {
            if (branchId == 0) {
                mapObj.put("waitingClass", classRepository.countByStatusAllIgnoreCase(Constant.CLASS_STATUS_WAITING));
                mapObj.put("studyingClass", classRepository.countByStatusAllIgnoreCase(Constant.CLASS_STATUS_STUDYING));
                mapObj.put("finishedClass", classRepository.countByStatusAllIgnoreCase(Constant.CLASS_STATUS_FINISHED));
                mapObj.put("canceledClass", classRepository.countByStatusAllIgnoreCase(Constant.CLASS_STATUS_CANCELED));
            } else if (branchId != 0) {
                if (branchRepository.existsBranchByBranchId(branchId) && branchRepository.findIsAvailableByBranchId(branchId)) {
                    mapObj.put("waitingClass", classRepository.countDistinctByBranch_BranchIdAndStatusAllIgnoreCase(branchId, Constant.CLASS_STATUS_WAITING));
                    mapObj.put("studyingClass", classRepository.countDistinctByBranch_BranchIdAndStatusAllIgnoreCase(branchId, Constant.CLASS_STATUS_STUDYING));
                    mapObj.put("finishedClass", classRepository.countDistinctByBranch_BranchIdAndStatusAllIgnoreCase(branchId, Constant.CLASS_STATUS_FINISHED));
                    mapObj.put("canceledClass", classRepository.countDistinctByBranch_BranchIdAndStatusAllIgnoreCase(branchId, Constant.CLASS_STATUS_CANCELED));
                } else {
                    throw new Exception(Constant.INVALID_BRANCH_ID);
                }
            }
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.10-activate-class">
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> activateClass(Map<String, Object> reqBody) throws Exception {
        String className = reqBody.get("className").toString();
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_PATTERN);
        Date openingDate = sdf.parse(reqBody.get("openingDate").toString());
        Integer branchId = Integer.parseInt(reqBody.get("branchId").toString());
        Integer subjectId = Integer.parseInt(reqBody.get("subjectId").toString());
        Integer shiftId = Integer.parseInt(reqBody.get("shiftId").toString());
        Integer roomId = Integer.parseInt(reqBody.get("roomId").toString());
        Integer teacherId = Integer.parseInt(reqBody.get("teacherId").toString());
        String creator = reqBody.get("creator").toString();
        List<Integer> bookingIdList = (List<Integer>) reqBody.get("bookingIdList");

        try {
            ClassRequestDto newClass = new ClassRequestDto();
            newClass.setClassName(className);
            newClass.setOpeningDate(openingDate);
            newClass.setBranchId(branchId);
            newClass.setSubjectId(subjectId);
            newClass.setShiftId(shiftId);
            newClass.setCreator(creator);
            Integer newClassId = createANewClass(newClass);

            Teacher teacher = teacherRepository.findByTeacherId(teacherId);
            Room room = roomRepository.findByRoomId(roomId);
            Class activateClass = classRepository.findClassByClassId(newClassId);

            try {
                for (int bookingId : bookingIdList) {
                    // Change Booking Status: PAID ??? PROCESSED
                    Booking currentBooking = bookingRepository.findBookingByBookingId(bookingId);
                    currentBooking.setStatus(Constant.BOOKING_STATUS_PROCESSED);
                    // Insert Student to Student In Class
                    Student currentStudent = currentBooking.getStudent();
                    StudentInClass newStudentInClass = new StudentInClass();
                    newStudentInClass.setAClass(activateClass);
                    newStudentInClass.setTeacherRating(0);
                    newStudentInClass.setSubjectRating(0);
                    newStudentInClass.setFeedback(null);
                    newStudentInClass.setAClass(activateClass);
                    newStudentInClass.setStudent(currentStudent);
                    studentInClassRepository.save(newStudentInClass);
                }
            } catch (Exception e) {
                e.printStackTrace();
                classRepository.delete(activateClass);
                throw new Exception(Constant.ERROR_SAVE_STUDENT_IN_CLASS);
            }

            // Creator (aka Staff)
            // Check whether ???creator??? (request body) matches creator (tbl Class).
            // If not ??? update ???creator??? to the latest value.
            String classCreator = activateClass.getStaff().getAccount().getUsername();
            try {
                if (!creator.equals(classCreator)) {
                    classCreator = creator;
                }
                activateClass.setStaff(staffRepository.findByAccount_Username(classCreator));
                classRepository.save(activateClass);
            } catch (Exception e) {
                classRepository.delete(activateClass);
                throw new Exception(Constant.INVALID_CLASS_CREATOR);
            }

            // Find Shift by Class ID in Class
            Shift shift = activateClass.getShift();

            // Create Session
            int numberOfSlot = activateClass.getSlot();
            String[] daysOfWeek = shift.getDayOfWeek().split(Constant.SYMBOL_HYPHEN);
            daysOfWeek = convertDowToInteger(daysOfWeek);
            String[] timeStart = shift.getTimeStart().split(Constant.SYMBOL_COLON);
            int totalSession = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(activateClass.getOpeningDate());
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeStart[0]));
            calendar.set(Calendar.MINUTE, Integer.valueOf(timeStart[1]));

            List<Date> dateList = new ArrayList<>();

            while (totalSession < numberOfSlot) {
                if (isDaysInShift(daysOfWeek, calendar)) {
                    totalSession++;
                    dateList.add(calendar.getTime());
                }
                calendar.add(Calendar.DATE, 1);
            }

            // Insert information to Session
            List<Session> sessionList;
            try {
                sessionList = new ArrayList<>();
                for (Date date : dateList) {
                    Session session = new Session();
                    session.setAClass(activateClass);
                    session.setTeacher(teacher);
                    session.setStartTime(date);
                    Date newDate = new Date();
                    newDate.setDate(date.getDate());
                    newDate.setTime(date.getTime() + activateClass.getShift().getDuration() * 60000);
                    session.setEndTime(newDate);
                    session.setRoom(room);
                    sessionList.add(session);
                }
                sessionRepository.saveAll(sessionList);
            } catch (Exception e) {
                e.printStackTrace();
                classRepository.delete(activateClass);
                throw new Exception(Constant.ERROR_GENERATE_SESSIONS);
            }

            // Get Student List by Class ID in Student In Class
            List<StudentInClass> studentInClassList = studentInClassRepository.findStudentsByClassId(activateClass.getClassId());

            // Insert sessions to each student in student list in Attendance
            try {
                for (StudentInClass studentInClass : studentInClassList) {
                    for (Session session : sessionList) {
                        Attendance attendance = new Attendance();
                        attendance.setStatus(Constant.ATTENDANCE_STATUS_NOT_YET);
                        attendance.setCheckingDate(session.getStartTime());
                        attendance.setIsReopen(Boolean.FALSE);
                        attendance.setClosingDate(null);
                        attendance.setReopenReason(null);
                        attendance.setSession(session);
                        attendance.setStudentInClass(studentInClass);
                        attendanceRepository.save(attendance);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                sessionRepository.deleteAll(sessionList);
                throw new Exception(Constant.ERROR_INSERT_TO_ATTENDANCE);
            }

            // Update Class information
            activateClass.setRoom(room);
            activateClass.setStatus(Constant.CLASS_STATUS_STUDYING);
            classRepository.save(activateClass);

            return ResponseEntity.ok(newClassId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.11-get-all-classes-has-not-got-feedback-from-student-by-student-username">
    public ResponseEntity<?> getAllClassesHasNotGotFeedbackFromStudentByStudentUsername(String studentUsername) throws Exception {
        /**
         * Find Classes that need Student feedback
         * Class Status = finished
         * Session - Teacher Rating = 0
         */
        try {
            int studentId = studentRepository.findStudentByAccount_Username(studentUsername).getId();
            List<ClassNeedsFeedbackDto> classList = classRepository.findClassesNeedFeedback(studentId, Constant.CLASS_STATUS_FINISHED)
                    .stream().map(aClass -> aClass.convertToClassNeedsFeedbackDto()).collect(Collectors.toList());
            for (ClassNeedsFeedbackDto dto : classList) {
                dto.setTeacherId(sessionRepository.findSessionByaClass_ClassId(dto.getClassId()).get(classList.size()).getTeacher().getTeacherId());
                dto.setStudentInClassId(studentInClassRepository.findByStudent_IdAndAClass_ClassId(studentId, dto.getClassId()).getStudentInClassId());
            }
            return ResponseEntity.status(HttpStatus.OK).body(classList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.13-class-static-teacher">
    public ResponseEntity<?> getTeacherClassesStatistic(String teacherUsername) throws Exception {
        HashMap<String, Object> mapObj = new LinkedHashMap<>();
        try {
            Account account = accountRepository.findOneByUsername(teacherUsername);
            List<Integer> list = new ArrayList();
            if (account != null) {
                List<Session> sessionList = sessionRepository.findSessionByTeacher_TeacherId(account.getTeacher().getTeacherId());
                for (Session session : sessionList) {
                    list.add(session.getAClass().getClassId());
                }
                //temporary set waiting class is 0
                mapObj.put("waitingClass", 0);
                mapObj.put("studyingClass", classRepository.countClassByClassIdIsInAndStatus(list, Constant.CLASS_STATUS_STUDYING));
                mapObj.put("finishedClass", classRepository.countClassByClassIdIsInAndStatus(list, Constant.CLASS_STATUS_FINISHED));
            } else {
                throw new Exception(Constant.INVALID_USERNAME);
            }
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.14-suspend-class">
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> suspendClass(Integer studentInClassId, HashMap<String, Object> reqBody) throws Exception {
        try {
            String insOpeningDate = (String) reqBody.get("openingDate");
            String type = (String) reqBody.get("type");
            Integer newClassId = Integer.parseInt(reqBody.get("newClassId").toString());
            String studentUsername = (String) reqBody.get("studentUsername");
            Integer branchId = Integer.parseInt(reqBody.get("branchId").toString());
            String status = (String) reqBody.get("status");
            String description = (String) reqBody.get("description");
            Integer subjectId = Integer.parseInt(reqBody.get("subjectId").toString());

            ZonedDateTime currentDate = ZonedDateTime.now();
            LocalDate localDate = LocalDate.parse(insOpeningDate, DateTimeFormatter.ofPattern(Constant.DATE_PATTERN));
            ZonedDateTime openingDate = localDate.atStartOfDay(ZoneId.of(Constant.TIMEZONE));
            if (currentDate.isAfter(openingDate.plusDays(7))) {
                throw new Exception(Constant.ERROR_SUSPEND_CLASS);
            } else {
                List<Attendance> attendanceList = attendanceRepository.
                        findAttendanceAfter(Date.from(currentDate.toInstant()), studentInClassId);
                for (Attendance att : attendanceList) {
                    attendanceRepository.delete(att);
                }

                Class newClass = classRepository.findClassByClassId(newClassId);
                Student student = studentRepository.findByStudent_StudentUsername(studentUsername);

                if (!type.equalsIgnoreCase("class") && !type.equalsIgnoreCase("booking")) {
                    throw new Exception(Constant.ERROR_SUSPEND_CLASS_TYPE);
                } else if (type.equalsIgnoreCase("class")) {
                    StudentInClass newSic = null;
                    try {
                        StudentInClass sic = new StudentInClass();
                        sic.setAClass(newClass);
                        sic.setStudent(student);
                        sic.setTeacherRating(0);
                        sic.setSubjectRating(0);
                        sic.setFeedback(null);
                        sic.setSuspend(Boolean.FALSE);
                        newSic = studentInClassRepository.saveAndFlush(sic);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        studentInClassRepository.delete(newSic);
                        throw new Exception(Constant.ERROR_SAVE_STUDENT_IN_CLASS);
                    }

                    List<Session> sessionList = sessionRepository.
                            findSessionByaClass_ClassId(newClassId);
                    try {
                        for (Session ses : sessionList) {
                            Attendance att = new Attendance();
                            if (currentDate.isAfter(ZonedDateTime.ofInstant(ses.getStartTime().toInstant(), ZoneId.of(Constant.TIMEZONE)))) {
                                att.setStatus(Constant.ATTENDANCE_STATUS_ABSENT);
                            } else {
                                att.setStatus(Constant.ATTENDANCE_STATUS_NOT_YET);
                            }
                            att.setCheckingDate(ses.getStartTime());
                            att.setIsReopen(Boolean.FALSE);
                            att.setClosingDate(null);
                            att.setReopenReason(null);
                            att.setSession(ses);
                            att.setStudentInClass(newSic);
                            attendanceRepository.save(att);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception(Constant.ERROR_INSERT_TO_ATTENDANCE);
                    }
                } else if (type.equalsIgnoreCase("booking")) {
                    Booking tmpBkn = null;
                    try {
                        Booking bkn = new Booking();
                        bkn.setPayingDate(Date.from(currentDate.toInstant()));
                        bkn.setSubjectId(subjectId);
                        bkn.setBranch(branchRepository.findByBranchId(branchId));
                        bkn.setStatus(Constant.BOOKING_STATUS_PAID);
                        bkn.setPayingPrice(subjectRepository.findSubjectPriceBySubjectId(subjectId));
                        bkn.setDescription(description);
                        bkn.setAClass(newClass);
                        bkn.setStudent(student);
                        tmpBkn = bookingRepository.saveAndFlush(bkn);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        bookingRepository.delete(tmpBkn);
                        throw new Exception(Constant.ERROR_INSERT_TO_BOOKING);
                    }
                }

                StudentInClass oldSic = studentInClassRepository.findSicBySicId(studentInClassId);
                oldSic.setSuspend(Boolean.TRUE);
                studentInClassRepository.save(oldSic);
            }
            return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.15-search-class-to-suspend">
    public ResponseEntity<?> getClassToSuspend(String status, float price, int branchId) throws Exception {
        try {
            if (!branchRepository.existsBranchByBranchId(branchId))
                throw new Exception(Constant.INVALID_BRANCH_ID);
            if (price < 0)
                throw new Exception(Constant.INVALID_SUBJECT_PRICE);
            if (status.matches(Constant.CLASS_STATUS_STUDYING) || status.matches(Constant.CLASS_STATUS_WAITING)) {
                List<ClassSearchToSuspend> classList = classRepository.findClassByStatusAndSubject_PriceAndBranch_BranchId(status, price, branchId).stream().map(aClass -> aClass.convertToSearchToSuspendDto()).collect(Collectors.toList());
                int numberofStudent = 0;
                for (ClassSearchToSuspend classSearchToSuspend : classList) {
                    if (classSearchToSuspend.getStatus().matches(Constant.CLASS_STATUS_STUDYING)) {
                        numberofStudent = studentInClassRepository.countStudentInClassByAClass_ClassId(classSearchToSuspend.getClassId());
                    }
                    if (classSearchToSuspend.getStatus().matches(Constant.CLASS_STATUS_WAITING)) {
                        numberofStudent = (int) bookingRepository.countWaitingBookingByClassIdAndStatusIsPaid(classSearchToSuspend.getClassId(), Constant.BOOKING_STATUS_PAID);
                    }
                    classSearchToSuspend.setNumberOfStudent(numberofStudent);
                }
                return ResponseEntity.status(HttpStatus.OK).body(classList);
            } else {
                throw new Exception(Constant.INVALID_CLASS_STATUS_NOT_WAITING_OR_STUDYING);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.16-get-class-suspend-true-of-student">
    public ResponseEntity<?> getClassSuspendIsTrueOfStudent(String studentUsername) throws Exception {
        try {
            Student student = studentRepository.findByStudent_StudentUsername(studentUsername);
            if (student == null) {
                throw new Exception(Constant.INVALID_USERNAME);
            } else {
                List<String> status = new ArrayList<>();
                status.add(Constant.CLASS_STATUS_STUDYING);
                status.add(Constant.CLASS_STATUS_FINISHED);
                List<StudentInClassSuspendIsTrueOfStudent> list = studentInClassRepository.findByaClass_StatusIsInAndStudent_IdAndAndSuspend(status, student.getId(), Boolean.TRUE).stream().map(studentInClass -> studentInClass.convertToSuspendSearchDto()).collect(Collectors.toList());
                return ResponseEntity.status(HttpStatus.OK).body(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}