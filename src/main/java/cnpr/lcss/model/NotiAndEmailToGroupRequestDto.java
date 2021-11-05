package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotiAndEmailToGroupRequestDto {
    List<String> username;
    private String senderUsername;
    private String title;
    private String body;
    private String className;
    private String subjectName;
    private String oldOpeningDate;
    private String newOpeningDate;
}
