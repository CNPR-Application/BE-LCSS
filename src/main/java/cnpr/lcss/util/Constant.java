package cnpr.lcss.util;

public class Constant {
    //<editor-fold desc="Account">
    /**
     * -----ACCOUNT-----
     */
    // Vietnamese Characters
    // Lower Case
    public static final String aLower = "à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ";
    public static final String aLowerAscii = "a";
    public static final String eLower = "è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ";
    public static final String eLowerAscii = "e";
    public static final String iLower = "ì|í|ị|ỉ|ĩ";
    public static final String iLowerAscii = "i";
    public static final String oLower = "ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ";
    public static final String oLowerAscii = "o";
    public static final String uLower = "ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ";
    public static final String uLowerAscii = "u";
    public static final String yLower = "ỳ|ý|ỵ|ỷ|ỹ";
    public static final String yLowerAscii = "y";
    public static final String dLower = "đ";
    public static final String dLowerAscii = "d";
    // Upper Case
    public static final String aUpper = "À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ";
    public static final String aUpperAscii = "A";
    public static final String eUpper = "È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ";
    public static final String eUpperAscii = "E";
    public static final String iUpper = "Ì|Í|Ị|Ỉ|Ĩ";
    public static final String iUpperAscii = "I";
    public static final String oUpper = "Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ";
    public static final String oUpperAscii = "O";
    public static final String uUpper = "Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ";
    public static final String uUpperAscii = "U";
    public static final String yUpper = "Ỳ|Ý|Ỵ|Ỷ|Ỹ";
    public static final String yUpperAscii = "Y";
    public static final String dUpper = "Đ";
    public static final String dUpperAscii = "D";
    // User Role
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_ADMIN_CODE = "ad";
    public static final String ROLE_MANAGER = "manager";
    public static final String ROLE_MANAGER_CODE = "ql";
    public static final String ROLE_STAFF = "staff";
    public static final String ROLE_STAFF_CODE = "nv";
    public static final String ROLE_STUDENT = "student";
    public static final String ROLE_STUDENT_CODE = "hs";
    public static final String ROLE_TEACHER = "teacher";
    public static final String ROLE_TEACHER_CODE = "gv";
    // Error Message
    public static final String ERROR_EMAIL_SENDING = "Sending email failed!";
    public static final String ERROR_GENERATE_PASSWORD = "Generate password failed!";
    public static final String ERROR_GENERATE_USERNAME = "Generate username failed!";
    public static final String INVALID_ADMIN_BIRTHDAY = "Admin must OLDER or EQUAL to 18!";
    public static final String INVALID_CHANGE_ROLE = "Role must be manager/staff!";
    public static final String INVALID_IS_AVAILABLE = "Account no longer active!";
    public static final String INVALID_MANAGER_BIRTHDAY = "Manager must older or EQUAL to 18!";
    public static final String INVALID_NEW_ROLE = "New role must be manager/staff!";
    public static final String INVALID_ROLE = "Invalid role!";
    public static final String INVALID_STAFF_BIRTHDAY = "Staff must OLDER or EQUAL to 18!";
    public static final String INVALID_STUDENT_BIRTHDAY = "Student must OLDER or EQUAL to 3!";
    public static final String INVALID_TEACHER_BIRTHDAY = "Teacher must OLDER or EQUAL to 15!";
    public static final String INVALID_TEACHER_EXP = "Null or empty experience!";
    public static final String INVALID_USERNAME = "Username not exist!";
    public static final String PASSWORD_NOT_MATCH = "Password not match!";
    //</editor-fold>

    //<editor-fold desc="Attendance">
    /**
     * -----ATTENDANCE-----
     */
    // Attendance Status
    public static final String ATTENDANCE_STATUS_ABSENT = "absent";
    public static final String ATTENDANCE_STATUS_NOT_YET = "not yet";
    public static final String ATTENDANCE_STATUS_PRESENT = "present";
    // Error Message
    public static final String ERROR_INSERT_TO_ATTENDANCE = "Error at insert Student to Attendance!";
    public static final String INVALID_ATTENDANCE_STATUS = "Invalid Attendance Status [absent | not yet | present]!";
    //</editor-fold>

