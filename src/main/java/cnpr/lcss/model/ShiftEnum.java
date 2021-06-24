package cnpr.lcss.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
public enum ShiftEnum {

    /**
     * --- DEFINE SHIFTS ---
     */
    MON_WED_FRI_1ST_SHIFT(1, "2-4-6", "18:00", "19:30"),
    MON_WED_FRI_2ND_SHIFT(2, "2-4-6", "19:30", "21:00"),
    TUE_THU_SAT_1ST_SHIFT(3, "3-5-7", "18:00", "19:30"),
    TUE_THU_SAT_2ND_SHIFT(4, "3-5-7", "19:30", "21:00");

    private int id;
    private String dayOfWeek;
    private String timeStart;
    private String timeEnd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
