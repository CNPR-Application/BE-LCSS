package cnpr.lcss.dao;

import cnpr.lcss.model.CurriculumResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "curriculum")
public class Curriculum implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curriculum_id")
    private int curriculumId;
    @Column(name = "curriculum_code")
    private String curriculumCode;
    @Column(name = "curriculum_name")
    private String curriculumName;
    @Column(name = "description")
    private String description;
    @Column(name = "creating_date")
    private Date creatingDate;
    @Column(name = "is_available")
    private Boolean isAvailable;
    @Column(name = "image")
    private String image;
    @Column(name = "link_clip")
    private String linkClip;
    @Column(name = "learning_outcome")
    private String learningOutcome;

    public CurriculumResponseDto convertToDto() {
        CurriculumResponseDto curResDto = new CurriculumResponseDto(curriculumId, curriculumCode, curriculumName, image);
        return curResDto;
    }
}
