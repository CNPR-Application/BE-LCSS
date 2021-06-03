package cnpr.lcss.service;

import cnpr.lcss.dao.Account;
import cnpr.lcss.errorMessageConfig.ErrorMessage;
import cnpr.lcss.model.LoginRequestDto;
import cnpr.lcss.model.LoginResponseDto;
import cnpr.lcss.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
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
                    if (acc.getPassword().equals(loginRequest.getPassword())) {
                        loginResponseDto.setName(acc.getName());
                        loginResponseDto.setAddress(acc.getAddress());
                        loginResponseDto.setEmail(acc.getEmail());
                        loginResponseDto.setBirthday(acc.getBirthday());
                        loginResponseDto.setPhone(acc.getPhone());
                        loginResponseDto.setImage(acc.getImage());
                        loginResponseDto.setRole(acc.getRole());
                        loginResponseDto.setCreatingDate(acc.getCreatingDate());
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
