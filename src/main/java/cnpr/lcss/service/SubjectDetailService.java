package cnpr.lcss.service;

import cnpr.lcss.dao.Subject;
import cnpr.lcss.dao.SubjectDetail;
import cnpr.lcss.model.SubjectDetailDto;
import cnpr.lcss.model.SubjectDetailPagingResponseDto;
import cnpr.lcss.model.SubjectDetailRequestDto;
import cnpr.lcss.model.SubjectDetailUpdateRequestDto;
import cnpr.lcss.repository.SubjectDetailRepository;
import cnpr.lcss.repository.SubjectRepository;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    //<editor-fold desc="Find Subject Detail by Subject Id">
    public SubjectDetailPagingResponseDto findSubjectDetailBySubjectId(int subjectId, boolean isAvailable, int pageNo, int pageSize) {
        // pageNo starts at 0
        // always set first page = 1 ---> pageNo - 1
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<SubjectDetail> page = subjectDetailRepository.findSubjectDetailBySubjectIdAndIsAvailable(subjectId, isAvailable, pageable);
        List<SubjectDetail> subjectDetailList = page.getContent();
        List<SubjectDetailDto> subjectDetailDtoList = subjectDetailList.stream().map(subjectDetail -> subjectDetail.convertToDto()).collect(Collectors.toList());
        int pageTotal = page.getTotalPages();

        SubjectDetailPagingResponseDto subDetailPgResDto = new SubjectDetailPagingResponseDto(pageNo, pageSize, pageTotal, subjectDetailDtoList);

        return subDetailPgResDto;
    }
    //</editor-fold>

    //<editor-fold desc="Create New Subject Detail">
    @Transactional
    public ResponseEntity<?> createNewSubjectDetail(SubjectDetailRequestDto insSubjectDetail) throws Exception {

        try {
            if (subjectRepository.existsById(insSubjectDetail.getSubjectId())) {
                Subject subject = subjectRepository.findBySubjectId(insSubjectDetail.getSubjectId());
                boolean availableSubject = subject.getIsAvailable();
                if (availableSubject) {
                    /**
                     * WeekNum <= Slot/SlotPerWeek
                     */
                    if (insSubjectDetail.getWeekNum() <= (subject.getSlot() / subject.getSlotPerWeek())) {
                        if (insSubjectDetail.getWeekNum() > 0) {
                            SubjectDetail newSubjectDetail = new SubjectDetail();

                            newSubjectDetail.setSubject(subject);
                            newSubjectDetail.setWeekNum(insSubjectDetail.getWeekNum());
                            newSubjectDetail.setIsAvailable(true);
                            newSubjectDetail.setWeekDescription(insSubjectDetail.getWeekDescription().trim());
                            newSubjectDetail.setLearningOutcome(insSubjectDetail.getLearningOutcome().trim());

                            subjectDetailRepository.save(newSubjectDetail);
                        } else {
                            throw new Exception(Constant.INVALID_WEEK_NUM);
                        }
                    } else {
                        throw new Exception(Constant.INVALID_WEEK_NUM_LIMIT);
                    }
                } else {
                    throw new Exception(Constant.INVALID_SUBJECT_AVAILABLE);
                }
            } else {
                throw new IllegalArgumentException(Constant.INVALID_SUBJECT_ID);
            }
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Update Subject Detail by Subject Detail Id">
    public ResponseEntity<?> updateSubjectDetail(int subjectDetailId, SubjectDetailUpdateRequestDto insSubjectDetail) throws Exception {

        try {
            if (subjectDetailRepository.existsById(subjectDetailId)) {
                if (insSubjectDetail.getWeekNum() > 0) {
                    SubjectDetail updateSubjectDetail = subjectDetailRepository.findBySubjectDetailId(subjectDetailId);

                    updateSubjectDetail.setWeekNum(insSubjectDetail.getWeekNum());
                    updateSubjectDetail.setWeekDescription(insSubjectDetail.getWeekDescription().trim());
                    updateSubjectDetail.setIsAvailable(insSubjectDetail.getIsAvailable());
                    updateSubjectDetail.setLearningOutcome(insSubjectDetail.getLearningOutcome().trim());

                    subjectDetailRepository.save(updateSubjectDetail);
                } else {
                    throw new Exception(Constant.INVALID_WEEK_NUM);
                }
            } else {
                throw new IllegalArgumentException(Constant.INVALID_SUBJECT_DETAIL_ID);
            }
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Delete Subject Detail by Subject Detail Id">
    public ResponseEntity<?> deleteSubjectDetailBySubjectDetailId(int subjectDetailId) throws Exception {

        try {
            if (subjectDetailRepository.existsById(subjectDetailId)) {
                // Find Subject Detail by Subject Detail Id
                SubjectDetail subjectDetail = subjectDetailRepository.findBySubjectDetailId(subjectDetailId);
                // Check whether subject detail is available or not
                if (subjectDetail.getIsAvailable() == true) {
                    // If being available, change to unavailable
                    subjectDetail.setIsAvailable(false);
                    subjectDetailRepository.save(subjectDetail);
                } else {
                    throw new Exception(Constant.INVALID_SUBJECT_DETAIL_AVAILABLE);
                }
            } else {
                throw new IllegalArgumentException(Constant.INVALID_SUBJECT_DETAIL_ID);
            }
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }
    //</editor-fold>
}