package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubjectDetailDto {

    private int subjectDetailId;
    private int weekNum;
    private String weekDescription;
    private boolean isAvailable;
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