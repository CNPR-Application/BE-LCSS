package cnpr.lcss.service;

import cnpr.lcss.dao.Curriculum;
import cnpr.lcss.dao.Subject;
import cnpr.lcss.model.CurriculumPagingResponseDto;
import cnpr.lcss.model.SubjectDetailDto;
import cnpr.lcss.model.SubjectDto;
import cnpr.lcss.model.SubjectPagingResponseDto;
import cnpr.lcss.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;

    public SubjectPagingResponseDto findBySubjectNameContainsAndIsAvailable(String keyword,boolean isAvailable, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Subject> page = subjectRepository.findBySubjectNameContainingIgnoreCaseAndIsAvailable(keyword,isAvailable, pageable);
        List<Subject> subjectList = page.getContent();
        List<SubjectDto> subjectDtoList = subjectList.stream().map(subject -> subject.convertToDto()).collect(Collectors.toList());

        int pageTotal = page.getTotalPages();

        SubjectPagingResponseDto subPgResDtos = new SubjectPagingResponseDto(pageNo, pageSize, pageTotal, subjectDtoList);

        return subPgResDtos;
    }
    public SubjectPagingResponseDto findBySubjectCodeAndIsAvailable(String code,boolean isAvailable, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Subject> page = subjectRepository.findBySubjectCodeAndIsAvailable(code,isAvailable, pageable);
        List<Subject> subjectList = page.getContent();
        List<SubjectDto> subjectDtoList = subjectList.stream().map(subject -> subject.convertToDto()).collect(Collectors.toList());

        int pageTotal = page.getTotalPages();

        SubjectPagingResponseDto subPgResDtos = new SubjectPagingResponseDto(pageNo, pageSize, pageTotal, subjectDtoList);

        return subPgResDtos;
    }
}
