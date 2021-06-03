package cnpr.lcss.errorMessageConfig;

import org.springframework.beans.factory.annotation.Autowired;

public class ErrorMessage {

    @Autowired
    ErrorConstant errorConstant;

    /**
     * LOGIN FEATURE
     */
    public String LackOfUsernameOrPassword() {
        errorConstant.setLACK_OF_USERNAME_OR_PASSWORD("Username of Password is empty!");
        return errorConstant.getLACK_OF_USERNAME_OR_PASSWORD();
    }

    public String UsernameNotExist(String username) {
        errorConstant.setUSERNAME_NOT_EXIST("Username '" + username + "' not exist!");
        return errorConstant.getUSERNAME_NOT_EXIST();
    }

    public String PasswordNotMatch() {
        errorConstant.setPASSWORD_NOT_MATCH("Password not match!");
        return errorConstant.getPASSWORD_NOT_MATCH();
    }
}
