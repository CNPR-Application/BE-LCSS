package cnpr.lcss.service;

import cnpr.lcss.dao.Subject;
import cnpr.lcss.model.SubjectDto;
import cnpr.lcss.model.SubjectPagingResponseDto;
import cnpr.lcss.model.SubjectUpdateRequestDto;
import cnpr.lcss.repository.CurriculumRepository;
import cnpr.lcss.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    CurriculumRepository curriculumRepository;

    private final String SUBJECT_ID_DOES_NOT_EXIST = "Subject Id does not exist!";
    private final String DUPLICATE_NAME = "Duplicate Subject Name!";
    private final String INVALID_PRICE = "Price CAN NOT BE EQUAL OR LOWER THAN ZERO!";
    private final String CURRICULUM_ID_DOES_NOT_EXIST = "Curriculum Id does not exist!";

    public SubjectPagingResponseDto findBySubjectNameContainsAndIsAvailable(String keyword, boolean isAvailable, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Subject> page = subjectRepository.findBySubjectNameContainingIgnoreCaseAndIsAvailable(keyword, isAvailable, pageable);
        List<Subject> subjectList = page.getContent();
        List<SubjectDto> subjectDtoList = subjectList.stream().map(subject -> subject.convertToDto()).collect(Collectors.toList());

        int pageTotal = page.getTotalPages();

        SubjectPagingResponseDto subPgResDtos = new SubjectPagingResponseDto(pageNo, pageSize, pageTotal, subjectDtoList);

        return subPgResDtos;
    }

    public SubjectPagingResponseDto findSubjectByCurriculumIdAndAndIsAvailable(int keyword, boolean isAvailable, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Subject> page = subjectRepository.findByCurriculum_CurriculumIdAndIsAvailable(keyword, isAvailable, pageable);
        List<Subject> subjectList = page.getContent();
        List<SubjectDto> subjectDtoList = subjectList.stream().map(subject -> subject.convertToDto()).collect(Collectors.toList());
        int pageTotal = page.getTotalPages();


        SubjectPagingResponseDto subjectPagingResponseDto = new SubjectPagingResponseDto(pageNo, pageSize, pageTotal, subjectDtoList);

        return subjectPagingResponseDto;
    }

    public SubjectPagingResponseDto findBySubjectCodeAndIsAvailable(String code, boolean isAvailable, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Subject> page = subjectRepository.findBySubjectCodeContainingIgnoreCaseAndIsAvailable(code, isAvailable, pageable);
        List<Subject> subjectList = page.getContent();
        List<SubjectDto> subjectDtoList = subjectList.stream().map(subject -> subject.convertToDto()).collect(Collectors.toList());

        int pageTotal = page.getTotalPages();

        SubjectPagingResponseDto subPgResDtos = new SubjectPagingResponseDto(pageNo, pageSize, pageTotal, subjectDtoList);

        return subPgResDtos;
    }

    public ResponseEntity<?> updateSubject(int subId, SubjectUpdateRequestDto insSub) throws Exception {
        try {
            if (!subjectRepository.existsById(subId)) {
                throw new IllegalArgumentException(SUBJECT_ID_DOES_NOT_EXIST);
            }
            if (subjectRepository.existsSubjectBySubjectNameAndSubjectIdIsNot(insSub.getSubjectName(), subId) == Boolean.TRUE) {
                throw new Exception(DUPLICATE_NAME);
            }
            if (insSub.getPrice() <= 0) {
                throw new Exception(INVALID_PRICE);
            }
            if (curriculumRepository.existsCurriculumByCurriculumId(insSub.getCurriculumId()) == Boolean.FALSE) {
                throw new Exception(CURRICULUM_ID_DOES_NOT_EXIST);
            } else {
                Subject updateSubject = subjectRepository.findBySubjectId(subId);

                updateSubject.setSubjectName(insSub.getSubjectName().trim());
                updateSubject.setPrice(insSub.getPrice());
                updateSubject.setDescription(insSub.getDescription().trim());
                // KEEP THE CREATING DATE
                updateSubject.setCreatingDate(updateSubject.getCreatingDate());
                updateSubject.setIsAvailable(insSub.getIsAvailable());
                updateSubject.setImage(insSub.getImage().trim());
                updateSubject.setSlot(insSub.getSlot());
                updateSubject.setSlotPerWeek(insSub.getSlotPerWeek());
                updateSubject.setRating(insSub.getRating());
                updateSubject.setCurriculum(curriculumRepository.findOneByCurriculumId(insSub.getCurriculumId()));
                
                subjectRepository.save(updateSubject);

                return ResponseEntity.ok(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }
}