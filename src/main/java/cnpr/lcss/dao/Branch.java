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

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    private List<Class> classList;
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Staff> staffs;
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TeachingBranch> teachingBranches;
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Student> students;
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RegisteringGuest> registeringGuestList;
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Booking> bookingList;
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Room> roomList;

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

    public BranchResponseDto convertToBranchResponseDto() {
        BranchResponseDto dto = new BranchResponseDto(branchId, branchName);
        return dto;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "branchId=" + branchId +
                ", branchName='" + branchName + '\'' +
                ", address='" + address + '\'' +
                ", isAvailable=" + isAvailable +
                ", openingDate=" + openingDate +
                ", phone='" + phone + '\'' +
                '}';
    }
}
