package cnpr.lcss.model;

import lombok.Data;

@Data
public class NotificationRequestDto {
    //sender ID in firebase prj setting
    private String target;
    private String title;
    private String body;
}
