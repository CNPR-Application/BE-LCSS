package cnpr.lcss.service;

import cnpr.lcss.dao.Subject;
import cnpr.lcss.dao.SubjectDetail;
import cnpr.lcss.model.SubjectDetailDto;
import cnpr.lcss.model.SubjectDetailPagingResponseDto;
import cnpr.lcss.model.SubjectDetailRequestDto;
import cnpr.lcss.repository.SubjectDetailRepository;
import cnpr.lcss.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectDetailService {

    @Autowired
    SubjectDetailRepository subjectDetailRepository;
    @Autowired
    SubjectRepository subjectRepository;

    private final String WEEK_NO_MUST_BE_POSITIVE = "Number of weeks must be larger than 0!";
    private final String SUBJECT_DOES_NOT_EXIST = "Subject Id does not exist!";
    private final String SUBJECT_UNAVAILABLE = "Subject is being disable!";

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

    // Create New Subject Detail
    @Transactional
    public ResponseEntity<?> createNewSubjectDetail(SubjectDetailRequestDto insSubjectDetail) throws Exception {

        try {
            if (subjectRepository.existsById(insSubjectDetail.getSubjectId())) {
                Subject subject = subjectRepository.findBySubjectId(insSubjectDetail.getSubjectId());
                boolean availableSubject = subject.getIsAvailable();
                if (availableSubject) {
                    if (insSubjectDetail.getWeekNum() > 0) {
                        SubjectDetail newSubjectDetail = new SubjectDetail();

                        newSubjectDetail.setSubject(subject);
                        newSubjectDetail.setWeekNum(insSubjectDetail.getWeekNum());
                        newSubjectDetail.setWeekDescription(insSubjectDetail.getWeekDescription().trim());
                        newSubjectDetail.setLearningOutcome(insSubjectDetail.getLearningOutcome().trim());

                        subjectDetailRepository.save(newSubjectDetail);
                    } else {
                        throw new Exception(WEEK_NO_MUST_BE_POSITIVE);
                    }
                } else {
                    throw new Exception(SUBJECT_UNAVAILABLE);
                }
            } else {
                throw new Exception(SUBJECT_DOES_NOT_EXIST);
            }
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }
}