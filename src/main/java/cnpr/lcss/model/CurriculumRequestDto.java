package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CurriculumRequestDto {

    private String curriculumCode;
    private String curriculumName;
    private String description;
    private boolean isAvailable;
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
