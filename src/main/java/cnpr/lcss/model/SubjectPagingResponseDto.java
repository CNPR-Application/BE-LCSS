package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubjectPagingResponseDto {

    private int pageNo;
    private int pageSize;
    private int pageTotal;
    private List<SubjectDto> subjectsResponseDtos;
}