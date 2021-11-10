package cnpr.lcss.dao;

import cnpr.lcss.model.CurriculumDto;
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
    @Column(name = "learning_outcome")
    private String learningOutcome;

    @OneToMany(mappedBy = "curriculum")
    private List<RegisteringGuest> registeringGuestList;

    //<editor-fold desc="Modify isAvailable">
    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    //</editor-fold>

    //<editor-fold desc="Convert to CurriculumDto">
    public CurriculumDto convertToDto() {
        CurriculumDto curriculumDto = new CurriculumDto(curriculumId, curriculumCode, curriculumName, description, creatingDate, isAvailable, learningOutcome);
        return curriculumDto;
    }
    //</editor-fold>
}
