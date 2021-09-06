package cnpr.lcss.dao;

import cnpr.lcss.model.TeacherDto;
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

    //<editor-fold desc="Modify Constructor">
    public Teacher(String experience, String rating, Account account) {
        this.experience = experience;
        this.rating = rating;
        this.account = account;
    }
    //</editor-fold>

    //<editor-fold desc="Convert to TeacherDto">
    public TeacherDto convertToTeacherDto() {
        TeacherDto dto = new TeacherDto();

        dto.setTeacherId(teacherId);
        dto.setUsername(account.getUsername());
        dto.setExperience(experience);
        dto.setRating(rating);
        dto.setName(account.getName());
        dto.setAddress(account.getAddress());
        dto.setEmail(account.getEmail());
        dto.setBirthday(account.getBirthday());
        dto.setPhone(account.getPhone());
        dto.setImage(account.getImage());
        dto.setRole(account.getRole());
        dto.setCreatingDate(account.getCreatingDate());

        return dto;
    }
    //</editor-fold>
}