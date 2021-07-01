package cnpr.lcss.service;

import cnpr.lcss.dao.*;
import cnpr.lcss.model.*;
import cnpr.lcss.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountService {

    /**
     * -----USER ROLE-----
     **/
    private static final String ROLE_MANAGER = "manager";
    private static final String ROLE_STAFF = "staff";
    private static final String ROLE_ADMIN = "admin";
    private static final String ROLE_TEACHER = "teacher";
    private static final String ROLE_STUDENT = "student";
    /**
     * -----PATTERN-----
     **/
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9]+[@]{1}+[a-zA-Z0-9]+[.]{1}+([a-zA-Z0-9]+[.]{1})*+[a-zA-Z0-9]+$";
    private static final String PHONE_PATTERN = "(84|0[3|5|7|8|9])+([0-9]{8})\\b";
    private static final String NAME_PATTERN = "[A-Za-z ]*";
    private static final int RAND_MIN = 100000;
    private static final int RAND_MAX = 999999;
    private static final String CAPITAL_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String SPECIAL_CHARACTERS = "!@#$";
    private static final String NUMBERS = "1234567890";
    /**
     * -----ERROR MSG-----
     **/
    private static final String PASSWORD_NOT_MATCH = "Password does not match!";
    private static final String USERNAME_NOT_EXIST = "Username does not exist!";
    private static final String BRANCH_ID_NOT_EXIST = "Brand Id does not exist!";
    private static final String INVALID_NAME = "Name is null, empty, or unreal!";
    private static final String INVALID_PHONE_PATTERN = "Phone number is invalid!";
    private static final String INVALID_EMAIL_PATTERN = "Email is invalid!";
    private static final String INVALID_BIRTHDAY = "Birthday is invalid!";
    private static final String INVALID_TEACHER_EXP = "Teacher's experience is null or empty!";
    private static final String INVALID_STAFF_BIRTHDAY = "Staff MUST older or equal to 18 yo!";
    private static final String INVALID_MANAGER_BIRTHDAY = "Manager MUST older or equal to 18 yo!";
    private static final String INVALID_ADMIN_BIRTHDAY = "Admin MUST older or equal to 18 yo!";
    private static final String INVALID_TEACHER_BIRTHDAY = "Teacher MUST older or equal to 15 yo!";
    private static final String INVALID_STUDENT_BIRTHDAY = "Student MUST older or equal to 3 yo!";
    private static final String INVALID_ROLE = "Role is invalid!";
    private static final String INVALID_CHANGE_ROLE = "User's role MUST BE Manager or Staff!";
    private static final String INVALID_NEW_ROLE = "New role MUST BE Manager or Staff!";
    private static final String DUPLICATE_BRANCH_ID = "Branch id already existed!";
    private static final String NULL_OR_EMPTY_NAME = "Null or Empty Name!";
    private static final String NULL_OR_EMPTY_ADDRESS = "Null or Empty Address!";
    private static final String GENERATE_USERNAME_ERROR = "Generate username error!";
    private static final String GENERATE_PASSWORD_ERROR = "Generate password error!";

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    TeachingBranchRepository teachingBranchRepository;

    //<editor-fold desc="Convert from Unicode to normal string">
    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
    //</editor-fold>

    //<editor-fold desc="Generate Username">
    private String generateUsername(String name) throws Exception {
        String username;
        name = stripAccents(name);
        int randNum = (int) Math.floor(Math.random() * (RAND_MAX - RAND_MIN + 1) + RAND_MIN);

        if (name != null && !name.isEmpty() && name.matches(NAME_PATTERN)) {
            String[] parts = name.split("\\s+");
            username = parts[parts.length - 1] + randNum;
        } else {
            throw new Exception(INVALID_NAME);
        }
        return username.toLowerCase(Locale.ROOT);
    }
    //</editor-fold>

    //<editor-fold desc="Generate Password">
    private String generatePassword(int length) {
        final String chars = CAPITAL_CASE_LETTERS + LOWER_CASE_LETTERS + SPECIAL_CHARACTERS + NUMBERS;

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }
    //</editor-fold>

    //<editor-fold desc="1.0 Check login">
    public ResponseEntity<?> checkLogin(LoginRequestDto loginRequest) throws Exception {
        LoginResponseDto loginResponseDto = new LoginResponseDto();

        try {
            if (loginRequest.getUsername() == null || loginRequest.getUsername().isEmpty()
                    || loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                throw new NullPointerException();
            } else {
                if (accountRepository.existsById(loginRequest.getUsername())) {
                    Account acc = accountRepository.findOneByUsername(loginRequest.getUsername());
                    String accUsername = acc.getUsername();
                    if (acc.getPassword().equals(loginRequest.getPassword())) {
                        loginResponseDto.setName(acc.getName());
                        loginResponseDto.setAddress(acc.getAddress());
                        loginResponseDto.setEmail(acc.getEmail());
                        loginResponseDto.setBirthday(acc.getBirthday());
                        loginResponseDto.setPhone(acc.getPhone());
                        loginResponseDto.setImage(acc.getImage());
                        loginResponseDto.setRole(acc.getRole());
                        loginResponseDto.setCreatingDate(acc.getCreatingDate());

                        // Return Branch Id, Branch Name
                        // Role: Admin, Manager, Staff
                        if (acc.getRole().equalsIgnoreCase(ROLE_MANAGER) || acc.getRole().equalsIgnoreCase(ROLE_STAFF)
                                || acc.getRole().equalsIgnoreCase(ROLE_ADMIN)) {
                            loginResponseDto.setBranchId(staffRepository.findBranchIdByStaffUsername(accUsername));
                            loginResponseDto.setBranchName(staffRepository.findBranchNameByStaffUsername(accUsername));
                        } // Role: Teacher
                        else if (acc.getRole().equalsIgnoreCase(ROLE_TEACHER)) {
                            loginResponseDto.setBranchId(teacherRepository.findBranchIdByTeacherUsername(accUsername));
                            loginResponseDto.setBranchName(teacherRepository.findBranchNameByTeacherUsername(accUsername));
                        } // Role: Student
                        else if (acc.getRole().equalsIgnoreCase(ROLE_STUDENT)) {
                            loginResponseDto.setBranchId(studentRepository.findBranchIdByStudentUsername(accUsername));
                            loginResponseDto.setBranchName(studentRepository.findBranchNameByStudentUsername(accUsername));
                        } else {
                            throw new Exception();
                        }

                        // Return Parent's information
                        // Role: Student
                        if (acc.getRole().equalsIgnoreCase(ROLE_STUDENT)) {
                            loginResponseDto.setParentName(studentRepository.findParentNameByStudentUsername(accUsername));
                            loginResponseDto.setParentPhone(studentRepository.findParentPhoneByStudentUsername(accUsername));
                        } else {
                            loginResponseDto.setParentName(null);
                            loginResponseDto.setParentPhone(null);
                        }

                        // Return Experience, Rating
                        // Role: Teacher
                        if (acc.getRole().equalsIgnoreCase(ROLE_TEACHER)) {
                            loginResponseDto.setExperience(teacherRepository.findExperienceByTeacherUsername(accUsername));
                            loginResponseDto.setRating(teacherRepository.findRatingByTeacherUsername(accUsername));
                        } else {
                            loginResponseDto.setExperience(null);
                            loginResponseDto.setRating(null);
                        }
                        return ResponseEntity.ok(loginResponseDto);
                    } else {
                        throw new Exception(PASSWORD_NOT_MATCH);
                    }
                } else {
                    throw new Exception(USERNAME_NOT_EXIST);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="2.0 Search Account Like Username">
    public ResponseEntity<?> searchAccountLikeUsernamePaging(String role, String keyword, boolean isAvailable, int pageNo, int pageSize) throws Exception {
        try {
            // Check Role existence
            if (role.equalsIgnoreCase(ROLE_ADMIN) || role.equalsIgnoreCase(ROLE_MANAGER) || role.equalsIgnoreCase(ROLE_STAFF)
                    || role.equalsIgnoreCase(ROLE_TEACHER) || role.equalsIgnoreCase(ROLE_STUDENT)) {
                // pageNo starts at 0
                // always set first page = 1 ---> pageNo - 1
                Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

                Page<Account> page = accountRepository.findByRoleEqualsAndUsernameContainingAndIsAvailable(role, keyword, isAvailable, pageable);
                int totalPage = page.getTotalPages();
                List<Account> accountList = page.getContent();
                List<AccountResponseDto> accountResponseDtoList = new ArrayList<>();
                AccountResponsePagingDto accountResponsePagingDto = new AccountResponsePagingDto();

                for (Account account : accountList) {
                    Account acc = accountRepository.findOneByUsername(account.getUsername());
                    AccountResponseDto accDto = new AccountResponseDto();
                    accDto.setUsername(acc.getUsername());
                    accDto.setName(acc.getName());
                    accDto.setAddress(acc.getAddress());
                    accDto.setEmail(acc.getEmail());
                    accDto.setBirthday(acc.getBirthday());
                    accDto.setPhone(acc.getPhone());
                    accDto.setImage(acc.getImage());
                    accDto.setRole(acc.getRole());
                    accDto.setCreatingDate(acc.getCreatingDate());
                    accDto.setIsAvailable(acc.getIsAvailable());
                    // Branch
                    // Role: Admin, Manager, Staff
                    if (role.equalsIgnoreCase(ROLE_MANAGER) || role.equalsIgnoreCase(ROLE_STAFF)
                            || role.equalsIgnoreCase(ROLE_ADMIN)) {
                        List<Branch> branchList = branchRepository.findStaffBranchByAccountUsername(acc.getUsername());
                        List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                        accDto.setBranchResponseDtoList(branchResponseDtoList);
                    } // Role: Teacher
                    else if (role.equalsIgnoreCase(ROLE_TEACHER)) {
                        List<Branch> branchList = branchRepository.findTeacherBranchByAccountUsername(acc.getUsername());
                        List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                        accDto.setBranchResponseDtoList(branchResponseDtoList);
                    } // Role: Student
                    else if (role.equalsIgnoreCase(ROLE_STUDENT)) {
                        List<Branch> branchList = branchRepository.findStudentBranchByAccountUsername(acc.getUsername());
                        List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                        accDto.setBranchResponseDtoList(branchResponseDtoList);
                    }
                    // Parent's information
                    // Role: Student
                    if (role.equalsIgnoreCase(ROLE_STUDENT)) {
                        accDto.setParentPhone(studentRepository.findParentPhoneByStudentUsername(acc.getUsername()));
                        accDto.setParentName(studentRepository.findParentNameByStudentUsername(acc.getUsername()));
                    } else {
                        accDto.setParentPhone(null);
                        accDto.setParentName(null);
                    }
                    // Teacher's exp & rating
                    // Role: Teacher
                    if (role.equalsIgnoreCase(ROLE_TEACHER)) {
                        accDto.setExperience(teacherRepository.findExperienceByTeacherUsername(acc.getUsername()));
                        accDto.setRating(teacherRepository.findRatingByTeacherUsername(acc.getUsername()));
                    } else {
                        accDto.setExperience(null);
                        accDto.setRating(null);
                    }
                    accountResponseDtoList.add(accDto);
                }

                accountResponsePagingDto.setPageNo(pageNo);
                accountResponsePagingDto.setPageSize(pageSize);
                accountResponsePagingDto.setTotalPage(totalPage);
                accountResponsePagingDto.setAccountResponseDtoList(accountResponseDtoList);

                return ResponseEntity.status(HttpStatus.OK).body(accountResponsePagingDto);
            } else {
                throw new Exception(INVALID_ROLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="3.1 Search Information by Username">
    public ResponseEntity<?> searchInfoByUsername(String username) throws Exception {
        try {
            if (accountRepository.existsByUsername(username)) {
                Map<String, Object> mapObj = new LinkedHashMap();
                Account acc = accountRepository.findOneByUsername(username);
                AccountResponseDto accDto = new AccountResponseDto();
                accDto.setUsername(acc.getUsername());
                accDto.setName(acc.getName());
                accDto.setAddress(acc.getAddress());
                accDto.setEmail(acc.getEmail());
                accDto.setBirthday(acc.getBirthday());
                accDto.setPhone(acc.getPhone());
                accDto.setImage(acc.getImage());
                accDto.setRole(acc.getRole());
                accDto.setCreatingDate(acc.getCreatingDate());
                accDto.setIsAvailable(acc.getIsAvailable());
                // Role: Admin, Manager, Staff
                if (acc.getRole().equalsIgnoreCase(ROLE_MANAGER) || acc.getRole().equalsIgnoreCase(ROLE_STAFF)
                        || acc.getRole().equalsIgnoreCase(ROLE_ADMIN)) {
                    List<Branch> branchList = branchRepository.findStaffBranchByAccountUsername(acc.getUsername());
                    List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                    accDto.setBranchResponseDtoList(branchResponseDtoList);
                } // Role: Teacher
                else if (acc.getRole().equalsIgnoreCase(ROLE_TEACHER)) {

                    List<Branch> branchList = branchRepository.findTeacherBranchByAccountUsername(acc.getUsername());
                    List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                    accDto.setBranchResponseDtoList(branchResponseDtoList);

                } // Role: Student
                else if (acc.getRole().equalsIgnoreCase(ROLE_STUDENT)) {
                    List<Branch> branchList = branchRepository.findStudentBranchByAccountUsername(acc.getUsername());
                    List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                    accDto.setBranchResponseDtoList(branchResponseDtoList);

                } else {
                    throw new Exception();
                }
                // Role: Student
                if (acc.getRole().equalsIgnoreCase(ROLE_STUDENT)) {
                    accDto.setParentPhone(studentRepository.findParentPhoneByStudentUsername(acc.getUsername()));
                    accDto.setParentName(studentRepository.findParentNameByStudentUsername(acc.getUsername()));
                } else {
                    accDto.setParentPhone(null);
                    accDto.setParentName(null);

                }
                // Role: Teacher
                if (acc.getRole().equalsIgnoreCase(ROLE_TEACHER)) {
                    accDto.setExperience(teacherRepository.findExperienceByTeacherUsername(acc.getUsername()));
                    accDto.setRating(teacherRepository.findRatingByTeacherUsername(acc.getUsername()));

                } else {
                    accDto.setExperience(null);
                    accDto.setRating(null);
                }
                return ResponseEntity.ok(accDto);
            } else {
                throw new Exception(USERNAME_NOT_EXIST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="4.0 Create New Account">
    public ResponseEntity<?> createNewAccount(NewAccountRequestDto newAcc) throws Exception {
        Date today = new Date();
        try {
            Account account = new Account();
            String newUsername;
            String newPassword;

            // Username
            try {
                do {
                    newUsername = generateUsername(newAcc.getName());
                    account.setUsername(newUsername);
                } while (accountRepository.existsByUsername(account.getUsername()));
            } catch (Exception e) {
                throw new Exception(GENERATE_USERNAME_ERROR);
            }

            // Password
            try {
                newPassword = generatePassword(10).toString();
                account.setPassword(newPassword);
            } catch (Exception e) {
                throw new Exception(GENERATE_PASSWORD_ERROR);
            }

            // Name
            if (newAcc.getName() != null && !newAcc.getName().isEmpty() && stripAccents(newAcc.getName()).matches(NAME_PATTERN)) {
                account.setName(newAcc.getName());
            } else {
                throw new Exception(INVALID_NAME);
            }

            // Address
            if (newAcc.getAddress() != null && !newAcc.getAddress().isEmpty()) {
                account.setAddress(newAcc.getAddress().trim());
            } else {
                throw new Exception(NULL_OR_EMPTY_ADDRESS);
            }

            // Phone
            if (newAcc.getPhone() != null && !newAcc.getPhone().isEmpty()
                    && newAcc.getPhone().matches(PHONE_PATTERN)) {
                account.setPhone(newAcc.getPhone().trim());
            } else {
                throw new Exception(INVALID_PHONE_PATTERN);
            }

            // Email
            if (newAcc.getEmail() != null && !newAcc.getEmail().isEmpty()
                    && newAcc.getEmail().matches(EMAIL_PATTERN)) {
                account.setEmail(newAcc.getEmail().trim());
            } else {
                throw new Exception(INVALID_EMAIL_PATTERN);
            }

            // Image
            if (newAcc.getImage() != null && !newAcc.getImage().isEmpty()) {
                account.setImage(newAcc.getImage().trim());
            }

            // Role
            String userRole = newAcc.getRole();
            if (userRole.equalsIgnoreCase(ROLE_ADMIN) || userRole.equalsIgnoreCase(ROLE_MANAGER) || userRole.equalsIgnoreCase(ROLE_STAFF)
                    || userRole.equalsIgnoreCase(ROLE_TEACHER) || userRole.equalsIgnoreCase(ROLE_STUDENT)) {
                account.setRole(newAcc.getRole());
            } else {
                throw new Exception(INVALID_ROLE);
            }

            // Birthday
            if ((newAcc.getBirthday() != null) && (newAcc.getBirthday() != today)) {
                // Calculate years old
                int yo = today.getYear() - newAcc.getBirthday().getYear();
                // Role: ADMIN, MANAGER, STAFF
                // OLDER OR EQUAL 18
                if (userRole.equalsIgnoreCase(ROLE_ADMIN) && yo < 18) {
                    throw new Exception(INVALID_ADMIN_BIRTHDAY);
                }
                if (userRole.equalsIgnoreCase(ROLE_MANAGER) && yo < 18) {
                    throw new Exception(INVALID_MANAGER_BIRTHDAY);
                }
                if (userRole.equalsIgnoreCase(ROLE_STAFF) && yo < 18) {
                    throw new Exception(INVALID_STAFF_BIRTHDAY);
                }
                // Role: TEACHER
                // OLDER OR EQUAL 15
                if (userRole.equalsIgnoreCase(ROLE_TEACHER) && yo < 15) {
                    throw new Exception(INVALID_TEACHER_BIRTHDAY);
                }
                // Role: STUDENT
                // OLDER OR EQUAL 3
                if (userRole.equalsIgnoreCase(ROLE_STUDENT) && yo < 3) {
                    throw new Exception(INVALID_STUDENT_BIRTHDAY);
                }
                account.setBirthday(newAcc.getBirthday());
            } else {
                throw new Exception(INVALID_BIRTHDAY);
            }

            // Is Available
            account.setIsAvailable(true);

            // Creating Date
            account.setCreatingDate(today);

            // Temporary New Account
            Account accTmp = new Account();
            accTmp.setUsername(newUsername);
            accTmp.setPassword(newPassword);
            accTmp.setName(newAcc.getName());
            accTmp.setBirthday(newAcc.getBirthday());
            accTmp.setAddress(newAcc.getAddress());
            accTmp.setPhone(newAcc.getPhone());
            accTmp.setEmail(newAcc.getEmail());
            accTmp.setImage(newAcc.getImage());
            accTmp.setRole(userRole);
            accTmp.setIsAvailable(true);
            accTmp.setCreatingDate(today);
            accountRepository.save(accTmp);

            // Branch Id
            // Check Branch Id existence
            Branch branch = new Branch();
            if (branchRepository.existsById(newAcc.getBranchId())) {
                // Find Branch by newAcc's branch id
                branch = branchRepository.findByBranchId(newAcc.getBranchId());
            } else {
                throw new IllegalArgumentException(BRANCH_ID_NOT_EXIST);
            }

            // Role: ADMIN, MANAGER, STAFF
            if (userRole.equalsIgnoreCase(ROLE_ADMIN)
                    || userRole.equalsIgnoreCase(ROLE_MANAGER)
                    || userRole.equalsIgnoreCase(ROLE_STAFF)) {
                Account accountStaff = accountRepository.findOneByUsername(accTmp.getUsername());
                Staff staff = new Staff(accountStaff, branch);
                staffRepository.save(staff);
            }

            // Role: STUDENT
            if (userRole.equalsIgnoreCase(ROLE_STUDENT)) {
                Student student = new Student();
                // Insert Parent's name
                if (newAcc.getParentName() != null && !newAcc.getParentName().isEmpty() && stripAccents(newAcc.getName()).matches(NAME_PATTERN)) {
                    student.setParentName(newAcc.getParentName());
                } else {
                    throw   new Exception(INVALID_NAME);
                }
                // Insert Parent's phone
                if (newAcc.getParentPhone() != null && newAcc.getParentPhone().matches(PHONE_PATTERN)) {
                    student.setParentPhone(newAcc.getParentPhone());
                } else {
                    throw new Exception(INVALID_PHONE_PATTERN);
                }
                Account accountStudent = accountRepository.findOneByUsername(accTmp.getUsername());
                student.setAccount(accountStudent);
                student.setBranch(branch);
                studentRepository.save(student);
            }

            // Role: TEACHER
            if (userRole.equalsIgnoreCase(ROLE_TEACHER)) {
                Teacher teacher = new Teacher();
                TeachingBranch teachingBranch = new TeachingBranch();
                Account accountTeacher = accountRepository.findOneByUsername(accTmp.getUsername());
                if (newAcc.getExperience() != null && !newAcc.getExperience().isEmpty()) {
                    teacher.setAccount(accountTeacher);
                    teacher.setRating(null);
                    teacher.setExperience(newAcc.getExperience());
                    teacherRepository.save(teacher);
                    teachingBranch.setBranch(branch);
                    teachingBranch.setStartingDate(today);
                    teachingBranch.setTeacher(teacherRepository.findTeacherByAccount_Username(accountTeacher.getUsername()));
                    teachingBranchRepository.save(teachingBranch);
                } else {
                    throw new Exception(INVALID_TEACHER_EXP);
                }
            }
            SendEmailService sendEmailService = new SendEmailService();
            sendEmailService.sendGmail(account.getEmail(), account.getName(), account.getUsername(), account.getPassword());

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="5.0 Update Account">
    public ResponseEntity<?> updateAccount(String username, AccountRequestDto insAcc) throws Exception {
        try {
            Date today = new Date();

            // Check username existence
            if (accountRepository.existsByUsername(username)) {
                // Find user's role
                String userRole = accountRepository.findRoleByUsername(username);

                // Find account by username
                Account updateAcc = accountRepository.findOneByUsername(username);

                // Update Name
                if (insAcc.getName() != null && !insAcc.getName().isEmpty()) {
                    updateAcc.setName(insAcc.getName().trim());
                } else {
                    throw new Exception(NULL_OR_EMPTY_NAME);
                }

                // Update Address
                if (insAcc.getAddress() != null && !insAcc.getAddress().isEmpty()) {
                    updateAcc.setAddress(insAcc.getAddress().trim());
                } else {
                    throw new Exception(NULL_OR_EMPTY_ADDRESS);
                }

                // Update Email
                if (insAcc.getEmail() != null && !insAcc.getEmail().isEmpty()
                        && insAcc.getEmail().matches(EMAIL_PATTERN)) {
                    updateAcc.setEmail(insAcc.getEmail().trim());
                } else {
                    throw new Exception(INVALID_EMAIL_PATTERN);
                }

                // Update Birthday
                if ((insAcc.getBirthday() != null) && (insAcc.getBirthday() != today)) {
                    // Calculate years old
                    int yo = today.getYear() - insAcc.getBirthday().getYear();
                    // Role: ADMIN, MANAGER, STAFF
                    // OLDER OR EQUAL 18
                    if (userRole.equalsIgnoreCase(ROLE_ADMIN) && yo < 18) {
                        throw new Exception(INVALID_ADMIN_BIRTHDAY);
                    }
                    if (userRole.equalsIgnoreCase(ROLE_MANAGER) && yo < 18) {
                        throw new Exception(INVALID_MANAGER_BIRTHDAY);
                    }
                    if (userRole.equalsIgnoreCase(ROLE_STAFF) && yo < 18) {
                        throw new Exception(INVALID_STAFF_BIRTHDAY);
                    }
                    // Role: TEACHER
                    // OLDER OR EQUAL 15
                    if (userRole.equalsIgnoreCase(ROLE_TEACHER) && yo < 15) {
                        throw new Exception(INVALID_TEACHER_BIRTHDAY);
                    }
                    // Role: STUDENT
                    // OLDER OR EQUAL 3
                    if (userRole.equalsIgnoreCase(ROLE_STUDENT) && yo < 3) {
                        throw new Exception(INVALID_STUDENT_BIRTHDAY);
                    }
                    updateAcc.setBirthday(insAcc.getBirthday());
                } else {
                    throw new Exception(INVALID_BIRTHDAY);
                }

                // Update Phone
                if (insAcc.getPhone() != null && !insAcc.getPhone().isEmpty()
                        && insAcc.getPhone().matches(PHONE_PATTERN)) {
                    updateAcc.setPhone(insAcc.getPhone().trim());
                } else {
                    throw new Exception(INVALID_PHONE_PATTERN);
                }

                // Update Image
                if (insAcc.getImage() != null && !insAcc.getImage().isEmpty()) {
                    updateAcc.setImage(insAcc.getImage().trim());
                }

                // Update Is Available
                updateAcc.setIsAvailable(insAcc.getIsAvailable());

                // Update Branch Id
                // Check Branch Id existence
                if (branchRepository.existsById(insAcc.getBranchId())) {
                    // Find Branch by insAcc's branch id
                    Branch updateBranch = branchRepository.findByBranchId(insAcc.getBranchId());

                    // Role: ADMIN, MANAGER, STAFF
                    if (userRole.equalsIgnoreCase(ROLE_ADMIN)
                            || userRole.equalsIgnoreCase(ROLE_MANAGER)
                            || userRole.equalsIgnoreCase(ROLE_STAFF)) {
                        Staff staff = staffRepository.findByAccount_Username(username);
                        staff.setBranch(updateBranch);
                        staffRepository.save(staff);
                    } else
                        // Role: TEACHER
                        if (userRole.equalsIgnoreCase(ROLE_TEACHER)) {
                            // Find Teacher by username
                            Teacher teacher = teacherRepository.findTeacherByAccount_Username(username);
                            if (!teachingBranchRepository.existsByTeacher_TeacherIdAndBranch_BranchId(teacher.getTeacherId(), insAcc.getBranchId())) {
                                TeachingBranch newTeachingBranch = new TeachingBranch();
                                newTeachingBranch.setBranch(branchRepository.findByBranchId(insAcc.getBranchId()));
                                newTeachingBranch.setTeacher(teacher);
                                newTeachingBranch.setStartingDate(today);
                                teachingBranchRepository.save(newTeachingBranch);
                            } else {
                                throw new Exception(DUPLICATE_BRANCH_ID);
                            }
                        }
                        // Role: STUDENT
                        else {
                            Student student = studentRepository.findStudentByAccount_Username(username);
                            student.setBranch(updateBranch);
                            studentRepository.save(student);
                        }

                    // Update Parent's information
                    // Role: STUDENT
                    if (userRole.equalsIgnoreCase(ROLE_STUDENT)) {
                        Student student = studentRepository.findStudentByAccount_Username(username);
                        // Update Parent's name
                        if (insAcc.getParentName() != null && !insAcc.getParentName().isEmpty()) {
                            student.setParentName(insAcc.getParentName().trim());
                        } else {
                            throw new Exception(INVALID_NAME);
                        }

                        // Update Parent's phone
                        if (insAcc.getParentPhone() != null && insAcc.getParentPhone().matches(PHONE_PATTERN)) {
                            student.setParentPhone(insAcc.getParentPhone());
                        } else {
                            throw new Exception(INVALID_PHONE_PATTERN);
                        }

                        studentRepository.save(student);
                    }

                    // Update Teacher's Experience
                    // Role: TEACHER
                    if (userRole.equalsIgnoreCase(ROLE_TEACHER)) {
                        Teacher teacher = teacherRepository.findTeacherByAccount_Username(username);
                        if (insAcc.getExperience() != null && !insAcc.getExperience().isEmpty()) {
                            teacher.setExperience(insAcc.getExperience().trim());
                        } else {
                            throw new Exception(INVALID_TEACHER_EXP);
                        }
                        teacherRepository.save(teacher);
                    }

                    accountRepository.save(updateAcc);
                    return ResponseEntity.ok(true);
                } else {
                    throw new IllegalArgumentException(BRANCH_ID_NOT_EXIST);
                }
            } else {
                throw new IllegalArgumentException(USERNAME_NOT_EXIST);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
//</editor-fold>

    //<editor-fold desc="5.1 Update Role">
    public ResponseEntity<?> updateRole(String username, String role) throws Exception {
        try {
            String userRole = accountRepository.findRoleByUsername(username);
            Account account = accountRepository.findOneByUsername(username);

            if (userRole.equalsIgnoreCase(ROLE_MANAGER) || userRole.equalsIgnoreCase(ROLE_STAFF)) {
                if (role.equalsIgnoreCase(ROLE_MANAGER) || role.equalsIgnoreCase(ROLE_STAFF)) {
                    account.setRole(role);
                    return ResponseEntity.ok(true);
                } else {
                    throw new Exception(INVALID_NEW_ROLE);
                }
            } else {
                throw new Exception(INVALID_CHANGE_ROLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="6.0 Delete Account by UserName">
    public ResponseEntity<?> deleteByUserName(String userName) throws Exception {
        try {
            if (!accountRepository.existsByUsername(userName)) {
                throw new IllegalArgumentException(USERNAME_NOT_EXIST);
            } else {
                Account delAccount = accountRepository.findOneByUsername(userName);
                if (delAccount.getIsAvailable()) {
                    delAccount.setIsAvailable(false);
                    accountRepository.save(delAccount);
                    return ResponseEntity.ok(Boolean.TRUE);
                } else {
                    return ResponseEntity.ok(Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}