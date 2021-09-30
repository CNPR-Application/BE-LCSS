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
    private String senderUsername;
    private String receiverUsername;
    private String title;
    private String body;
    private boolean isRead;
    private Date creatingDate;
    private Date lastModified;

    //<editor-fold desc="Modify isRead">
    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    //</editor-fold>
}
