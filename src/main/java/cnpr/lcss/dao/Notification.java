package cnpr.lcss.dao;

import cnpr.lcss.model.NotificationDto;
import cnpr.lcss.util.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "notification")
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "sender_username")
    private String senderUsername;
    @Column(name = "title")
    private String title;
    @Column(name = "body")
    private String body;
    @Column(name = "is_read")
    private boolean isRead;
    @Column(name = "creating_date")
    private Date creatingDate;
    @Column(name = "last_modified")
    private Date lastModified;

    @ManyToOne
    @JoinColumn(name = "receiver_username")
    private Account receiverUsername;

    //<editor-fold desc="Modify isRead">
    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    //</editor-fold>

    //<editor-fold desc="Convert to NotificationDto">
    public NotificationDto convertToNotificationDto() {
        NotificationDto notificationDto = new
                NotificationDto(
                id,
                senderUsername,
                receiverUsername.getUsername(),
                title, body, isRead,
                Constant.convertToUTC7TimeZone(creatingDate),
                Constant.convertToUTC7TimeZone(creatingDate));
        return notificationDto;
    }
    //</editor-fold>
}