    //<editor-fold desc="Booking">
    /**
     * -----BOOKING-----
     */
    // Booking Status
    public static final String BOOKING_STATUS_CANCELED = "canceled";
    public static final String BOOKING_STATUS_PAID = "paid";
    public static final String BOOKING_STATUS_PROCESSED = "processed";
    // Error Message
    public static final String INVALID_BOOKING_ID = "Invalid Booking ID!";
    public static final String INVALID_BOOKING_PAYING_PRICE = "Paying Price must be GREATER or EQUAL to Subject's Price!";
    public static final String INVALID_BOOKING_STATUS = "Booking Status must be paid/canceled!";
    public static final String ERROR_GET_BOOKING_ID = "Unable to get Booking ID!";
    //</editor-fold>

    //<editor-fold desc="Branch">
    /**
     * -----BRANCH-----
     */
    // Error Message
    public static final String INVALID_BRANCH_ID = "Branch ID not exist!";
    public static final String INVALID_BRANCH_AVAILABLE = "Branch not available!";
    public static final String DUPLICATE_BRANCH_ID = "Duplicate Branch ID!";
    public static final String DUPLICATE_BRANCH_NAME = "Duplicate Branch Name!";
    //</editor-fold>

    //<editor-fold desc="Class">
    /**
     * -----CLASS-----
     */
    // Class Status
    public static final String CLASS_STATUS_CANCELED = "canceled";
    public static final String CLASS_STATUS_FINISHED = "finished";
    public static final String CLASS_STATUS_STUDYING = "studying";
    public static final String CLASS_STATUS_WAITING = "waiting";
    // Pattern
    public static final String TWO_DAYS_OF_WEEK_PATTERN = "(((\\d)[-])+(\\d|[C][N])){1}";
    public static final String THREE_DAYS_OF_WEEK_PATTERN = "(((\\d)[-]){2})+(\\d|[C][N]){1}";
    // Error Message
    public static final String ERROR_GET_CLASS_ID = "Unable to get Class ID!";
    public static final String INVALID_CLASS_CREATOR = "Invalid Class Creator!";
    public static final String INVALID_CLASS_ID = "Class ID not exist!";
    public static final String INVALID_CLASS_NAME = "Null or empty class name!";
    public static final String INVALID_CLASS_STATUS_NOT_WAITING = "Class Status must be waiting!";
    public static final String INVALID_OPENING_DATE = "Null or invalid opening day!";
    public static final String INVALID_OPENING_DAY_VS_DAY_IN_SHIFT = "Opening Day must be a day in Shift!";
    public static final String INVALID_SLOT_PER_WEEK_AND_DAY_OF_WEEK = "Subject's slot per week incompatible with Shift's days of week!";
    //</editor-fold>

    //<editor-fold desc="Curriculum">
    /**
     * -----CURRICULUM-----
     */
    // Error Message
    public static final String DUPLICATE_CURRICULUM_CODE = "Duplicate Curriculum Code!";
    public static final String DUPLICATE_CURRICULUM_NAME = "Duplicate Curriculum Name!";
    public static final String INVALID_CURRICULUM_ID = "Curriculum ID not exist!";
    public static final String UNABLE_TO_DELETE_CURRICULUM_ = "Curriculum has available Subjects. Unable to delete!";
    //</editor-fold>

    //<editor-fold desc="Notification">
    /**
     * Notification
     */
    // Constant value
    public static final String ACCOUNT_SYSTEM = "system";
    // Error Message
    public static final String ERROR_GENERATE_NOTIFICATION = "Error at Generate Notifications!";
    //</editor-fold>

