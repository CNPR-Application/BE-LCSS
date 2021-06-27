package cnpr.lcss.service;

import cnpr.lcss.dao.*;
import cnpr.lcss.model.AccountRequestDto;
import cnpr.lcss.model.LoginRequestDto;
import cnpr.lcss.model.LoginResponseDto;
import cnpr.lcss.model.StaffDto;
import cnpr.lcss.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    private static final String ROLE_MANAGER = "manger";
    private static final String ROLE_STAFF = "staff";
    private static final String ROLE_ADMIN = "admin";
    private static final String ROLE_TEACHER = "teacher";
    private static final String ROLE_STUDENT = "student";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9]+[@]{1}+[a-zA-Z0-9]+[.]{1}+([a-zA-Z0-9]+[.]{1})*+[a-zA-Z0-9]+$";
    private static final String PHONE_PATTERN = "(84|0[3|5|7|8|9])+([0-9]{8})\\b";
    private static final String PASSWORD_NOT_MATCH = "Password does not match!";
    private static final String USERNAME_NOT_EXIST = "Username does not exist!";
    private static final String BRANCH_ID_NOT_EXIST = "Brand Id does not exist!";
    private static final String INVALID_PARENT_NAME = "Parent's name is null or empty!";
    private static final String INVALID_PHONE_PATTERN = "Phone number is invalid!";
    private static final String UPDATE_TEACHER_EXP_ERROR = "Error at Update Teacher's Experience!";
    private static final String UPDATE_STUDENT_PARENT_INFO_ERROR = "Error at Update Student's parent's information!";
    private static final String UPDATE_BRANCH_ERROR = "Error at Update Branch!";

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

    //<editor-fold desc="Check login">
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

    //<editor-fold desc="Search Information by Username">
    public ResponseEntity<?> searchInfoByUsername(String username) throws Exception {
        try {
            if (accountRepository.existsByUsername(username)) {
                Map<String, Object> mapObj = new LinkedHashMap();
                Account account = accountRepository.findOneByUsername(username);

                mapObj.put("username", account.getUsername());
                mapObj.put("name", account.getName());
                mapObj.put("address", account.getAddress());
                mapObj.put("email", account.getEmail());
                mapObj.put("birthday", account.getBirthday());
                mapObj.put("phone", account.getPhone());
                mapObj.put("image", account.getImage());
                mapObj.put("role", account.getRole());
                mapObj.put("creatingDate", account.getCreatingDate());
                // Role: Admin, Manager, Staff
                if (account.getRole().equalsIgnoreCase(ROLE_MANAGER) || account.getRole().equalsIgnoreCase(ROLE_STAFF)
                        || account.getRole().equalsIgnoreCase(ROLE_ADMIN)) {
                    mapObj.put("branchId", staffRepository.findBranchIdByStaffUsername(username));
                    mapObj.put("branchName", staffRepository.findBranchNameByStaffUsername(username));
                } // Role: Teacher
                else if (account.getRole().equalsIgnoreCase(ROLE_TEACHER)) {
                    mapObj.put("branchId", teacherRepository.findBranchIdByTeacherUsername(username));
                    mapObj.put("branchName", teacherRepository.findBranchNameByTeacherUsername(username));
                } // Role: Student
                else if (account.getRole().equalsIgnoreCase(ROLE_STUDENT)) {
                    mapObj.put("branchId", studentRepository.findBranchIdByStudentUsername(username));
                    mapObj.put("branchName", studentRepository.findBranchNameByStudentUsername(username));
                } else {
                    throw new Exception();
                }
                // Role: Student
                if (account.getRole().equalsIgnoreCase(ROLE_STUDENT)) {
                    mapObj.put("parentPhone", studentRepository.findParentPhoneByStudentUsername(username));
                    mapObj.put("parentName", studentRepository.findParentNameByStudentUsername(username));
                } else {
                    mapObj.put("parentPhone", null);
                    mapObj.put("parentName", null);
                }
                // Role: Teacher
                if (account.getRole().equalsIgnoreCase(ROLE_TEACHER)) {
                    mapObj.put("experience", teacherRepository.findExperienceByTeacherUsername(username));
                    mapObj.put("rating", teacherRepository.findRatingByTeacherUsername(username));
                } else {
                    mapObj.put("experience", null);
                    mapObj.put("rating", null);
                }

                return ResponseEntity.ok(mapObj);
            } else {
                throw new Exception(USERNAME_NOT_EXIST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Update Account">
    public ResponseEntity<?> updateAccount(String username, AccountRequestDto insAcc) throws Exception {
        try {
            // Check username existence
            if (accountRepository.existsByUsername(username)) {
                // Find user's role
                String userRole = accountRepository.findRoleByUsername(username);

                // Find account by username
                Account updateAcc = accountRepository.findOneByUsername(username);

                // Update Name
                if (insAcc.getName() != null && !insAcc.getName().isEmpty()) {
                    updateAcc.setName(insAcc.getName().trim());
                }

                // Update Address
                if (insAcc.getAddress() != null && !insAcc.getAddress().isEmpty()) {
                    updateAcc.setAddress(insAcc.getAddress().trim());
                }

                // Update Email
                if (insAcc.getEmail() != null && !insAcc.getEmail().isEmpty()
                        && insAcc.getEmail().matches(EMAIL_PATTERN)) {
                    updateAcc.setEmail(insAcc.getEmail().trim());
                }

                // Update Birthday
                if (insAcc.getBirthday() != null) {
                    updateAcc.setBirthday(insAcc.getBirthday());
                }

                // Update Phone
                if (insAcc.getPhone() != null && !insAcc.getPhone().isEmpty()
                        && insAcc.getPhone().matches(PHONE_PATTERN)) {
                    updateAcc.setPhone(insAcc.getPhone().trim());
                }

                // Update Image
                if (insAcc.getImage() != null && !insAcc.getImage().isEmpty()) {
                    updateAcc.setImage(insAcc.getImage().trim());
                }

                // Update Branch Id
                // Check Branch Id existence
                if (branchRepository.existsById(insAcc.getBranchId())) {
                    // Find Branch by insAcc's branch id
                    Branch updateBranch = branchRepository.findByBranchId(insAcc.getBranchId());

                    try {
                        // Role: ADMIN, MANAGER, STAFF
                        if (userRole.equalsIgnoreCase(ROLE_ADMIN)
                                || userRole.equalsIgnoreCase(ROLE_MANAGER)
                                || userRole.equalsIgnoreCase(ROLE_STAFF)) {
                            Staff staff = staffRepository.findStaffByStaffUsername(username);
                            staff.setBranch(updateBranch);
                            staffRepository.save(staff);
                        } else
                            // Role: TEACHER
                            if (userRole.equalsIgnoreCase(ROLE_TEACHER)) {
                                Date today = new Date();
                                Teacher teacher = teacherRepository.findTeacherByTeacherUsername(username);
                                List<TeachingBranch> teachingBranchList = teacher.getTeachingBranchList();
                                for (TeachingBranch teachingBranch : teachingBranchList) {
                                    if (!teachingBranch.getBranch().getBranchId().equals(updateBranch)) {
                                        TeachingBranch newTeachingBranch = new TeachingBranch(updateBranch, teacher, today);
                                        teachingBranchRepository.save(newTeachingBranch);
                                    }
                                }
                                teacherRepository.save(teacher);
                            }
                            // Role: STUDENT
                            else {
                                Student student = studentRepository.findStudentByStudentUsername(username);
                                student.setBranch(updateBranch);
                                studentRepository.save(student);
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception(UPDATE_BRANCH_ERROR);
                    }

                    // Update Parent's information
                    // Role: STUDENT
                    if (userRole.equalsIgnoreCase(ROLE_STUDENT)) {
                        try {
                            Student student = studentRepository.findStudentByStudentUsername(username);
                            // Update Parent's name
                            if (insAcc.getParentName() != null && !insAcc.getParentName().isEmpty()) {
                                student.setParentName(insAcc.getParentName().trim());
                            } else {
                                throw new Exception(INVALID_PARENT_NAME);
                            }

                            // Update Parent's phone
                            if (insAcc.getParentPhone() != null && insAcc.getParentPhone().matches(PHONE_PATTERN)) {
                                student.setParentPhone(insAcc.getParentPhone());
                            } else {
                                throw new Exception(INVALID_PHONE_PATTERN);
                            }

                            studentRepository.save(student);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new Exception(UPDATE_STUDENT_PARENT_INFO_ERROR);
                        }
                    }

                    // Update Teacher's Experience
                    // Role: TEACHER
                    if (userRole.equalsIgnoreCase(ROLE_TEACHER)) {
                        try {
                            Teacher teacher = teacherRepository.findTeacherByTeacherUsername(username);
                            if (insAcc.getExperience() != null && !insAcc.getExperience().isEmpty()) {
                                teacher.setExperience(insAcc.getExperience().trim());
                            }
                            teacherRepository.save(teacher);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new Exception(UPDATE_TEACHER_EXP_ERROR);
                        }
                    }

                    accountRepository.save(updateAcc);
                    return ResponseEntity.ok(true);
                } else {
                    throw new IllegalArgumentException(BRANCH_ID_NOT_EXIST);
                }
            } else {
                throw new IllegalArgumentException(USERNAME_NOT_EXIST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}