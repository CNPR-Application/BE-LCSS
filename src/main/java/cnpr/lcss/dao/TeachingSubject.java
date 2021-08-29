package cnpr.lcss.dao;

import cnpr.lcss.model.TeachingSubjectBasicInfoDto;
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
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    //<editor-fold desc="Convert to TeachingSubjectBasicInfoDto">
    public TeachingSubjectBasicInfoDto convertToTeachingBranchBasicInfoDto() {
        TeachingSubjectBasicInfoDto dto = new TeachingSubjectBasicInfoDto();

        dto.setSubjectId(subject.getSubjectId());
        dto.setSubjectName(subject.getSubjectName());

        return dto;
    }
    //</editor-fold>
}
