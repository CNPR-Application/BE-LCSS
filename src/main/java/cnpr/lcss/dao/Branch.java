package cnpr.lcss.dao;

import cnpr.lcss.model.BranchResponseDto;
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
@Table(name = "branch")
public class Branch implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private int branchId;
    @Column(name = "branch_name")
    private String branchName;
    @Column(name = "address")
    private String address;
    @Column(name = "is_available")
    private Boolean isAvailable;
    @Column(name = "opening_date")
    private Date openingDate;
    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "branch")
    private List<Class> classList;
    @OneToMany(mappedBy = "branch")
    @JsonIgnore
    private List<Staff> staffs;
    @OneToMany(mappedBy = "branch")
    @JsonIgnore
    private List<TeachingBranch> teachingBranches;
    @OneToMany(mappedBy = "branch")
    @JsonIgnore
    private List<Student> students;
    @OneToMany(mappedBy = "branch")
    @JsonIgnore
    private List<RegisteringGuest> registeringGuestList;
    @OneToMany(mappedBy = "branch")
    @JsonIgnore
    private List<Booking> bookingList;
    @OneToMany(mappedBy = "branch")
    @JsonIgnore
    private List<Room> roomList;

    //<editor-fold desc="Modify Constructor">
    public Branch(int branchId, String branchName) {
        this.branchId = branchId;
        this.branchName = branchName;
    }

    public Branch(int branchId, String branchName, String address, Boolean isAvailable, Date openingDate, String phone) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.address = address;
        this.isAvailable = isAvailable;
        this.openingDate = openingDate;
        this.phone = phone;
    }
    //</editor-fold>

    //<editor-fold desc="Convert to BranchResponseDto">
    public BranchResponseDto convertToBranchResponseDto() {
        BranchResponseDto dto = new BranchResponseDto();
        dto.setBranchId(branchId);
        dto.setBranchName(branchName);
        return dto;
    }
    //</editor-fold>

    //<editor-fold desc="Modify isAvailable">
    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    //</editor-fold>
}
