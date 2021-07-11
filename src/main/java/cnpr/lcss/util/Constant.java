package cnpr.lcss.util;

public class Constant {

    //<editor-fold desc="Account">
    /**
     * -----ACCOUNT-----
     */
    // User Role
    public static final String ROLE_MANAGER = "manager";
    public static final String ROLE_MANAGER_CODE = "ql";
    public static final String ROLE_STAFF = "staff";
    public static final String ROLE_STAFF_CODE = "nv";
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_ADMIN_CODE = "ad";
    public static final String ROLE_TEACHER = "teacher";
    public static final String ROLE_TEACHER_CODE = "gv";
    public static final String ROLE_STUDENT = "student";
    public static final String ROLE_STUDENT_CODE = "hs";
    // Pattern
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9]+[@]{1}+[a-zA-Z0-9]+[.]{1}+([a-zA-Z0-9]+[.]{1})*+[a-zA-Z0-9]+$";
    public static final String PHONE_PATTERN = "(84|0[3|5|7|8|9])+([0-9]{8})\\b";
    public static final String NAME_PATTERN = "[A-Za-z ]*";
    public static final String CAPITAL_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String SPECIAL_CHARACTERS = "!@#$";
    public static final String NUMBERS = "1234567890";
    // Error Message
    public static final String PASSWORD_NOT_MATCH = "Password does not match!";
    public static final String USERNAME_NOT_EXIST = "Username does not exist!";
    public static final String BRANCH_ID_NOT_EXIST = "Brand Id does not exist!";
    public static final String INVALID_NAME = "Name is null, empty, or unreal!";
    public static final String INVALID_PHONE_PATTERN = "Phone number is invalid!";
    public static final String INVALID_EMAIL_PATTERN = "Email is invalid!";
    public static final String INVALID_BIRTHDAY = "Birthday is invalid!";
    public static final String INVALID_TEACHER_EXP = "Teacher's experience is null or empty!";
    public static final String INVALID_STAFF_BIRTHDAY = "Staff MUST older or equal to 18 yo!";
    public static final String INVALID_MANAGER_BIRTHDAY = "Manager MUST older or equal to 18 yo!";
    public static final String INVALID_ADMIN_BIRTHDAY = "Admin MUST older or equal to 18 yo!";
    public static final String INVALID_TEACHER_BIRTHDAY = "Teacher MUST older or equal to 15 yo!";
    public static final String INVALID_STUDENT_BIRTHDAY = "Student MUST older or equal to 3 yo!";
    public static final String INVALID_ROLE = "Role is invalid!";
    public static final String INVALID_CHANGE_ROLE = "User's role MUST BE Manager or Staff!";
    public static final String INVALID_NEW_ROLE = "New role MUST BE Manager or Staff!";
    public static final String INVALID_IS_AVAILABLE = "Account is no longer active!";
    public static final String DUPLICATE_BRANCH_ID = "Branch id already existed!";
    public static final String NULL_OR_EMPTY_NAME = "Null or Empty Name!";
    public static final String NULL_OR_EMPTY_ADDRESS = "Null or Empty Address!";
    public static final String GENERATE_USERNAME_ERROR = "Generate username error!";
    public static final String GENERATE_PASSWORD_ERROR = "Generate password error!";
    public static final String EMAIL_SENDING_ERROR = "Sending Email failed!";
    //</editor-fold>

    /**
     * -----ATTENDANCE-----
     */

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

    /**
     * -----BRANCH-----
     */
    // Error Message
    public static final String INVALID_BRANCH_ID = "Branch ID not exist!";
    public static final String INVALID_BRANCH_AVAILABLE = "Branch not available!";

    /**
     * -----CLASS-----
     */
    // Class Status
    public static final String CLASS_STATUS_WAITING = "waiting";
    // Error Message
    public static final String INVALID_CLASS_ID = "Class ID not exist!";
    public static final String INVALID_CLASS_STATUS_NOT_WAITING = "Class Status must be waiting!";

    /**
     * -----CURRICULUM-----
     */

    /**
     * -----REGISTERING GUEST-----
     */

    /**
     * -----SESSION-----
     */

    /**
     * -----SHIFT-----
     */
    // Error Message
    public static final String INVALID_SHIFT_ID = "Shift ID not exist!";
    public static final String INVALID_SHIFT_AVAILABLE = "Shift not available!";

    /**
     * -----STAFF-----
     */

    /**
     * -----STUDENT-----
     */
    // Error Message
    public static final String INVALID_STUDENT_ID = "Student ID not exist!";
    public static final String INVALID_STUDENT_USERNAME = "Student Username not exist!";

    /**
     * -----STUDENT IN CLASS-----
     */
    // Error Message
    public static final String ERROR_SAVE_STUDENT_IN_CLASS = "Save student in class FAILED!";

    /**
     * -----SUBJECT-----
     */
    // Error Message
    public static final String INVALID_SUBJECT_ID = "Subject ID not exist!";
    public static final String INVALID_SUBJECT_AVAILABLE = "Subject not available!";

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

    /**
     * -----OTHERS-----
     */
    public static final String TIMEZONE = "Asia/Ho_Chi_Minh";
}
