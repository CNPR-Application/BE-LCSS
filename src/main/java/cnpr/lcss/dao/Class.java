package cnpr.lcss.dao;

import cnpr.lcss.model.ClassDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "class")
public class Class implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private int classId;
    @Column(name = "class_name")
    private String className;
    @Column(name = "opening_date")
    private Date openingDate;
    @Column(name = "status")
    private String status;
    @Column(name = "slot")
    private int slot;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "creator")
    private Staff staff;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "aClass")
    @JsonIgnore
    private List<StudentInClass> studentInClassList;
    @OneToMany(mappedBy = "aClass")
    @JsonIgnore
    private List<Session> sessionList;
    @OneToMany(mappedBy = "aClass")
    @JsonIgnore
    private List<Booking> bookingList;

    //<editor-fold desc="Convert to ClassDto">
    public ClassDto convertToDto() {
        ClassDto dto = new ClassDto();
        dto.setClassId(classId);
        dto.setClassName(className);
        dto.setOpeningDate(openingDate);
        dto.setStatus(status);
        dto.setSlot(slot);
        dto.setSubjectId(subject.getSubjectId());
        dto.setSubjectPrice(subject.getPrice());
        dto.setBranchId(branch.getBranchId());
        dto.setShiftId(shift.getShiftId());
        if (staff != null) {
            dto.setManagerId(staff.getId());
            dto.setManagerUsername(staff.getAccount().getUsername());
        } else {
            dto.setManagerId(0);
            dto.setManagerUsername(null);
        }
        dto.setRoomId(room.getRoomId());
        return dto;
    }
    //</editor-fold>

    //<editor-fold desc="Modify toString()">
    @Override
    public String toString() {
        return "Class{" +
                "classId=" + classId +
                ", className='" + className + '\'' +
                ", openingDate=" + openingDate +
                ", status='" + status + '\'' +
                ", slot=" + slot +
                '}';
    }
    //</editor-fold>
}
