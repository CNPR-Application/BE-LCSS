package cnpr.lcss.service;

import cnpr.lcss.dao.Account;
import cnpr.lcss.errorMessageConfig.ErrorMessage;
import cnpr.lcss.model.LoginRequestDto;
import cnpr.lcss.model.LoginResponseDto;
import cnpr.lcss.repository.AccountRepository;
import cnpr.lcss.repository.StaffRepository;
import cnpr.lcss.repository.StudentRepository;
import cnpr.lcss.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;

    ErrorMessage errorMessage;

    // Check login
    public LoginResponseDto checkLogin(LoginRequestDto loginRequest) throws Exception {
        LoginResponseDto loginResponseDto = new LoginResponseDto();

        try {
            if (loginRequest.getUsername() == null || loginRequest.getUsername().isEmpty()
                    || loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                throw new Exception(errorMessage.LackOfUsernameOrPassword());
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
                        if (acc.getRole().equalsIgnoreCase("manger") || acc.getRole().equalsIgnoreCase("staff")
                                || acc.getRole().equalsIgnoreCase("admin")) {
                            loginResponseDto.setBranchId(staffRepository.findBranchIdByStaffUsername(accUsername));
                            loginResponseDto.setBranchName(staffRepository.findBranchNameByStaffUsername(accUsername));
                        } // Role: Teacher
                        else if (acc.getRole().equalsIgnoreCase("teacher")) {
                            loginResponseDto.setBranchId(teacherRepository.findBranchIdByTeacherUsername(accUsername));
                            loginResponseDto.setBranchName(teacherRepository.findBranchNameByTeacherUsername(accUsername));
                        } // Role: Student
                        else if (acc.getRole().equalsIgnoreCase("student")) {
                            loginResponseDto.setBranchId(studentRepository.findBranchIdByStudentUsername(accUsername));
                            loginResponseDto.setBranchName(studentRepository.findBranchNameByStudentUsername(accUsername));
                        } else {
                            throw new Exception();
                        }

                        // Return Parent's information
                        // Role: Student
                        if (acc.getRole().equalsIgnoreCase("student")) {
                            loginResponseDto.setParentName(studentRepository.findParentNameByStudentUsername(accUsername));
                            loginResponseDto.setParentPhone(studentRepository.findParentPhoneByStudentUsername(accUsername));
                        } else {
                            loginResponseDto.setParentName(null);
                            loginResponseDto.setParentPhone(null);
                        }

                        // Return Experience, Rating
                        // Role: Teacher
                        if (acc.getRole().equalsIgnoreCase("teacher")) {
                            loginResponseDto.setExperience(teacherRepository.findExperienceByTeacherUsername(accUsername));
                            loginResponseDto.setRating(teacherRepository.findRatingByTeacherUsername(accUsername));
                        } else {
                            loginResponseDto.setExperience(null);
                            loginResponseDto.setRating(null);
                        }
                    } else {
                        throw new Exception(errorMessage.PasswordNotMatch());
                    }
                } else {
                    throw new Exception(errorMessage.UsernameNotExist(loginRequest.getUsername()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginResponseDto;
    }
}
