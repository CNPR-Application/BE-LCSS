package cnpr.lcss.util;

public class Constant {

    //<editor-fold desc="Account">
    /**
     * -----ACCOUNT-----
     */
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
    public static final String INVALID_ADDRESS = "Null or empty address!";
    public static final String INVALID_ADMIN_BIRTHDAY = "Admin must older or equal to 18!";
    public static final String INVALID_BIRTHDAY = "Invalid birthday!";
    public static final String INVALID_CHANGE_ROLE = "Role must be manager/staff!";
    public static final String INVALID_EMAIL_PATTERN = "Invalid email!";
    public static final String INVALID_IS_AVAILABLE = "Account no longer active!";
    public static final String INVALID_MANAGER_BIRTHDAY = "Manager must older or equal to 18!";
    public static final String INVALID_NAME = "Null or empty or unreal name!";
    public static final String INVALID_NEW_ROLE = "New role must be manager/staff!";
    public static final String INVALID_PHONE_PATTERN = "Invalid phone!";
    public static final String INVALID_ROLE = "Invalid role!";
    public static final String INVALID_STAFF_BIRTHDAY = "Staff must older or equal to 18!";
    public static final String INVALID_STUDENT_BIRTHDAY = "Student must older or equal to 3!";
    public static final String INVALID_TEACHER_BIRTHDAY = "Teacher must older or equal to 15!";
    public static final String INVALID_TEACHER_EXP = "Null or empty experience!";
    public static final String PASSWORD_NOT_MATCH = "Password not match!";
    public static final String USERNAME_NOT_EXIST = "Username not exist!";
    //</editor-fold>

    /**
     * -----ATTENDANCE-----
     */

    //<editor-fold desc="Booking">
    /**
     * -----BOOKING-----
     */
    // Booking Status
    public static final String BOOKING_STATUS_PAID = "paid";
    public static final String BOOKING_STATUS_CANCELED = "canceled";
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
    public static final String CLASS_STATUS_WAITING = "waiting";
    // Error Message
    public static final String INVALID_CLASS_ID = "Class ID not exist!";
    public static final String INVALID_CLASS_STATUS_NOT_WAITING = "Class Status must be waiting!";
    //</editor-fold>

    /**
     * -----CURRICULUM-----
     */

    /**
     * -----REGISTERING GUEST-----
     */

    /**
     * -----SESSION-----
     */

    //<editor-fold desc="Shift">
    /**
     * -----SHIFT-----
     */
    // Error Message
    public static final String INVALID_SHIFT_ID = "Shift ID not exist!";
    public static final String INVALID_SHIFT_AVAILABLE = "Shift not available!";
    //</editor-fold>

    /**
     * -----STAFF-----
     */

    //<editor-fold desc="Student">
    /**
     * -----STUDENT-----
     */
    // Error Message
    public static final String INVALID_STUDENT_ID = "Student ID not exist!";
    public static final String INVALID_STUDENT_USERNAME = "Student Username not exist!";
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
    // Error Message
    public static final String INVALID_SUBJECT_ID = "Subject ID not exist!";
    public static final String INVALID_SUBJECT_AVAILABLE = "Subject not available!";
    //</editor-fold>

    /**
     * -----SUBJECT DETAIL-----
     */

    /**
     * -----TEACHER-----
     */

    /**
     * -----TEACHING BRANCH-----
     */

    /**
     * -----TEACHING SUBJECT-----
     */

    //<editor-fold desc="Others">
    /**
     * -----OTHERS-----
     */
    public static final String TIMEZONE = "Asia/Ho_Chi_Minh";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9]+[@]{1}+[a-zA-Z0-9]+[.]{1}+([a-zA-Z0-9]+[.]{1})*+[a-zA-Z0-9]+$";
    public static final String PHONE_PATTERN = "(84|0[3|5|7|8|9])+([0-9]{8})\\b";
    public static final String NAME_PATTERN = "[A-Za-z ]*";
    public static final String CAPITAL_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String SPECIAL_CHARACTERS = "!@#$";
    public static final String NUMBERS = "1234567890";
    //</editor-fold>
}
