package cnpr.lcss.errorMessageConfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorConstant {

    /**
     * LOGIN FEATURE
     */
    private String LACK_OF_USERNAME_OR_PASSWORD;
    private String USERNAME_NOT_EXIST;
    private String PASSWORD_NOT_MATCH;
}
