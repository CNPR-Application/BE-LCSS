package cnpr.lcss.dao;

import cnpr.lcss.model.SubjectDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "subject_detail")
public class SubjectDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_detail_id")
    private int subjectDetailId;
    @Column(name = "week_num")
    private int weekNum;
    @Column(name = "week_description")
    private String weekDescription;
    @Column(name = "learning_outcome")
    private String learningOutcome;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public SubjectDetail(int subjectDetailId, int weekNum, String weekDescription, String learningOutcome) {
        this.subjectDetailId = subjectDetailId;
        this.weekNum = weekNum;
        this.weekDescription = weekDescription;
        this.learningOutcome = learningOutcome;
    }

    public SubjectDetailDto convertToDto() {
        SubjectDetailDto subjectDetailDto = new SubjectDetailDto(subjectDetailId, weekNum, weekDescription, learningOutcome);
        return subjectDetailDto;
    }
}