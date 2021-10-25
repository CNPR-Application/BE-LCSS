package cnpr.lcss.dao;

import cnpr.lcss.model.TeachingSubjectBasicInfoDto;
import cnpr.lcss.model.TeachingSubjectDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "teaching_subject")
public class TeachingSubject implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    //<editor-fold desc="Modify toString">
    @Override
    public String toString() {
        return "TeachingSubject{" +
                "id=" + id +
                ", teacher=" + teacher +
                ", subject=" + subject +
                '}';
    }
    //</editor-fold>

    //<editor-fold desc="Convert to TeachingSubjectBasicInfoDto">
    public TeachingSubjectBasicInfoDto convertToTeachingBranchBasicInfoDto() {
        TeachingSubjectBasicInfoDto dto = new TeachingSubjectBasicInfoDto();

        dto.setSubjectId(subject.getSubjectId());
        dto.setSubjectName(subject.getSubjectName());

        return dto;
    }
    //</editor-fold>

    //<editor-fold desc="Convert to TeachingSubjectDto">
    public TeachingSubjectDto convertToTeachingSubjectDto() {
        TeachingSubjectDto dto = new TeachingSubjectDto();
        dto.setSubjectId(subject.getSubjectId());
        dto.setSubjectCode(subject.getSubjectCode());
        dto.setSubjectName(subject.getSubjectName());
        return dto;
    }
    //</editor-fold>
}
