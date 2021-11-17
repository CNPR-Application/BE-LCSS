package cnpr.lcss.dao;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import cnpr.lcss.model.TeacherBasisDetailDto;
import cnpr.lcss.model.TeacherDto;
import cnpr.lcss.model.TeacherInBranchDto;
import cnpr.lcss.util.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "teacher")
public class Teacher implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private int teacherId;
    @Column(name = "experience")
    private String experience;
    @Column(name = "rating")
    private String rating;

    @OneToOne
    @JoinColumn(name = "teacher_username", referencedColumnName = "username")
    private Account account;

    @OneToMany(mappedBy = "teacher")
    private List<TeachingBranch> teachingBranchList;
    @OneToMany(mappedBy = "teacher")
    private List<Session> sessionList;
    @OneToMany(mappedBy = "teacher")
    private List<TeachingSubject> teachingSubjectList;

    // <editor-fold desc="Modify Constructor">
    public Teacher(String experience, String rating, Account account) {
        this.experience = experience;
        this.rating = rating;
        this.account = account;
    }
    // </editor-fold>

    // <editor-fold desc="Modify toString">
    @Override
    public String toString() {
        return "Teacher{" + "teacherId=" + teacherId + ", experience='" + experience + '\'' + ", rating='" + rating
                + '\'' + '}';
    }
    // </editor-fold>

    // <editor-fold desc="Convert to TeacherDto">
    public TeacherDto convertToTeacherDto() {
        TeacherDto dto = new TeacherDto();
        dto.setTeacherId(teacherId);
        dto.setUsername(account.getUsername());
        dto.setExperience(experience);
        dto.setRating(Constant.calculateRating(rating));
        dto.setName(account.getName());
        dto.setAddress(account.getAddress());
        dto.setEmail(account.getEmail());
        dto.setBirthday(account.getBirthday());
        dto.setPhone(account.getPhone());
        dto.setImage(account.getImage());
        dto.setRole(account.getRole().getRoleId());
        dto.setCreatingDate(account.getCreatingDate());
        return dto;
    }
    // </editor-fold>

    // <editor-fold desc="Convert to TeacherInBranchDto">
    public TeacherInBranchDto convertToTeacherInBranchDto() {
        TeacherInBranchDto dto = new TeacherInBranchDto();
        dto.setTeacherId(teacherId);
        dto.setTeacherUsername(account.getUsername());
        dto.setTeacherName(account.getName());
        dto.setTeacherAddress(account.getAddress());
        dto.setTeacherEmail(account.getEmail());
        dto.setTeacherBirthday(account.getBirthday());
        dto.setTeacherPhone(account.getPhone());
        dto.setTeacherImage(account.getImage());
        dto.setRole(account.getRole().getRoleId());
        dto.setAccountCreatingDate(account.getCreatingDate());
        dto.setTeacherExperience(experience);
        dto.setTeacherRating(Constant.calculateRating(rating));
        return dto;
    }
    // </editor-fold>

    // <editor-fold desc="Convert to TeacherBasisDetailDto">
    public TeacherBasisDetailDto convertToTeacherBasisDetailDto() {
        TeacherBasisDetailDto dto = new TeacherBasisDetailDto();
        dto.setTeacherId(teacherId);
        dto.setTeacherName(account.getName());
        dto.setTeacherUsername(account.getUsername());
        dto.setTeacherImage(account.getImage());
        return dto;
    }
    // </editor-fold>
}