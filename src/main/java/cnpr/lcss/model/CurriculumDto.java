package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CurriculumDto {

    private int curriculumId;
    private String curriculumCode;
    private String curriculumName;
    private String description;
    private Date creatingDate;
    private Boolean isAvailable;
    private String image;
    private String linkClip;
    private String learningOutcome;

    /**
     * --- modify getter & setter ---
     */
    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
