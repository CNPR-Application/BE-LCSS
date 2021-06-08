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
public class CurriculumPagingResponseDto {

    private int pageNo;
    private int pageSize;
    private List<CurriculumResponseDto> curriculumResponseDtos;
}
