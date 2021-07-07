package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClassSearchPagingResponseDto {

        List<ClassSearchByIdDto> classSearchByIdDtos;
        private int roomNo;
        private int pageNo;
        private int totalPage;
}
