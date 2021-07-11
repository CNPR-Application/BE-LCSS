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
    @OneToMany(mappedBy = "shift")
    private List<Booking> bookingList;

    public ShiftDto convertToDto() {
        ShiftDto shiftDto = new ShiftDto(shiftId, timeStart, timeEnd, dayOfWeek, duration, isAvailable);
        return shiftDto;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

}
