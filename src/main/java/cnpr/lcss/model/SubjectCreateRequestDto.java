package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubjectCreateRequestDto {

    private String subjectCode;
    private String subjectName;
    private float price;
    private Date creatingDate;
    private String description;
    private String image;
    private int slot;
    private int slotPerWeek;
    private String rating;
    private int curriculumId;
}