    //<editor-fold desc="Firebase">
    /**
     * -----FIREBASE-----
     */
    // Pattern
    public static final String PROJECT_ID = "app-test-c1bfb";
    public static final String BUCKET_NAME = "app-test-c1bfb.appspot.com";
    public static final String CURRICULUM = "curriculum";
    public static final String AVATAR = "avatar";
    public static final String SUBJECT = "subject";
    //</editor-fold>

    //<editor-fold desc="Registering Guest">
    /**
     * -----REGISTERING GUEST-----
     */
    // Guest Status
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_CONTACTED = "contacted";
    public static final String STATUS_CANCELED = "canceled";
    // Error Message
    public static final String INVALID_CITY = "Invalid city!";
    public static final String INVALID_GUEST_ID = "Guest ID not exist!";
    public static final String INVALID_GUEST_STATUS = "Guest Status must be pending/contacted/canceled!";
    //</editor-fold>

    //<editor-fold desc="Room">
    /**
     * -----ROOM-----
     */
    // Error Message
    public static final String ERROR_GET_ROOM_ID = "Unable to get Room ID!";
    //</editor-fold>

    //<editor-fold desc="Session">
    /**
     * -----SESSION-----
     */
    // Error Message
    public static final String ERROR_GENERATE_SESSIONS = "Error at Generate Sessions!";
    public static final String INVALID_NEW_SESSION = "New Start Time already existed in Session List!";
    //</editor-fold>

    //<editor-fold desc="Shift">
    /**
     * -----SHIFT-----
     */
    // Pattern
    public static final String DAY_OF_WEEK_PATTERN = "((\\d)[-])+(\\d|[C][N])";
    public static final String TIME_END_PATTERN = "(09:30)|(11:00)|(15:30)|(17:00)|(19:30)|(21:00)";
    public static final String TIME_START_PATTERN = "(08:00)|(09:30)|(14:00)|(15:30)|(18:00)|(19:30)";
    // Error Message
    public static final String DUPLICATE_SHIFT = "Duplicate Shift!";
    public static final String INCOMPATIBLE_START_TIME = "New Start Time incompatible with new Shift's Start Time!";
    public static final String INVALID_DAY_OF_WEEK = "DayOfWeek must be 2 days or more, separated by [-]! (2-4-6, 3-5, 7-CN)";
    public static final String INVALID_DURATION = "Duration must be multiples of 90 and larger than 0!";
    public static final String INVALID_NEW_SHIFT_ID = "You're changing the rest Sessions of this Class to another Shift. Please input new Shift ID!";
    public static final String INVALID_SHIFT_AVAILABLE = "Shift not available!";
    public static final String INVALID_SHIFT_ID = "Shift ID not exist!";
    public static final String TIME_END_PATTERN_ERROR = "TimeEnd must be [09:30, 11:00, 15:30, 17:00, 19:30, 21:00]!";
    public static final String TIME_START_PATTERN_ERROR = "TimeStart must be [08:00, 09:30, 14:00, 15:30, 18:00, 19:30]!";
    //</editor-fold>

    //<editor-fold desc="Staff">
    /**
     * -----STAFF-----
     */
    // Error Message
    //</editor-fold>

    //<editor-fold desc="Student">
    /**
     * -----STUDENT-----
     */
    // Error Message
    public static final String INVALID_STUDENT_ID = "Student ID not exist!";
    public static final String INVALID_STUDENT_USERNAME = "Student Username not exist!";
    public static final String ERROR_DELETE_STUDENT_BOOKING = "CAN NOT DELETE STUDENT BECAUSE STUDENT'S BOOKING IS PAID!";
    public static final String ERROR_DELETE_STUDENT_CLASS = "CAN NOT DELETE STUDENT BECAUSE STUDENT'S CLASS IS WAITING/STUDYING!";
    //</editor-fold>

    //<editor-fold desc="Student In Class">
    /**
     * -----STUDENT IN CLASS-----
     */
    // Error Message
    public static final String ERROR_SAVE_STUDENT_IN_CLASS = "Save student in class FAILED!";
    //</editor-fold>

