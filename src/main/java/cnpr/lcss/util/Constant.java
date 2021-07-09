package cnpr.lcss.util;

public class Constant {

    /**
     * -----BOOKING-----
     */
    // Constant Variable
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
    // Constant Variable
    public static final String CLASS_STATUS_WAITING = "waiting";
    // Error Message
    public static final String INVALID_CLASS_ID = "Class ID not exist!";
    public static final String INVALID_CLASS_STATUS_NOT_WAITING = "Class Status must be waiting!";

    /**
     * -----SHIFT-----
     */
    // Error Message
    public static final String INVALID_SHIFT_ID = "Shift ID not exist!";
    public static final String INVALID_SHIFT_AVAILABLE = "Shift not available!";

    /**
     * -----STUDENT-----
     */
    // Error Message
    public static final String INVALID_STUDENT_ID = "Student ID not exist!";

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
     * -----OTHERS-----
     */
    public static final String TIMEZONE = "Asia/Ho_Chi_Minh";
}
