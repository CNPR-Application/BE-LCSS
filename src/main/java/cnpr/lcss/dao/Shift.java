package cnpr.lcss.dao;

import cnpr.lcss.model.ShiftDto;
import cnpr.lcss.model.SubjectDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "shift")
public class Shift implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shiftId;
    @Column(name = "description")
    private String description;

    public ShiftDto convertToDto() {
        String[] strings=description.split(",");
        ShiftDto shiftDto = new ShiftDto(shiftId, strings[0],strings[1],strings[2]);
        return shiftDto;
    }
}
