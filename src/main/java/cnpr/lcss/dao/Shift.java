package cnpr.lcss.dao;

import cnpr.lcss.model.ShiftDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "shift")
public class Shift implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id")
    private int shiftId;
    @Column(name = "time_start")
    private String timeStart;
    @Column(name = "time_end")
    private String timeEnd;
    @Column(name = "day_of_week")
    private String dayOfWeek;
    @Column(name = "duration")
    private int duration;
    @Column(name = "is_available")
    private boolean isAvailable;

    @OneToMany(mappedBy = "shift")
    private List<Class> classList;

    //<editor-fold desc="Modify isAvailable">
    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    //</editor-fold>

    //<editor-fold desc="Convert to ShiftDto">
    public ShiftDto convertToDto() {
        ShiftDto dto = new ShiftDto();
        dto.setShiftId(shiftId);
        dto.setTimeStart(timeStart);
        dto.setTimeEnd(timeEnd);
        dto.setDayOfWeek(dayOfWeek);
        dto.setDuration(duration);
        dto.setAvailable(isAvailable);
        return dto;
    }
    //</editor-fold>
}
