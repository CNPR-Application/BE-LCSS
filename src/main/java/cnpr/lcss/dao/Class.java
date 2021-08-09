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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_id")
    private Shift shift;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "creator")
    private Staff staff;

    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<StudentInClass> studentInClassList;
    @OneToMany(mappedBy = "aClass", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Session> sessionList;
    @OneToMany(mappedBy = "aClass", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Booking> bookingList;

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
        dto.setManagerId(staff.getId());
        dto.setManagerUsername(staff.getAccount().getUsername());
        return dto;
    }

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
}
