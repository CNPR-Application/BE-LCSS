package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountResponsePagingDto {
    private int pageNo;
    private int pageSize;
    private int totalPage;
    private List<AccountResponseDto> accountResponseDtoList;
}
