package cnpr.lcss.service;


import cnpr.lcss.dao.Subject;
import cnpr.lcss.model.SubjectPagingResponseDto;
import cnpr.lcss.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;

    public SubjectPagingResponseDto findSubjectByCurriculum_CurriculumIdAndAndIsAvailableIsTrue(int keyword, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Subject> page = subjectRepository.findSubjectByCurriculumIddAndAndIsAvailableIsTrue(keyword, pageable);
        List<Subject> subjectList = page.getContent();
        int pageTotal = page.getTotalPages();

        SubjectPagingResponseDto subjectPagingResponseDto = new SubjectPagingResponseDto(pageNo, pageSize, pageTotal, subjectList);

        return subjectPagingResponseDto;
    }

}
