package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentInClassSearchPagingResponseDto {
    private int pageNo;
    private int pageSize;
    private int pageTotal;
    List<StudentInClassSearchResponseDto> studentInClassSearchResponseDtos;
}