    //<editor-fold desc="Subject">
    /**
     * -----SUBJECT-----
     */
    // Constant Value
    public static final String DEFAULT_SUBJECT_RATING = "0/0";
    // Error Message
    public static final String DUPLICATE_SUBJECT_CODE = "Duplicate Subject Code!";
    public static final String DUPLICATE_SUBJECT_NAME = "Duplicate Subject Name!";
    public static final String ERROR_GENERATE_SUBJECT_RATING = "Generate subject rating FAILED!";
    public static final String INVALID_SUBJECT_AVAILABLE = "Subject not available!";
    public static final String INVALID_SUBJECT_ID = "Subject ID not exist!";
    public static final String INVALID_SUBJECT_PRICE = "Subject Price must be GREATER or EQUAL to 0!";
    public static final String INVALID_SUBJECT_SLOT = "Subject's Slot must be GREATER than 0!";
    public static final String INVALID_SUBJECT_SLOT_PER_WEEK = "Subject's Slot Per Week must be GREATER than 0!";
    //</editor-fold>

    //<editor-fold desc="Subject Detail">
    /**
     * -----SUBJECT DETAIL-----
     */
    // Error Message
    public static final String INVALID_SUBJECT_DETAIL_AVAILABLE = "Subject Detail not available!";
    public static final String INVALID_SUBJECT_DETAIL_ID = "Subject Detail ID not exist!";
    public static final String INVALID_WEEK_NUM = "Number of weeks must be GREATER than 0!";
    public static final String INVALID_WEEK_NUM_LIMIT = "Number of weeks for this Subject is at their limit!";
    //</editor-fold>

    //<editor-fold desc="Teacher">
    /**
     * -----TEACHER-----
     */
    // Constant Value
    public static final String DEFAULT_TEACHER_RATING = "0/0";
    // Error Message
    public static final String ERROR_GENERATE_TEACHER_RATING = "Generate teacher rating FAILED!";
    public static final String ERROR_DELETE_TEACHER_CLASS = "CAN NOT DELETE TEACHER BECAUSE TEACHER'S CLASS IS WAITING/STUDYING!";
    //</editor-fold>

    //<editor-fold desc="Teaching Branch">
    /**
     * -----TEACHING BRANCH-----
     */
    // Error Message
    //</editor-fold>

    //<editor-fold desc="Teaching Subject">
    /**
     * -----TEACHING SUBJECT-----
     */
    // Error Message
    //</editor-fold>

    //<editor-fold desc="Others">
    /**
     * -----OTHERS-----
     */
    public static final String CAPITAL_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_PATTERN = "HH:mm";
    public static final String DAY_END = " 11:59:59";
    public static final String DAY_START = " 00:00:00";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9]+[@]{1}+[a-zA-Z0-9]+[.]{1}+([a-zA-Z0-9]+[.]{1})*+[a-zA-Z0-9]+$";
    public static final String INVALID_ADDRESS = "Null or empty address!";
    public static final String INVALID_BIRTHDAY = "Invalid birthday!";
    public static final String INVALID_EMAIL_PATTERN = "Invalid email!";
    public static final String INVALID_NAME = "Null or empty or unreal name!";
    public static final String INVALID_PHONE_PATTERN = "Invalid phone!";
    public static final String INVALID_PARENT_PHONE_PATTERN = "Invalid parent phone!";
    public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String NAME_PATTERN = "[A-Za-z ]*";
    public static final String NUMBERS = "1234567890";
    public static final String PHONE_PATTERN = "(84|0[3|5|7|8|9])+([0-9]{8})\\b";
    public static final String RATING_PATTERN = "#.#";
    public static final String SPECIAL_CHARACTERS = "!@#$";
    public static final String SYMBOL_COLON = ":";
    public static final String SYMBOL_HYPHEN = "-";
    public static final String SYMBOL_SLASH = "/";
    public static final String TIMEZONE = "Asia/Ho_Chi_Minh";
    public static final int NO_OF_RATING_PPL_ADD_ON = 1;
    //</editor-fold>
}
