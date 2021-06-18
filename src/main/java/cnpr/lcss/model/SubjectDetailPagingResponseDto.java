package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubjectDetailPagingResponseDto {

    private int pageNo;
    private int pageSize;
    private int pageTotal;
    List<SubjectDetailDto> subjectDetailDtoList;
}