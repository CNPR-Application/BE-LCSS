package cnpr.lcss.util;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class Constant {
    // <editor-fold desc="Account">
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
    public static final String INVALID_USERNAME_OR_PASSWORD = "Invalid Username or Password!";
    public static final String PASSWORD_NOT_MATCH = "Password not match!";
    public static final String INVALID_PASSWORD_PATTERN = "Password must has minimum six characters, at least one letter and one number!";
    public static final String OLDPASSWORD_MATCH_NEWPASSWORD = "New password matched Old password! Please try new password";
    public static final String RENEWPASSWORD_NOT_MATCH_NEWPASSWORD = "Re new password did not match new password! Please try again";
    // </editor-fold>

    // <editor-fold desc="Attendance">
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
    // </editor-fold>

    // <editor-fold desc="Booking">
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
    public static final String INVALID_BOOKING_STATUS = "Booking Status must be paid/canceled/processed!";
    public static final String ERROR_GET_BOOKING_ID = "Unable to get Booking ID!";
    // </editor-fold>

    // <editor-fold desc="Branch">
    /**
     * -----BRANCH-----
     */
    // Error Message
    public static final String INVALID_BRANCH_ID = "Branch ID not exist!";
    public static final String INVALID_BRANCH_AVAILABLE = "Branch not available!";
    public static final String DUPLICATE_BRANCH_ID = "Duplicate Branch ID!";
    public static final String DUPLICATE_BRANCH_NAME = "Duplicate Branch Name!";
    // </editor-fold>

    // <editor-fold desc="Class">
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
    public static final String ERROR_DELETE_CLASS = "Unable to delete this class!";
    public static final String ERROR_GET_CLASS_ID = "Unable to get Class ID!";
    public static final String INVALID_CLASS_CREATOR = "Invalid Class Creator!";
    public static final String INVALID_CLASS_ID = "Class ID not exist!";
    public static final String INVALID_CLASS_NAME = "Null or empty class name!";
    public static final String INVALID_CLASS_STATUS_NOT_WAITING = "Class Status must be waiting!";
    public static final String INVALID_DELETE_CLASS_REASON = "Delete Class Reason must not be empty or null!";
    public static final String INVALID_OPENING_DATE = "Null or invalid opening day!";
    public static final String INVALID_OPENING_DAY_VS_DAY_IN_SHIFT = "Opening Day must be a day in Shift!";
    public static final String INVALID_SLOT_PER_WEEK_AND_DAY_OF_WEEK = "Subject's slot per week incompatible with Shift's days of week!";
    // </editor-fold>

    // <editor-fold desc="Curriculum">
    /**
     * -----CURRICULUM-----
     */
    // Error Message
    public static final String DUPLICATE_CURRICULUM_CODE = "Duplicate Curriculum Code!";
    public static final String DUPLICATE_CURRICULUM_NAME = "Duplicate Curriculum Name!";
    public static final String INVALID_CURRICULUM_ID = "Curriculum ID not exist!";
    public static final String UNABLE_TO_DELETE_CURRICULUM_ = "Curriculum has available Subjects. Unable to delete!";
    // </editor-fold>

    // <editor-fold desc="Notification">
    /**
     * Notification
     */
    // Constant value
    public static final String ACCOUNT_SYSTEM = "system";
    public static final String FEEDBACK_TITLE = "BẠN CÓ HÀI LÒNG VỚI LỚP '%s'?";
    public static final String FEEDBACK_BODY = "Vui lòng để lại đánh giá về lớp '%s' để trung tâm cải thiện chất lượng giảng dạy. Cám ơn bạn!";
    // Error Message
    public static final String ERROR_GENERATE_NOTIFICATION = "Error at Generate Notifications!";
    // </editor-fold>

    // <editor-fold desc="Firebase">
    /**
     * -----FIREBASE-----
     */
    // Pattern
    public static final String PROJECT_ID = "app-test-c1bfb";
    public static final String BUCKET_NAME = "app-test-c1bfb.appspot.com";
    public static final String CURRICULUM = "curriculum";
    public static final String AVATAR = "avatar";
    public static final String SUBJECT = "subject";
    // </editor-fold>

    // <editor-fold desc="Registering Guest">
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
    // </editor-fold>

    // <editor-fold desc="Room">
    /**
     * -----ROOM-----
     */
    // Error Message
    public static final String ERROR_GET_ROOM_ID = "Unable to get Room ID!";
    public static final String INVALID_ROOM_ID = "Room ID not exist!";
    public static final String ERROR_DELETE_ROOM_SESSION = "Can not delete Room because Room's Sessions are still available!";
    // </editor-fold>

    // <editor-fold desc="Session">
    /**
     * -----SESSION-----
     */
    // Error Message
    public static final String ERROR_GENERATE_SESSIONS = "Error at Generate Sessions!";
    public static final String INVALID_NEW_SESSION = "New Start Time already existed in Session List!";
    // </editor-fold>

    // <editor-fold desc="Shift">
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
    // </editor-fold>

    // <editor-fold desc="Staff">
    /**
     * -----STAFF-----
     */
    // Error Message
    // </editor-fold>

    // <editor-fold desc="Student">
    /**
     * -----STUDENT-----
     */
    // Error Message
    public static final String INVALID_STUDENT_ID = "Student ID not exist!";
    public static final String INVALID_STUDENT_USERNAME = "Student Username not exist!";
    public static final String ERROR_DELETE_STUDENT_BOOKING = "CAN NOT DELETE STUDENT BECAUSE STUDENT'S BOOKING IS PAID!";
    public static final String ERROR_DELETE_STUDENT_CLASS = "CAN NOT DELETE STUDENT BECAUSE STUDENT'S CLASS IS WAITING/STUDYING!";
    // </editor-fold>

    // <editor-fold desc="Student In Class">
    /**
     * -----STUDENT IN CLASS-----
     */
    // Error Message
    public static final String ERROR_SAVE_STUDENT_IN_CLASS = "Save student in class FAILED!";
    // </editor-fold>

    // <editor-fold desc="Subject">
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
    // </editor-fold>

    // <editor-fold desc="Subject Detail">
    /**
     * -----SUBJECT DETAIL-----
     */
    // Error Message
    public static final String INVALID_SUBJECT_DETAIL_AVAILABLE = "Subject Detail not available!";
    public static final String INVALID_SUBJECT_DETAIL_ID = "Subject Detail ID not exist!";
    public static final String INVALID_WEEK_NUM = "Number of weeks must be GREATER than 0!";
    public static final String INVALID_WEEK_NUM_LIMIT = "Number of weeks for this Subject is at their limit!";
    // </editor-fold>

    // <editor-fold desc="Teacher">
    /**
     * -----TEACHER-----
     */
    // Constant Value
    public static final String DEFAULT_TEACHER_RATING = "0/0";
    // Error Message
    public static final String ERROR_GENERATE_TEACHER_RATING = "Generate teacher rating FAILED!";
    public static final String ERROR_DELETE_TEACHER_CLASS = "CAN NOT DELETE TEACHER BECAUSE TEACHER'S CLASS IS WAITING/STUDYING!";
    // </editor-fold>

    // <editor-fold desc="Teaching Branch">
    /**
     * -----TEACHING BRANCH-----
     */
    // Error Message
    // </editor-fold>

    // <editor-fold desc="Teaching Subject">
    /**
     * -----TEACHING SUBJECT-----
     */
    // Error Message
    public static final String EXISTED_TEACHING_SUBJECT = "This Teaching Subject is already taken!";
    public static final String UNABLE_TO_DELETE_TEACHING_SUBJECT = "This Teaching Subject is UNABLE to delete!";
    // </editor-fold>

    // <editor-fold desc="Others">
    /**
     * -----OTHERS-----
     */
    // String
    public static final String CAPITAL_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String CRON_EVERY_DAY_AT_MIDNIGHT = "0 0 0 * * *";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DAY_END = " 11:59:59";
    public static final String DAY_START = " 00:00:00";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9]+[@]{1}+[a-zA-Z0-9]+[.]{1}+([a-zA-Z0-9]+[.]{1})*+[a-zA-Z0-9]+$";
    public static final String INVALID_ADDRESS = "Null or empty address!";
    public static final String INVALID_BIRTHDAY = "Invalid birthday!";
    public static final String INVALID_EMAIL_PATTERN = "Invalid email!";
    public static final String INVALID_NAME = "Null or empty or unreal name!";
    public static final String INVALID_PARENT_PHONE_PATTERN = "Invalid parent phone!";
    public static final String INVALID_PHONE_PATTERN = "Invalid phone!";
    public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String NAME_PATTERN = "[A-Za-z ]*";
    public static final String NOT_AVAILABLE_INFO = "N/A";
    public static final String NUMBERS = "1234567890";
    public static final String NUMBER_ZERO = "0";
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"; // Minimum six characters, at least one letter and one number
    public static final String PHONE_PATTERN = "(84|0[3|5|7|8|9])+([0-9]{8})\\b";
    public static final String RATING_PATTERN = "#.#";
    public static final String SECRET_KEY = "LCSS_FA21#CNPR";
    public static final String SPECIAL_CHARACTERS = "!@#$";
    public static final String STRING_AUTHORIZATION = "Authorization";
    public static final String SYMBOL_COLON = ":";
    public static final String SYMBOL_HYPHEN = "-";
    public static final String SYMBOL_SLASH = "/";
    public static final String SYSTEM_MAIL_PASSWORD = "lcss@123";
    public static final String SYSTEM_MAIL_SUBJECT_CREATE_NEW_ACOUNT = "Chào mừng bạn đến với trung tâm CNPR!";
    public static final String SYSTEM_MAIL_SUBJECT_SEND_NOTI_MAIL_TO_GROUP = "THÔNG BÁO DỜI LỊCH KHAI GIẢNG!";
    public static final String SYSTEM_MAIL_SUBJECT_FORGOT_PASSWORD= "THÔNG BÁO KHÔI PHỤC MẬT KHẨU";
    public static final String SYSTEM_MAIL_USERNAME = "lcssfall2021";
    public static final String SYSTEM_NAME = "LCSS - LANGUAGE CENTER SUPPORT SYSTEM";
    public static final String TIMEZONE = "Asia/Ho_Chi_Minh";
    public static final String TIME_PATTERN = "HH:mm";
    // URL
    public static final String URL_BRANCH = "/admin/branches**";
    public static final String URL_CLASS = "/classes**";
    public static final String URL_CLASS_FILTER = "/classes/**";
    public static final String URL_CURRICULUM = "/curriculums**";
    public static final String URL_FORGOT_PASSWORD = "/forgot-password";
    public static final String URL_GUESTS = "/guests**";
    public static final String URL_LOGIN = "/login";
    public static final String URL_SHIFT = "/shifts**";
    public static final String URL_SUBJECT = "/subjects**";
    public static final String URL_SUBJECT_DETAIL = "/subjects/details**";
    // Number
    public static final int NO_OF_RATING_PPL_ADD_ON = 1;
    public static final long PLUS_HOUR_FROM_UTC_TO_UTC7 = 7;
    // Message
    public static final String SYSTEM_MAIL_CONTENT_CREATE_NEW_ACOUNT = "Tài khoản đăng nhập của bạn là: %s"
            + "\n\nMật khẩu: %s" + "\n\nHãy nhớ đổi mật khẩu ngay lần đăng nhập đầu tiên nhé!"
            + "\n\nChúc bạn một ngày vui vẻ!" + "\n\nCNPR.";
    public static final String SYSTEM_MAIL_CONTENT_SEND_NOTI_EMAIL_TO_GROUP = "Thân gửi bạn %s,\n"
            + "\nCám ơn bạn đã đăng ký vào khóa học %s của trung tâm CNPR. Sau khi nhận đơn đăng ký, trung tâm đã xử lý và mong muốn khai giảng lớp vào ngày %s như dự tính.\n\n"
            + "Tuy nhiên, do tình hình dịch bệnh, hiện lớp vẫn chưa đủ học viên đăng ký, trung tâm muốn thông báo sẽ dời khai giảng sang ngày %s. Mong quý học viên thông cảm!\n"
            + "\n" + "Chân thành cám ơn,\n" + "CNPR.";
    public static final String SYSTEM_MAIL_CONTENT_FORGOT_PASSWORD= "Chào %s,\n"
            +"\nHệ thống trung tâm ngoại ngữ CNPR chúng tôi vừa nhận được yêu cầu khôi phục mật khẩu từ bạn với tài khoản %s, mật khẩu của bạn là: %s.\n" +
            "\n" +
            "Chân thành cám ơn,\n" +
            "CNPR.";
    // </editor-fold>

    // <editor-fold desc="Conversion">

    /**
     * -----CONVERSION-----
     */
    // <editor-fold desc="Convert to UTC+7 TimeZone">
    public static Date convertToUTC7TimeZone(Date insDate) {
        ZonedDateTime date = ZonedDateTime.ofInstant(insDate.toInstant(), ZoneId.of(TIMEZONE))
                .plusHours(PLUS_HOUR_FROM_UTC_TO_UTC7);
        return Date.from(date.toInstant());
    }
    // </editor-fold>

    //<editor-fold desc="Calculate Rating">
    public static String calculateRating(String rating) {
        DecimalFormat df = new DecimalFormat(Constant.RATING_PATTERN);
        String[] arrOfInpStr = rating.split(Constant.SYMBOL_SLASH);
        String finalResult;
        if (arrOfInpStr[0].equals(Constant.NUMBER_ZERO) && arrOfInpStr[1].equals(Constant.NUMBER_ZERO)) {
            finalResult = Constant.NUMBER_ZERO;
        } else {
            double result = Double.parseDouble(arrOfInpStr[0]) / Double.parseDouble(arrOfInpStr[1]);
            finalResult = df.format(result);
        }
        return finalResult;
    }
    //</editor-fold>
    // </editor-fold>
}
