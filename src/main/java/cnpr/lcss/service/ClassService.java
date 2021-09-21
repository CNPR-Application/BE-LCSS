package cnpr.lcss.service;

import cnpr.lcss.dao.Class;
import cnpr.lcss.dao.*;
import cnpr.lcss.model.ClassDto;
import cnpr.lcss.model.ClassRequestDto;
import cnpr.lcss.model.ClassSearchDto;
import cnpr.lcss.model.ClassTeacherSearchDto;
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
        List<ClassDto> classDtoList = classList.getContent().stream().map(aClass -> aClass.convertToDto()).collect(Collectors.toList());

        for (ClassDto aClass : classDtoList) {
            aClass.setSubjectName(subjectRepository.findSubject_SubjectNameBySubjectId(aClass.getSubjectId()));
            aClass.setSubjectPrice(subjectRepository.findSubject_SubjectPriceBySubjectId(aClass.getSubjectId()));
            aClass.setBranchName(branchRepository.findBranch_BranchNameByBranchId(aClass.getBranchId()));
            String description = shiftRepository.findShift_DayOfWeekByShiftId(aClass.getShiftId())
                    + " (" + shiftRepository.findShift_TimeStartByShiftId(aClass.getShiftId())
                    + "-" + shiftRepository.findShift_TimeEndByShiftId(aClass.getShiftId()) + ")";
            aClass.setShiftDescription(description);
            if (aClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_WAITING) || aClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_CANCELED)) {
                aClass.setTeacherId(0);
                aClass.setTeacherName(null);
                int numberOfStudent = bookingRepository.countBookingByaClass_ClassId(aClass.getClassId());
                aClass.setNumberOfStudent(numberOfStudent);
            } else {
                // CLASS_STATUS: STUDYING || FINISHED
                List<Session> sessionList = sessionRepository.findSessionByaClass_ClassId(aClass.getClassId());
                Teacher teacher = sessionList.get(0).getTeacher();
                aClass.setTeacherId(teacher.getTeacherId());
                aClass.setTeacherName(teacher.getAccount().getName());
                int numberOfStudent = studentInClassRepository.countStudentInClassByAClass_ClassId(aClass.getClassId());
                aClass.setNumberOfStudent(numberOfStudent);
            }
            Room room = roomRepository.findByRoomId(aClass.getRoomId());
            aClass.setRoomName(room.getRoomName());
            aClass.setRoomId(room.getRoomId());
            aClass.setManagerId(aClass.getManagerId());
            aClass.setManagerUsername(aClass.getManagerUsername());
        }
        return classDtoList;
    }
    //</editor-fold>

    //<editor-fold desc="Convert CN to 1">
    private String[] convertDowToInteger(String[] daysOfWeek) {
        String[] daysOfWeekCopy = new String[daysOfWeek.length];
        // Copy the old one to the new one
        System.arraycopy(daysOfWeek, 0, daysOfWeekCopy, 0, daysOfWeek.length);
        // Replace the last element from "CN" to "1"
        if (daysOfWeekCopy[daysOfWeek.length - 1] == "CN") {
            daysOfWeekCopy[daysOfWeek.length - 1] = "1";
        }
        // Append
        daysOfWeek = daysOfWeekCopy;
        return daysOfWeek;
    }
    //</editor-fold>

    //<editor-fold desc="Is Days In Shift">
    private boolean isDaysInShift(String[] daysOfWeek, Calendar calendar) {
        return Arrays.stream(convertDowToInteger(daysOfWeek))
                .anyMatch(dayOfWeek -> calendar.get(Calendar.DAY_OF_WEEK) == Integer.valueOf(dayOfWeek));
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
         * CASE 6: Shift ID != 0 && Status != 0
         * CASE 7: Status != 0
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
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 2
            if (branchId != 0 && subjectId != 0 && shiftId != 0 && (status.isEmpty() || status == null)) {
                classList = classRepository.findByBranch_BranchIdAndSubject_SubjectIdAndShift_ShiftId(branchId, subjectId, shiftId, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 3
            if (branchId != 0 && subjectId != 0 && shiftId != 0 && !status.isEmpty() && status != null) {
                classList = classRepository.findByBranch_BranchIdAndSubject_SubjectIdAndShift_ShiftIdAndStatusContainingIgnoreCase(branchId, subjectId, shiftId, status, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 4
            if (branchId != 0 && subjectId != 0 && shiftId == 0 && !status.isEmpty() && status != null) {
                classList = classRepository.findByBranch_BranchIdAndSubject_SubjectIdAndStatusContainingAllIgnoreCase(branchId, subjectId, status, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 5
            if (branchId != 0 && subjectId == 0 && shiftId != 0 && (status.isEmpty() || status == null)) {
                classList = classRepository.findByBranch_BranchIdAndShift_ShiftId(branchId, shiftId, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 6
            if (branchId != 0 && subjectId == 0 && shiftId != 0 && !status.isEmpty() && status != null) {
                classList = classRepository.findByBranch_BranchIdAndShift_ShiftIdAndStatusContainingAllIgnoreCase(branchId, shiftId, status, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 7
            if (branchId != 0 && subjectId == 0 && shiftId == 0 && !status.isEmpty() && status != null) {
                classList = classRepository.findByBranch_BranchIdAndStatusContainingAllIgnoreCase(branchId, status, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }
            // CASE 8
            if (branchId != 0 && subjectId == 0 && shiftId == 0 && (status.isEmpty() || status == null)) {
                classList = classRepository.findByBranch_BranchId(branchId, pageable);
                pageTotal = classList.getTotalPages();
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", autoMapping(classList));
            }

            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="9.02-Get-all-class-by-branchId-status">
    public ResponseEntity<?> searchAllClassByBranchIdAndStatusPaging(int branchId, String status, int pageNo, int pageSize) throws Exception {
        try {
            if (branchRepository.existsBranchByBranchId(branchId)) {
                Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

                HashMap<String, Object> mapObj = new LinkedHashMap();
                Page<Class> classList = classRepository.findClassByBranch_BranchIdAndStatus(branchId, status, pageable);
                List<ClassDto> classDtoList = classList.getContent().stream().map(aClass -> aClass.convertToDto()).collect(Collectors.toList());
                int pageTotal = classList.getTotalPages();

                for (ClassDto aClass : classDtoList) {
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
                        //number of student
                        int numberOfStudent = bookingRepository.countBookingByaClass_ClassId(aClass.getClassId());
                        // int numberOfStudent = studentInClassRepository.countStudentInClassByAClass_ClassId(aClass.getClassId());
                        aClass.setNumberOfStudent(numberOfStudent);
                    } else {
                        //STATUS: studying và finished
                        //get list session
                        List<Session> sessionList = sessionRepository.findSessionByaClass_ClassId(aClass.getClassId());
                        //get teacher
                        Teacher teacher = sessionList.get(0).getTeacher();
                        aClass.setTeacherId(teacher.getTeacherId());
                        aClass.setTeacherName(teacher.getAccount().getName());
                        //number of student
                        int numberOfStudent = studentInClassRepository.countStudentInClassByAClass_ClassId(aClass.getClassId());
                        aClass.setNumberOfStudent(numberOfStudent);
                    }
                    //ROOM
                    //find room by ID
                    Room room = roomRepository.findByRoomId(aClass.getRoomId());
                    //room name and ID
                    aClass.setRoomName(room.getRoomName());
                    aClass.setRoomId(room.getRoomId());
                }
                mapObj.put("pageNo", pageNo);
                mapObj.put("pageSize", pageSize);
                mapObj.put("pageTotal", pageTotal);
                mapObj.put("classList", classDtoList);
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
                if (status.matches("studying") || status.matches("finished")) {
                    //get a list student in class by student Id
                    List<StudentInClass> studentInClassList = studentInClassRepository.findStudentInClassByStudent_Id(student.getId());
                    //get a classIDList by Student In Class list
                    for (StudentInClass studentInClass : studentInClassList) {
                        list.add(studentInClass.getAClass().getClassId());
                    }
                }
                //with status: waiting and canceled
                if (status.matches("waiting") || status.matches("canceled")) {
                    //get booking list by student ID
                    List<Booking> bookingList = bookingRepository.findBookingByStudent_Id(student.getId());
                    //get a class ID list by booking list
                    for (Booking booking : bookingList) {
                        list.add(booking.getAClass().getClassId());
                    }
                }
                //Get classes with CLASSID LIST and STATUS
                Page<Class> classList = classRepository.findClassByClassIdIsInAndStatusOrderByOpeningDateDesc(list, status, pageable);
                List<ClassSearchDto> classSearchDtoList = classList.getContent().stream().map(aClass -> aClass.convertToSearchDto()).collect(Collectors.toList());
                int pageTotal = classList.getTotalPages();
                for (ClassSearchDto aClass : classSearchDtoList) {
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
                        //STATUS: studying và finished
                        //get list session
                        List<Session> sessionList = sessionRepository.findSessionByaClass_ClassId(aClass.getClassId());
                        //get teacher
                        Teacher teacher = sessionList.get(0).getTeacher();
                        aClass.setTeacherId(teacher.getTeacherId());
                        aClass.setTeacherName(teacher.getAccount().getName());
                    }
                    //ROOM
                    //find room by ID
                    Room room = roomRepository.findByRoomId(aClass.getRoomId());
                    //room name and ID
                    aClass.setRoomName(room.getRoomName());
                    aClass.setRoomId(room.getRoomId());
                }
                mapObj.put("pageNo", pageNo);
                mapObj.put("pageSize", pageSize);
                mapObj.put("pageTotal", pageTotal);
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
                //with status: studying and finished
                if (status.matches("studying") || status.matches("finished")) {
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
                    Room room = roomRepository.findByRoomId(aClass.getRoomId());
                    //room name and ID
                    aClass.setRoomName(room.getRoomName());
                    aClass.setRoomId(room.getRoomId());
                }
                mapObj.put("pageNo", pageNo);
                mapObj.put("pageSize", pageSize);
                mapObj.put("pageTotal", pageTotal);
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
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(Constant.TIMEZONE));
            Date today = calendar.getTime();
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
            if (insClass.getOpeningDate() != null && insClass.getOpeningDate().compareTo(today) >= 0) {
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

            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            mapObj.put("classId", classId);

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
            // Get RequestBody
            String className = (String) reqBody.get("className");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String reqDate = (String) reqBody.get("openingDate");
            Date openingDate = sdf.parse(reqDate);
            Integer roomId = (Integer) reqBody.get("roomId");
            String status = (String) reqBody.get("status");

            // Find Class by Class ID
            Class editClass = classRepository.findClassByClassId(classId);

            /**
             * If status = waiting
             * ➞ Edit: className, openingDate, status, roomNo
             * If status != waiting
             * ➞ Edit: status
             */
            // Status = waiting
            if (editClass.getStatus().equalsIgnoreCase(Constant.CLASS_STATUS_WAITING)) {
                // Class Name
                if (className != null && !className.isEmpty() && !className.equals(editClass.getClassName())) {
                    editClass.setClassName(className);
                }
                // Opening Date
                try {
                    if (openingDate != null && openingDate.compareTo(editClass.getOpeningDate()) >= 0) {
                        // Check whether Opening Date is a day in Shift
                        // Sunday = 0
                        int openingDayOfWeek = openingDate.getDay() + 1;
                        Shift shift = shiftRepository.findShiftByShiftId(editClass.getShift().getShiftId());
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
        int roomId = (int) reqBody.get("roomId");
        int teacherId = (int) reqBody.get("teacherId");
        int classId = (int) reqBody.get("classId");
        String creator = (String) reqBody.get("creator");
        List<Integer> bookingIdList = (List<Integer>) reqBody.get("bookingIdList");

        try {
            Teacher teacher = teacherRepository.findByTeacherId(teacherId);
            Room room = roomRepository.findByRoomId(roomId);
            Class activateClass = classRepository.findClassByClassId(classId);

            try {
                for (int bookingId : bookingIdList) {
                    // Change Booking Status: PAID → PROCESSED
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
                throw new Exception(Constant.ERROR_SAVE_STUDENT_IN_CLASS);
            }

            // Creator (aka Staff)
            // Check whether “creator” (request body) matches creator (tbl Class).
            // If not → update “creator” to the latest value.
            String classCreator = activateClass.getStaff().getAccount().getUsername();
            try {
                if (!creator.equals(classCreator)) {
                    classCreator = creator;
                }
                activateClass.setStaff(staffRepository.findByAccount_Username(classCreator));
                classRepository.save(activateClass);
            } catch (Exception e) {
                throw new Exception(Constant.INVALID_CLASS_CREATOR);
            }

            // Find Shift by Class ID in Class
            Shift shift = activateClass.getShift();

            // Create Session
            int numberOfSlot = activateClass.getSlot();
            String[] daysOfWeek = shift.getDayOfWeek().split("-");
            daysOfWeek = convertDowToInteger(daysOfWeek);
            String[] timeStart = shift.getTimeStart().split(":");
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
                throw new Exception(Constant.ERROR_GENERATE_SESSIONS);
            }

            // Get Student List by Class ID in Student In Class
            List<StudentInClass> studentInClassList = studentInClassRepository.findStudentsByClassId(activateClass.getClassId());

            // Insert sessions to each student in student list in Attendance
            try {
                for (StudentInClass studentInClass : studentInClassList) {
                    for (Session session : sessionList) {
                        Attendance attendance = new Attendance();
                        attendance.setSession(session);
                        attendance.setStatus(Constant.ATTENDANCE_STATUS_NOT_YET);
                        attendance.setCheckingDate(session.getStartTime());
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
            activateClass.setStatus(Constant.CLASS_STATUS_STUDYING);
            classRepository.save(activateClass);

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}