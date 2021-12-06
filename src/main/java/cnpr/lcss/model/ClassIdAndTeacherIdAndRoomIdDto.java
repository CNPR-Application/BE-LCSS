package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClassIdAndTeacherIdAndRoomIdDto {
    Integer classId;
    Integer teacherId;
    Integer roomId;
}
