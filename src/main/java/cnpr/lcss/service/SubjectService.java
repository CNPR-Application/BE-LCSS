package cnpr.lcss.service;

import cnpr.lcss.dao.Subject;
import cnpr.lcss.dao.SubjectDetail;
import cnpr.lcss.model.SubjectCreateRequestDto;
import cnpr.lcss.model.SubjectDto;
import cnpr.lcss.model.SubjectPagingResponseDto;
import cnpr.lcss.model.SubjectUpdateRequestDto;
import cnpr.lcss.repository.CurriculumRepository;
import cnpr.lcss.repository.SubjectDetailRepository;
import cnpr.lcss.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    private final String SUBJECT_ID_DOES_NOT_EXIST = "Subject Id does not exist!";
    private final String CURRICULUM_ID_DOES_NOT_EXIST = "Curriculum Id does not exist!";
    private final String DUPLICATE_CODE = "Duplicate Subject Code!";
    private final String DUPLICATE_NAME = "Duplicate Subject Name!";
    private final String INVALID_PRICE = "Price CAN NOT BE LOWER THAN ZERO!";
    private final String INVALID_SLOT = "SLOT CAN NOT BE EQUAL OR LOWER THAN ZERO!";
    private final String INVALID_SLOT_PER_WEEK = "SLOT PER WEEK CAN NOT BE EQUAL OR LOWER THAN ZERO!";

    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    SubjectDetailRepository subjectDetailRepository;
    @Autowired
    CurriculumRepository curriculumRepository;

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

    public ResponseEntity<?> findSubjectAndCurriculumBySubjectId(int subjectId) throws Exception {

        try {
            if (subjectRepository.existsById(subjectId)) {
                Map<String, Object> mapObj = new LinkedHashMap<>();

                Subject subject = subjectRepository.findBySubjectId(subjectId);

                mapObj.put("subjectId", subject.getSubjectId());
                mapObj.put("subjectCode", subject.getSubjectCode());
                mapObj.put("subjectName", subject.getSubjectName());
                mapObj.put("price", subject.getPrice());
                mapObj.put("creatingDate", subject.getCreatingDate());
                mapObj.put("description", subject.getDescription());
                mapObj.put("isAvailable", subject.getIsAvailable());
                mapObj.put("image", subject.getImage());
                mapObj.put("slot", subject.getSlot());
                mapObj.put("slotPerWeek", subject.getSlotPerWeek());
                mapObj.put("rating", subject.getRating());
                mapObj.put("curriculumId", subject.getCurriculum().getCurriculumId());
                mapObj.put("curriculumCode", subject.getCurriculum().getCurriculumCode());
                mapObj.put("curriculumName", subject.getCurriculum().getCurriculumName());

                return ResponseEntity.ok(mapObj);
            } else {
                throw new IllegalArgumentException(SUBJECT_ID_DOES_NOT_EXIST);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity<?> updateSubject(int subId, SubjectUpdateRequestDto insSub) throws Exception {
        try {
            if (!subjectRepository.existsById(subId)) {
                throw new IllegalArgumentException(SUBJECT_ID_DOES_NOT_EXIST);
            }
            if (subjectRepository.existsSubjectBySubjectNameAndSubjectIdIsNot(insSub.getSubjectName(), subId) == Boolean.TRUE) {
                throw new Exception(DUPLICATE_NAME);
            }
            if (insSub.getPrice() < 0) {
                throw new Exception(INVALID_PRICE);
            }
            if (!curriculumRepository.existsById(insSub.getCurriculumId())) {
                throw new IllegalArgumentException(CURRICULUM_ID_DOES_NOT_EXIST);
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

    public ResponseEntity<?> deleteSubjectBySubjectId(int subjectId) throws Exception {

        try {
            if (!subjectRepository.existsById(subjectId)) {
                throw new IllegalArgumentException(SUBJECT_ID_DOES_NOT_EXIST);
            } else {
                Subject deleteSubject = subjectRepository.findBySubjectId(subjectId);
                if (deleteSubject.getIsAvailable()) {
                    List<SubjectDetail> subjectDetailList = subjectDetailRepository.findSubjectDetailBySubject_SubjectId(subjectId);
                    for (SubjectDetail subjectDetail : subjectDetailList) {
                        subjectDetail.setIsAvailable(false);
                    }
                    deleteSubject.setIsAvailable(Boolean.FALSE);
                    subjectRepository.save(deleteSubject);
                    return ResponseEntity.ok(Boolean.TRUE);
                } else {
                    return ResponseEntity.ok(Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> createNewSubject(SubjectCreateRequestDto newSub) throws Exception {

        Date creatingDate = new Date();

        try {
            if (subjectRepository.existsSubjectBySubjectCode(newSub.getSubjectCode()) == Boolean.TRUE) {
                throw new Exception(DUPLICATE_CODE);
            } else {
                if (subjectRepository.existsSubjectBySubjectName(newSub.getSubjectName()) == Boolean.TRUE) {
                    throw new Exception(DUPLICATE_NAME);
                }
                //NOT EXIST CURRICULUM then throw EXCEPTION
                if (curriculumRepository.existsByCurriculumId(newSub.getCurriculumId()) == Boolean.FALSE) {
                    throw new Exception(CURRICULUM_ID_DOES_NOT_EXIST);
                }
                if (newSub.getPrice() < 0) {
                    throw new Exception(INVALID_PRICE);
                }
                if (newSub.getSlot() <= 0) {
                    throw new Exception(INVALID_SLOT);
                }
                if (newSub.getSlotPerWeek() <= 0) {
                    throw new Exception(INVALID_SLOT_PER_WEEK);
                } else {
                    Subject insSub = new Subject();

                    insSub.setSubjectCode(newSub.getSubjectCode().trim().replaceAll("\\s+", ""));
                    insSub.setSubjectName(newSub.getSubjectName().trim());
                    insSub.setPrice(newSub.getPrice());
                    insSub.setCreatingDate(creatingDate);
                    insSub.setDescription(newSub.getDescription().trim());
                    insSub.setIsAvailable(Boolean.TRUE);
                    insSub.setImage(newSub.getImage().trim());
                    insSub.setCurriculum(curriculumRepository.findOneByCurriculumId(newSub.getCurriculumId()));
                    insSub.setSlot(newSub.getSlot());
                    insSub.setSlotPerWeek(newSub.getSlotPerWeek());
                    insSub.setRating(null);

                    subjectRepository.save(insSub);
                    return ResponseEntity.ok(Boolean.TRUE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }
}