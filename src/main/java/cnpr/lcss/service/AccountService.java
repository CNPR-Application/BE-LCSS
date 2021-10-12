package cnpr.lcss.service;

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

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    TeachingBranchRepository teachingBranchRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    RoleRepository roleRepository;

    //<editor-fold desc="Convert Vietnamese characters to ASCII">
    public static String convertToASCII(String str) throws UnsupportedEncodingException {
        str = str.replaceAll(Constant.aLower, Constant.aLowerAscii);
        str = str.replaceAll(Constant.eLower, Constant.eLowerAscii);
        str = str.replaceAll(Constant.iLower, Constant.iLowerAscii);
        str = str.replaceAll(Constant.oLower, Constant.oLowerAscii);
        str = str.replaceAll(Constant.uLower, Constant.uLowerAscii);
        str = str.replaceAll(Constant.yLower, Constant.yLowerAscii);
        str = str.replaceAll(Constant.dLower, Constant.dLowerAscii);

        str = str.replaceAll(Constant.aUpper, Constant.aUpperAscii);
        str = str.replaceAll(Constant.eUpper, Constant.eUpperAscii);
        str = str.replaceAll(Constant.iUpper, Constant.iUpperAscii);
        str = str.replaceAll(Constant.oUpper, Constant.oUpperAscii);
        str = str.replaceAll(Constant.uUpper, Constant.uUpperAscii);
        str = str.replaceAll(Constant.yUpper, Constant.yUpperAscii);
        str = str.replaceAll(Constant.dUpper, Constant.dUpperAscii);
        return str;
    }
    //</editor-fold>

    //<editor-fold desc="Generate Username">
    private String generateUsername(String name, String role) throws Exception {
        String username;
        name = convertToASCII(name);

        if (name != null && !name.isEmpty() && name.matches(Constant.NAME_PATTERN)) {
            String[] parts = name.split("\\s+");
            // Get First Name
            username = parts[parts.length - 1];

            // ADMIN
            if (role.equalsIgnoreCase(Constant.ROLE_ADMIN)) {
                username += Constant.ROLE_ADMIN_CODE + String.format("%06d", (accountRepository.countByRole_RoleId(Constant.ROLE_ADMIN) + 1));
            }

            // MANAGER
            if (role.equalsIgnoreCase(Constant.ROLE_MANAGER)) {
                username += Constant.ROLE_MANAGER_CODE + String.format("%06d", (accountRepository.countByRole_RoleId(Constant.ROLE_MANAGER) + 1));
            }

            // STAFF
            if (role.equalsIgnoreCase(Constant.ROLE_STAFF)) {
                username += Constant.ROLE_STAFF_CODE + String.format("%06d", (accountRepository.countByRole_RoleId(Constant.ROLE_STAFF) + 1));
            }

            // TEACHER
            if (role.equalsIgnoreCase(Constant.ROLE_TEACHER)) {
                username += Constant.ROLE_TEACHER_CODE + String.format("%06d", (accountRepository.countByRole_RoleId(Constant.ROLE_TEACHER) + 1));
            }

            // STUDENT
            if (role.equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                username += Constant.ROLE_STUDENT_CODE + String.format("%06d", (accountRepository.countByRole_RoleId(Constant.ROLE_STUDENT) + 1));
            }
        } else {
            throw new Exception(Constant.INVALID_NAME);
        }
        return username.toLowerCase(Locale.ROOT);
    }
    //</editor-fold>

    //<editor-fold desc="Generate Password">
    private String generatePassword(int length) {
        final String chars = Constant.CAPITAL_CASE_LETTERS
                + Constant.LOWER_CASE_LETTERS
                + Constant.SPECIAL_CHARACTERS
                + Constant.NUMBERS;

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

    //<editor-fold desc="1.01 Check login">
    public ResponseEntity<?> checkLogin(LoginRequestDto loginRequest) throws Exception {
        LoginResponseDto loginResponseDto = new LoginResponseDto();

        try {
            if (loginRequest.getUsername() == null || loginRequest.getUsername().isEmpty()
                    || loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                throw new NullPointerException();
            } else {
                if (accountRepository.existsById(loginRequest.getUsername())) {
                    Account acc = accountRepository.findOneByUsername(loginRequest.getUsername());

                    // Check whether account is available or not
                    if (!acc.getIsAvailable()) {
                        throw new Exception(Constant.INVALID_IS_AVAILABLE);
                    }

                    String accUsername = acc.getUsername();
                    if (acc.getPassword().equals(loginRequest.getPassword())) {
                        loginResponseDto.setName(acc.getName());
                        loginResponseDto.setAddress(acc.getAddress());
                        loginResponseDto.setEmail(acc.getEmail());
                        loginResponseDto.setBirthday(acc.getBirthday());
                        loginResponseDto.setPhone(acc.getPhone());
                        loginResponseDto.setImage(acc.getImage());
                        loginResponseDto.setRole(acc.getRole().getRoleId());
                        loginResponseDto.setCreatingDate(acc.getCreatingDate());

                        // Return Branch Id, Branch Name
                        // Role: Admin, Manager, Staff
                        if (acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_MANAGER) || acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_STAFF)
                                || acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_ADMIN)) {
                            loginResponseDto.setBranchId(staffRepository.findBranchIdByStaffUsername(accUsername));
                            loginResponseDto.setBranchName(staffRepository.findBranchNameByStaffUsername(accUsername));
                        } // Role: Teacher
                        else if (acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_TEACHER)) {
                            loginResponseDto.setBranchId(teacherRepository.findBranchIdByTeacherUsername(accUsername));
                            loginResponseDto.setBranchName(teacherRepository.findBranchNameByTeacherUsername(accUsername));
                        } // Role: Student
                        else if (acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                            loginResponseDto.setBranchId(studentRepository.findBranchIdByStudentUsername(accUsername));
                            loginResponseDto.setBranchName(studentRepository.findBranchNameByStudentUsername(accUsername));
                        } else {
                            throw new Exception();
                        }

                        // Return Parent's information
                        // Role: Student
                        if (acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                            loginResponseDto.setParentName(studentRepository.findParentNameByStudentUsername(accUsername));
                            loginResponseDto.setParentPhone(studentRepository.findParentPhoneByStudentUsername(accUsername));
                        } else {
                            loginResponseDto.setParentName(null);
                            loginResponseDto.setParentPhone(null);
                        }

                        // Return Experience, Rating
                        // Role: Teacher
                        if (acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_TEACHER)) {
                            loginResponseDto.setExperience(teacherRepository.findExperienceByTeacherUsername(accUsername));
                            loginResponseDto.setRating(teacherRepository.findRatingByTeacherUsername(accUsername));
                        } else {
                            loginResponseDto.setExperience(null);
                            loginResponseDto.setRating(null);
                        }
                        return ResponseEntity.ok(loginResponseDto);
                    } else {
                        throw new Exception(Constant.PASSWORD_NOT_MATCH);
                    }
                } else {
                    throw new Exception(Constant.INVALID_USERNAME);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="1.02 Search Account Like Username and Paging">
    public ResponseEntity<?> searchAccountLikeUsernamePaging(String role, String keyword, boolean isAvailable, int pageNo, int pageSize) throws Exception {
        try {
            // Check Role existence
            if (role.equalsIgnoreCase(Constant.ROLE_ADMIN) || role.equalsIgnoreCase(Constant.ROLE_MANAGER) || role.equalsIgnoreCase(Constant.ROLE_STAFF)
                    || role.equalsIgnoreCase(Constant.ROLE_TEACHER) || role.equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                // pageNo starts at 0
                // always set first page = 1 ---> pageNo - 1
                Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

                Page<Account> page = accountRepository.findByRole_RoleIdAndUsernameContainingAndIsAvailable(role, keyword, isAvailable, pageable);
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
                    accDto.setRole(acc.getRole().getRoleId());
                    accDto.setCreatingDate(acc.getCreatingDate());
                    accDto.setIsAvailable(acc.getIsAvailable());
                    // Branch
                    // Role: Admin, Manager, Staff
                    if (role.equalsIgnoreCase(Constant.ROLE_MANAGER) || role.equalsIgnoreCase(Constant.ROLE_STAFF)
                            || role.equalsIgnoreCase(Constant.ROLE_ADMIN)) {
                        List<Branch> branchList = branchRepository.findStaffBranchByAccountUsername(acc.getUsername());
                        List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                        accDto.setBranchResponseDtoList(branchResponseDtoList);
                    } // Role: Teacher
                    else if (role.equalsIgnoreCase(Constant.ROLE_TEACHER)) {
                        List<Branch> branchList = branchRepository.findTeacherBranchByAccountUsername(acc.getUsername());
                        List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                        accDto.setBranchResponseDtoList(branchResponseDtoList);
                    } // Role: Student
                    else if (role.equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                        List<Branch> branchList = branchRepository.findStudentBranchByAccountUsername(acc.getUsername());
                        List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                        accDto.setBranchResponseDtoList(branchResponseDtoList);
                    }
                    // Parent's information
                    // Role: Student
                    if (role.equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                        accDto.setParentPhone(studentRepository.findParentPhoneByStudentUsername(acc.getUsername()));
                        accDto.setParentName(studentRepository.findParentNameByStudentUsername(acc.getUsername()));
                    } else {
                        accDto.setParentPhone(null);
                        accDto.setParentName(null);
                    }
                    // Teacher's exp & rating
                    // Role: Teacher
                    if (role.equalsIgnoreCase(Constant.ROLE_TEACHER)) {
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
                throw new Exception(Constant.INVALID_ROLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="1.03 Search Account Like Name">
    public ResponseEntity<?> searchAccountLikeNamePaging(String role, String keyword, int pageNo, int pageSize) throws Exception {
        try {
            // Check Role existence
            if (role.equalsIgnoreCase(Constant.ROLE_ADMIN) || role.equalsIgnoreCase(Constant.ROLE_MANAGER) || role.equalsIgnoreCase(Constant.ROLE_STAFF)
                    || role.equalsIgnoreCase(Constant.ROLE_TEACHER) || role.equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                // pageNo starts at 0
                // always set first page = 1 ---> pageNo - 1
                Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

                Page<Account> page = accountRepository.findByRole_RoleIdAndNameContainingIgnoreCase(role, keyword, pageable);
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
                    accDto.setRole(acc.getRole().getRoleId());
                    accDto.setCreatingDate(acc.getCreatingDate());
                    accDto.setIsAvailable(acc.getIsAvailable());
                    // Branch
                    // Role: Admin, Manager, Staff
                    if (role.equalsIgnoreCase(Constant.ROLE_MANAGER) || role.equalsIgnoreCase(Constant.ROLE_STAFF)
                            || role.equalsIgnoreCase(Constant.ROLE_ADMIN)) {
                        List<Branch> branchList = branchRepository.findStaffBranchByAccountUsername(acc.getUsername());
                        List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                        accDto.setBranchResponseDtoList(branchResponseDtoList);
                    } // Role: Teacher
                    else if (role.equalsIgnoreCase(Constant.ROLE_TEACHER)) {
                        List<Branch> branchList = branchRepository.findTeacherBranchByAccountUsername(acc.getUsername());
                        List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                        accDto.setBranchResponseDtoList(branchResponseDtoList);
                    } // Role: Student
                    else if (role.equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                        List<Branch> branchList = branchRepository.findStudentBranchByAccountUsername(acc.getUsername());
                        List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                        accDto.setBranchResponseDtoList(branchResponseDtoList);
                    }
                    // Parent's information
                    // Role: Student
                    if (role.equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                        accDto.setParentPhone(studentRepository.findParentPhoneByStudentUsername(acc.getUsername()));
                        accDto.setParentName(studentRepository.findParentNameByStudentUsername(acc.getUsername()));
                    } else {
                        accDto.setParentPhone(null);
                        accDto.setParentName(null);
                    }
                    // Teacher's exp & rating
                    // Role: Teacher
                    if (role.equalsIgnoreCase(Constant.ROLE_TEACHER)) {
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
                throw new Exception(Constant.INVALID_ROLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="1.04 Search Information by Username">
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
                accDto.setRole(acc.getRole().getRoleId());
                accDto.setCreatingDate(acc.getCreatingDate());
                accDto.setIsAvailable(acc.getIsAvailable());
                // Role: Admin, Manager, Staff
                if (acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_MANAGER) || acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_STAFF)
                        || acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_ADMIN)) {
                    List<Branch> branchList = branchRepository.findStaffBranchByAccountUsername(acc.getUsername());
                    List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                    accDto.setBranchResponseDtoList(branchResponseDtoList);
                } // Role: Teacher
                else if (acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_TEACHER)) {

                    List<Branch> branchList = branchRepository.findTeacherBranchByAccountUsername(acc.getUsername());
                    List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                    accDto.setBranchResponseDtoList(branchResponseDtoList);

                } // Role: Student
                else if (acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                    List<Branch> branchList = branchRepository.findStudentBranchByAccountUsername(acc.getUsername());
                    List<BranchResponseDto> branchResponseDtoList = branchList.stream().map(branch -> branch.convertToBranchResponseDto()).collect(Collectors.toList());
                    accDto.setBranchResponseDtoList(branchResponseDtoList);

                } else {
                    throw new Exception();
                }
                // Role: Student
                if (acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                    accDto.setParentPhone(studentRepository.findParentPhoneByStudentUsername(acc.getUsername()));
                    accDto.setParentName(studentRepository.findParentNameByStudentUsername(acc.getUsername()));
                } else {
                    accDto.setParentPhone(null);
                    accDto.setParentName(null);

                }
                // Role: Teacher
                if (acc.getRole().getRoleId().equalsIgnoreCase(Constant.ROLE_TEACHER)) {
                    accDto.setExperience(teacherRepository.findExperienceByTeacherUsername(acc.getUsername()));
                    accDto.setRating(teacherRepository.findRatingByTeacherUsername(acc.getUsername()));

                } else {
                    accDto.setExperience(null);
                    accDto.setRating(null);
                }
                return ResponseEntity.ok(accDto);
            } else {
                throw new Exception(Constant.INVALID_USERNAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="1.05 Create New Account">
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<?> createNewAccount(NewAccountRequestDto newAcc) throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(Constant.TIMEZONE));
        Date today = calendar.getTime();
        HashMap<String, Object> mapObj = new LinkedHashMap<>();

        try {
            Account account = new Account();
            String newUsername;
            String newPassword;

            // Username
            try {
                do {
                    newUsername = generateUsername(newAcc.getName(), newAcc.getRole());
                    account.setUsername(newUsername);
                } while (accountRepository.existsByUsername(account.getUsername()));
            } catch (Exception e) {
                throw new Exception(Constant.ERROR_GENERATE_USERNAME);
            }

            // Password
            try {
                newPassword = generatePassword(10);
                account.setPassword(newPassword);
            } catch (Exception e) {
                throw new Exception(Constant.ERROR_GENERATE_PASSWORD);
            }

            // Name
            if (newAcc.getName() != null && !newAcc.getName().isEmpty() && convertToASCII(newAcc.getName()).matches(Constant.NAME_PATTERN)) {
                account.setName(newAcc.getName().trim());
            } else {
                throw new Exception(Constant.INVALID_NAME);
            }

            // Address
            if (newAcc.getAddress() != null && !newAcc.getAddress().isEmpty()) {
                account.setAddress(newAcc.getAddress().trim());
            } else {
                throw new Exception(Constant.INVALID_ADDRESS);
            }

            // Phone
            if (newAcc.getPhone() != null && !newAcc.getPhone().isEmpty()
                    && newAcc.getPhone().matches(Constant.PHONE_PATTERN)) {
                account.setPhone(newAcc.getPhone().trim());
            } else {
                throw new Exception(Constant.INVALID_PHONE_PATTERN);
            }

            // Email
            if (newAcc.getEmail() != null && !newAcc.getEmail().isEmpty()
                    && newAcc.getEmail().matches(Constant.EMAIL_PATTERN)) {
                account.setEmail(newAcc.getEmail().trim());
            } else {
                throw new Exception(Constant.INVALID_EMAIL_PATTERN);
            }

            // Image
            if (newAcc.getImage() != null && !newAcc.getImage().isEmpty()) {
                account.setImage(newAcc.getImage().trim());
            }

            // Role
            String insRole = newAcc.getRole();
            Role userRole = roleRepository.findByRoleIdAllIgnoreCase(insRole);
            if (insRole.equalsIgnoreCase(Constant.ROLE_ADMIN)
                    || insRole.equalsIgnoreCase(Constant.ROLE_MANAGER)
                    || insRole.equalsIgnoreCase(Constant.ROLE_STAFF)
                    || insRole.equalsIgnoreCase(Constant.ROLE_TEACHER)
                    || insRole.equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                account.setRole(userRole);
            } else {
                throw new Exception(Constant.INVALID_ROLE);
            }

            // Birthday
            if ((newAcc.getBirthday() != null) && (newAcc.getBirthday() != today)) {
                // Calculate years old
                int yo = today.getYear() - newAcc.getBirthday().getYear();
                // Role: ADMIN, MANAGER, STAFF
                // OLDER OR EQUAL 18
                if (insRole.equalsIgnoreCase(Constant.ROLE_ADMIN) && yo < 18) {
                    throw new Exception(Constant.INVALID_ADMIN_BIRTHDAY);
                }
                if (insRole.equalsIgnoreCase(Constant.ROLE_MANAGER) && yo < 18) {
                    throw new Exception(Constant.INVALID_MANAGER_BIRTHDAY);
                }
                if (insRole.equalsIgnoreCase(Constant.ROLE_STAFF) && yo < 18) {
                    throw new Exception(Constant.INVALID_STAFF_BIRTHDAY);
                }
                // Role: TEACHER
                // OLDER OR EQUAL 18
                if (insRole.equalsIgnoreCase(Constant.ROLE_TEACHER) && yo < 18) {
                    throw new Exception(Constant.INVALID_TEACHER_BIRTHDAY);
                }
                // Role: STUDENT
                // OLDER OR EQUAL 3
                if (insRole.equalsIgnoreCase(Constant.ROLE_STUDENT) && yo < 3) {
                    throw new Exception(Constant.INVALID_STUDENT_BIRTHDAY);
                }
                account.setBirthday(newAcc.getBirthday());
            } else {
                throw new Exception(Constant.INVALID_BIRTHDAY);
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
            boolean checkGmail = false;
            SendEmailService sendEmailService = new SendEmailService();
            checkGmail = sendEmailService.sendGmail(account.getEmail(), account.getName(), account.getUsername(), account.getPassword());
            if (checkGmail) {
                accountRepository.save(accTmp);
            } else {
                throw new Exception(Constant.ERROR_EMAIL_SENDING);
            }

            // Branch ID
            // Check Branch ID existence
            Branch branch = new Branch();
            if (branchRepository.existsById(newAcc.getBranchId())) {
                // Find Branch by newAcc's branch id
                branch = branchRepository.findByBranchId(newAcc.getBranchId());
            } else {
                throw new IllegalArgumentException(Constant.INVALID_BRANCH_ID);
            }

            // Role: ADMIN, MANAGER, STAFF
            if (insRole.equalsIgnoreCase(Constant.ROLE_ADMIN)
                    || insRole.equalsIgnoreCase(Constant.ROLE_MANAGER)
                    || insRole.equalsIgnoreCase(Constant.ROLE_STAFF)) {
                Account accountStaff = accountRepository.findOneByUsername(accTmp.getUsername());
                Staff staff = new Staff(accountStaff, branch);
                staffRepository.save(staff);
            }

            // Role: STUDENT
            if (insRole.equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                Student student = new Student();
                // Insert Parent's name
                student.setParentName(newAcc.getParentName());

                // Insert Parent's phone
                if (newAcc.getParentPhone() != null && !newAcc.getParentPhone().isEmpty()) {
                    if (newAcc.getParentPhone().matches(Constant.PHONE_PATTERN)) {
                        student.setParentPhone(newAcc.getParentPhone());
                    } else {
                        throw new Exception(Constant.INVALID_PHONE_PATTERN);
                    }
                } else {
                    student.setParentPhone(newAcc.getParentPhone());
                }
                Account accountStudent = accountRepository.findOneByUsername(accTmp.getUsername());
                student.setAccount(accountStudent);
                student.setBranch(branch);
                studentRepository.save(student);
            }

            // Role: TEACHER
            if (insRole.equalsIgnoreCase(Constant.ROLE_TEACHER)) {
                Teacher teacher = new Teacher();
                TeachingBranch teachingBranch = new TeachingBranch();
                Account accountTeacher = accountRepository.findOneByUsername(accTmp.getUsername());
                if (newAcc.getExperience() != null && !newAcc.getExperience().isEmpty()) {
                    teacher.setAccount(accountTeacher);
                    teacher.setRating(Constant.DEFAULT_TEACHER_RATING);
                    teacher.setExperience(newAcc.getExperience());
                    teacherRepository.save(teacher);
                    teachingBranch.setBranch(branch);
                    teachingBranch.setStartingDate(today);
                    teachingBranch.setTeacher(teacherRepository.findTeacherByAccount_Username(accountTeacher.getUsername()));
                    teachingBranchRepository.save(teachingBranch);
                } else {
                    throw new Exception(Constant.INVALID_TEACHER_EXP);
                }
            }
            mapObj.put("username", accTmp.getUsername());
            return ResponseEntity.ok(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="1.06 Update Account">
    public ResponseEntity<?> updateAccount(String username, AccountRequestDto insAcc) throws Exception {
        try {
            Date today = new Date();

            // Check username existence
            if (accountRepository.existsByUsername(username)) {
                // Find user's role
                Role userRole = accountRepository.findRoleByUsername(username);

                // Find account by username
                Account updateAcc = accountRepository.findOneByUsername(username);

                // Update Name
                if (insAcc.getName() != null && !insAcc.getName().isEmpty()) {
                    updateAcc.setName(insAcc.getName().trim());
                } else {
                    throw new Exception(Constant.INVALID_NAME);
                }

                // Update Address
                if (insAcc.getAddress() != null && !insAcc.getAddress().isEmpty()) {
                    updateAcc.setAddress(insAcc.getAddress().trim());
                } else {
                    throw new Exception(Constant.INVALID_ADDRESS);
                }

                // Update Email
                if (insAcc.getEmail() != null && !insAcc.getEmail().isEmpty()
                        && insAcc.getEmail().matches(Constant.EMAIL_PATTERN)) {
                    updateAcc.setEmail(insAcc.getEmail().trim());
                } else {
                    throw new Exception(Constant.INVALID_EMAIL_PATTERN);
                }

                // Update Birthday
                if ((insAcc.getBirthday() != null) && (insAcc.getBirthday() != today)) {
                    // Calculate years old
                    int yo = today.getYear() - insAcc.getBirthday().getYear();
                    // Role: ADMIN, MANAGER, STAFF
                    // OLDER OR EQUAL 18
                    if (userRole.getRoleId().equalsIgnoreCase(Constant.ROLE_ADMIN) && yo < 18) {
                        throw new Exception(Constant.INVALID_ADMIN_BIRTHDAY);
                    }
                    if (userRole.getRoleId().equalsIgnoreCase(Constant.ROLE_MANAGER) && yo < 18) {
                        throw new Exception(Constant.INVALID_MANAGER_BIRTHDAY);
                    }
                    if (userRole.getRoleId().equalsIgnoreCase(Constant.ROLE_STAFF) && yo < 18) {
                        throw new Exception(Constant.INVALID_STAFF_BIRTHDAY);
                    }
                    // Role: TEACHER
                    // OLDER OR EQUAL 18
                    if (userRole.getRoleId().equalsIgnoreCase(Constant.ROLE_TEACHER) && yo < 18) {
                        throw new Exception(Constant.INVALID_TEACHER_BIRTHDAY);
                    }
                    // Role: STUDENT
                    // OLDER OR EQUAL 3
                    if (userRole.getRoleId().equalsIgnoreCase(Constant.ROLE_STUDENT) && yo < 3) {
                        throw new Exception(Constant.INVALID_STUDENT_BIRTHDAY);
                    }
                    updateAcc.setBirthday(insAcc.getBirthday());
                } else {
                    throw new Exception(Constant.INVALID_BIRTHDAY);
                }

                // Update Phone
                if (insAcc.getPhone() != null && !insAcc.getPhone().isEmpty()
                        && insAcc.getPhone().matches(Constant.PHONE_PATTERN)) {
                    updateAcc.setPhone(insAcc.getPhone().trim());
                } else {
                    throw new Exception(Constant.INVALID_PHONE_PATTERN);
                }

                // Update Image
                if (insAcc.getImage() != null && !insAcc.getImage().isEmpty()) {
                    updateAcc.setImage(insAcc.getImage().trim());
                }

                // Update Is Available
                updateAcc.setIsAvailable(updateAcc.getIsAvailable());

                // Update Branch Id
                // Check Branch Id existence
                if (branchRepository.existsById(insAcc.getBranchId())) {
                    // Find Branch by insAcc's branch id
                    Branch updateBranch = branchRepository.findByBranchId(insAcc.getBranchId());

                    // Role: ADMIN, MANAGER, STAFF
                    if (userRole.getRoleId().equalsIgnoreCase(Constant.ROLE_ADMIN)
                            || userRole.getRoleId().equalsIgnoreCase(Constant.ROLE_MANAGER)
                            || userRole.getRoleId().equalsIgnoreCase(Constant.ROLE_STAFF)) {
                        Staff staff = staffRepository.findByAccount_Username(username);
                        staff.setBranch(updateBranch);
                        staffRepository.save(staff);
                    } else
                        // Role: TEACHER
                        if (userRole.getRoleId().equalsIgnoreCase(Constant.ROLE_TEACHER)) {
                            // Find Teacher by username
                            Teacher teacher = teacherRepository.findTeacherByAccount_Username(username);
                            if (!teachingBranchRepository.existsByTeacher_TeacherIdAndBranch_BranchId(teacher.getTeacherId(), insAcc.getBranchId())) {
                                TeachingBranch newTeachingBranch = new TeachingBranch();
                                newTeachingBranch.setBranch(branchRepository.findByBranchId(insAcc.getBranchId()));
                                newTeachingBranch.setTeacher(teacher);
                                newTeachingBranch.setStartingDate(today);
                                teachingBranchRepository.save(newTeachingBranch);
                            } else {
                                throw new Exception(Constant.DUPLICATE_BRANCH_ID);
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
                    if (userRole.getRoleId().equalsIgnoreCase(Constant.ROLE_STUDENT)) {
                        Student student = studentRepository.findStudentByAccount_Username(username);
                        // Update Parent's name
                        if (insAcc.getParentName() != null && !insAcc.getParentName().isEmpty()) {
                            student.setParentName(insAcc.getParentName().trim());
                        } else {
                            throw new Exception(Constant.INVALID_NAME);
                        }

                        // Update Parent's phone
                        if (insAcc.getParentPhone() != null && insAcc.getParentPhone().matches(Constant.PHONE_PATTERN)) {
                            student.setParentPhone(insAcc.getParentPhone());
                        } else {
                            throw new Exception(Constant.INVALID_PARENT_PHONE_PATTERN);
                        }

                        studentRepository.save(student);
                    }

                    // Update Teacher's Experience
                    // Role: TEACHER
                    if (userRole.getRoleId().equalsIgnoreCase(Constant.ROLE_TEACHER)) {
                        Teacher teacher = teacherRepository.findTeacherByAccount_Username(username);
                        if (insAcc.getExperience() != null && !insAcc.getExperience().isEmpty()) {
                            teacher.setExperience(insAcc.getExperience().trim());
                        } else {
                            throw new Exception(Constant.INVALID_TEACHER_EXP);
                        }
                        teacherRepository.save(teacher);
                    }

                    accountRepository.save(updateAcc);
                    return ResponseEntity.ok(true);
                } else {
                    throw new IllegalArgumentException(Constant.INVALID_BRANCH_ID);
                }
            } else {
                throw new IllegalArgumentException(Constant.INVALID_USERNAME);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
//</editor-fold>

    //<editor-fold desc="1.07 Update Role">
    public ResponseEntity<?> updateRole(String username, String role) throws Exception {
        try {
            Role userRole = roleRepository.findByRoleIdAllIgnoreCase(role);
            Account account = accountRepository.findOneByUsername(username);

            if (userRole.getRoleId().equalsIgnoreCase(Constant.ROLE_MANAGER) || userRole.getRoleId().equalsIgnoreCase(Constant.ROLE_STAFF)) {
                if (role.equalsIgnoreCase(Constant.ROLE_MANAGER) || role.equalsIgnoreCase(Constant.ROLE_STAFF)) {
                    account.setRole(userRole);
                    accountRepository.save(account);
                    return ResponseEntity.ok(true);
                } else {
                    throw new Exception(Constant.INVALID_NEW_ROLE);
                }
            } else {
                throw new Exception(Constant.INVALID_CHANGE_ROLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="1.08 Delete Account by UserName">
    public ResponseEntity<?> deleteByUserName(String userName) throws Exception {
        try {
            if (!accountRepository.existsByUsername(userName)) {
                throw new IllegalArgumentException(Constant.INVALID_USERNAME);
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

    //<editor-fold desc="1.09-change-password">
    public ResponseEntity<?> changePassword(String username, HashMap<String, Object> reqBody) throws Exception {
        try {

            Account account = accountRepository.findOneByUsername(username);
            if (account.equals(null))
                throw new Exception(Constant.INVALID_USERNAME);
            String newPassword = (String) reqBody.get("newPassword");
            String oldPassword = (String) reqBody.get("oldPassword");
            String reNewPassword = (String) reqBody.get("reNewPassword");
            if (!oldPassword.matches(account.getPassword())) {
                throw new Exception(Constant.PASSWORD_NOT_MATCH);
            }
            if (!newPassword.matches(oldPassword) && reNewPassword.matches(newPassword)) {
                account.setPassword(newPassword);
                accountRepository.save(account);
                return ResponseEntity.ok(Boolean.TRUE);
            } else {
                return ResponseEntity.ok(Boolean.FALSE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="1.12-search-teacher-in-branch">
    public ResponseEntity<?> searchTeacherInBranch(int branchId, boolean isAvailable, int pageNo, int pageSize) throws Exception {
        try {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<Teacher> page = teacherRepository
                    .findDistinctByTeachingBranchList_Branch_BranchIdAndAccount_IsAvailable(branchId, isAvailable, pageable);
            List<TeacherInBranchDto> teacherInBranchDtoList = page.getContent().stream()
                    .map(teacher -> teacher.convertToTeacherInBranchDto()).collect(Collectors.toList());
            for (TeacherInBranchDto teacher : teacherInBranchDtoList) {
                teacher.setTeacherStartingDate(teachingBranchRepository
                        .findByBranch_BranchIdAndTeacher_TeacherId(branchId, teacher.getTeacherId()).getStartingDate());
                teacher.setTeachingSubjectList(subjectRepository
                        .findDistinctByTeachingSubjectList_Teacher_TeacherId(teacher.getTeacherId()).stream()
                        .map(subject -> subject.convertToSubjectBasicInfoDto()).collect(Collectors.toList()));
            }
            HashMap<String, Object> mapObj = new LinkedHashMap<>();
            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);
            mapObj.put("totalPage", page.getTotalPages());
            mapObj.put("teacherInBranchList", teacherInBranchDtoList);
            return ResponseEntity.status(HttpStatus.OK).body(mapObj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}