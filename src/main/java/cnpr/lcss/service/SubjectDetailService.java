package cnpr.lcss.service;

import cnpr.lcss.dao.SubjectDetail;
import cnpr.lcss.model.SubjectDetailDto;
import cnpr.lcss.model.SubjectDetailPagingResponseDto;
import cnpr.lcss.repository.SubjectDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectDetailService {

    @Autowired
    SubjectDetailRepository subjectDetailRepository;

    // Find Subject Detail by Subject Id
    public SubjectDetailPagingResponseDto findSubjectDetailBySubjectId(int subjectId, int pageNo, int pageSize) {
        // pageNo starts at 0
        // always set first page = 1 ---> pageNo - 1
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<SubjectDetail> page = subjectDetailRepository.findSubjectDetailBySubjectId(subjectId, pageable);
        List<SubjectDetail> subjectDetailList = page.getContent();
        List<SubjectDetailDto> subjectDetailDtoList = subjectDetailList.stream().map(subjectDetail -> subjectDetail.convertToDto()).collect(Collectors.toList());
        int pageTotal = page.getTotalPages();

        SubjectDetailPagingResponseDto subDetailPgResDto = new SubjectDetailPagingResponseDto(pageNo, pageSize, pageTotal, subjectDetailDtoList);

        return subDetailPgResDto;
    }
}