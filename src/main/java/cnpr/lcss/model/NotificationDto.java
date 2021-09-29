package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDto {
    private int notificationId;
    private String senderUserName;
    private String receiverUserName;
    private String title;
    private String body;
    private boolean isRead;
    private Date creatingDate;
    private Date lastModified;

}
