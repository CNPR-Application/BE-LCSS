package cnpr.lcss.service;

import cnpr.lcss.dao.Account;
import cnpr.lcss.model.LoginRequestDto;
import cnpr.lcss.model.LoginResponseDto;
import cnpr.lcss.repository.AccountRepository;
import cnpr.lcss.repository.StaffRepository;
import cnpr.lcss.repository.StudentRepository;
import cnpr.lcss.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AccountService {

    private final String ROLE_MANAGER = "manger";
    private final String ROLE_STAFF = "staff";
    private final String ROLE_ADMIN = "admin";
    private final String ROLE_TEACHER = "teacher";
    private final String ROLE_STUDENT = "student";
    private final String USERNAME_NOT_EXIST = "Username does not exist!";
    private final String PASSWORD_NOT_MATCH = "Password does not match!";

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;

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
}
