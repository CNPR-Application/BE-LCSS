package cnpr.lcss.dao;

import cnpr.lcss.model.RoomAndBranchDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "room")
public class Room implements Serializable {
    @Id
    @Column(name = "room_id")
    private Integer roomId;
    @Column(name = "room_name")
    private int roomName;
    @Column(name = "is_available")
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Class> classList;
    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Session> sessionList;

    //<editor-fold desc="Modify isAvailable">
    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    //</editor-fold>

    //<editor-fold desc="Convert to RoomAndBranchDto">
    public RoomAndBranchDto convertToRoomAndBranchDto() {
        RoomAndBranchDto dto = new RoomAndBranchDto();
        dto.setRoomId(roomId);
        dto.setRoomName(roomName);
        dto.setBranchId(branch.getBranchId());
        return dto;
    }
    //</editor-fold>
}
