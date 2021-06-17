package cnpr.lcss.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
}
