package cnpr.lcss.service;

import cnpr.lcss.dao.Subject;
import cnpr.lcss.dao.SubjectDetail;
import cnpr.lcss.dao.TeachingSubject;
import cnpr.lcss.model.*;
import cnpr.lcss.repository.CurriculumRepository;
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
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    SubjectDetailRepository subjectDetailRepository;
    @Autowired
    CurriculumRepository curriculumRepository;

    //<editor-fold desc="Calculate Rating">
    private String calculateRating(String rating) {
        DecimalFormat df = new DecimalFormat(Constant.RATING_PATTERN);
        String[] arrOfInpStr = rating.split("/");
        double result = Double.parseDouble(arrOfInpStr[0]) / Double.parseDouble(arrOfInpStr[1]);
        String finalResult = df.format(result);
        return finalResult;
    }
    //</editor-fold>

    //<editor-fold desc="4.01-search-subject-by-subject-name">
    public ResponseEntity<?> findBySubjectNameContainsAndIsAvailable(String keyword, boolean isAvailable, int pageNo, int pageSize) {

        try {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Map<String, Object> mapObj = new LinkedHashMap<>();

            Page<Subject> subjectList = subjectRepository.findBySubjectNameContainingIgnoreCaseAndIsAvailable(keyword, isAvailable, pageable);
            List<SubjectSearchDto> subjectDtoList = subjectList.getContent().stream().map(subject -> subject.convertToSearchDto()).collect(Collectors.toList());
            int pageTotal = subjectList.getTotalPages();

            for (SubjectSearchDto subject : subjectDtoList) {
                subject.setRating(calculateRating(subject.getRating()));
            }

            mapObj.put("pageNo", pageNo);
            mapObj.put("pageSize", pageSize);
            mapObj.put("pageTotal", pageTotal);
            mapObj.put("subjectsResponseDtos", subjectDtoList);

            return ResponseEntity.ok(mapObj);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="4.03-search-subject-by-curriculum-id">
    public SubjectPagingResponseDto findSubjectByCurriculumIdAndAndIsAvailable(int keyword, boolean isAvailable, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Subject> page = subjectRepository.findByCurriculum_CurriculumIdAndIsAvailable(keyword, isAvailable, pageable);
        List<Subject> subjectList = page.getContent();
        List<SubjectDto> subjectDtoList = subjectList.stream().map(subject -> subject.convertToDto()).collect(Collectors.toList());
        int pageTotal = page.getTotalPages();

        for (SubjectDto subject : subjectDtoList) {
            subject.setRating(calculateRating(subject.getRating()));
        }

        SubjectPagingResponseDto subjectPagingResponseDto = new SubjectPagingResponseDto(pageNo, pageSize, pageTotal, subjectDtoList);

        return subjectPagingResponseDto;
    }
    //</editor-fold>

    //<editor-fold desc="4.02-search-subject-by-subject-code">
    public SubjectPagingResponseDto findBySubjectCodeAndIsAvailable(String code, boolean isAvailable, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Subject> page = subjectRepository.findBySubjectCodeContainingIgnoreCaseAndIsAvailable(code, isAvailable, pageable);
        List<Subject> subjectList = page.getContent();
        List<SubjectDto> subjectDtoList = subjectList.stream().map(subject -> subject.convertToDto()).collect(Collectors.toList());
        int pageTotal = page.getTotalPages();

        for (SubjectDto subject : subjectDtoList) {
            subject.setRating(calculateRating(subject.getRating()));
        }

        SubjectPagingResponseDto subPgResDtos = new SubjectPagingResponseDto(pageNo, pageSize, pageTotal, subjectDtoList);

        return subPgResDtos;
    }
    //</editor-fold>

    //<editor-fold desc="4.04-find-subject-and-curriculum-by-subject-id">
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
                mapObj.put("rating", calculateRating(subject.getRating()));
                mapObj.put("curriculumId", subject.getCurriculum().getCurriculumId());
                mapObj.put("curriculumCode", subject.getCurriculum().getCurriculumCode());
                mapObj.put("curriculumName", subject.getCurriculum().getCurriculumName());

                return ResponseEntity.ok(mapObj);
            } else {
                throw new IllegalArgumentException(Constant.INVALID_SUBJECT_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="4.05-delete-subject">
    public ResponseEntity<?> deleteSubjectBySubjectId(int subjectId) throws Exception {

        try {
            if (!subjectRepository.existsById(subjectId)) {
                throw new IllegalArgumentException(Constant.INVALID_SUBJECT_ID);
            } else {
                Subject deleteSubject = subjectRepository.findBySubjectId(subjectId);
                if (deleteSubject.getIsAvailable()&&deleteSubject.getClassList().isEmpty()) {
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
    //</editor-fold>

    //<editor-fold desc="4.06-create-subject">
    @Transactional
    public ResponseEntity<?> createNewSubject(SubjectCreateRequestDto newSub) throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(Constant.TIMEZONE));
        Date creatingDate = calendar.getTime();

        try {
            if (subjectRepository.existsSubjectBySubjectCode(newSub.getSubjectCode()) == Boolean.TRUE) {
                throw new Exception(Constant.DUPLICATE_SUBJECT_CODE);
            } else {
                if (subjectRepository.existsSubjectBySubjectName(newSub.getSubjectName()) == Boolean.TRUE) {
                    throw new Exception(Constant.DUPLICATE_SUBJECT_NAME);
                }
                //NOT EXIST CURRICULUM then throw EXCEPTION
                if (curriculumRepository.existsByCurriculumId(newSub.getCurriculumId()) == Boolean.FALSE) {
                    throw new Exception(Constant.INVALID_CURRICULUM_ID);
                }
                if (newSub.getPrice() < 0) {
                    throw new Exception(Constant.INVALID_SUBJECT_PRICE);
                }
                if (newSub.getSlot() <= 0) {
                    throw new Exception(Constant.INVALID_SUBJECT_SLOT);
                }
                if (newSub.getSlotPerWeek() <= 0) {
                    throw new Exception(Constant.INVALID_SUBJECT_SLOT_PER_WEEK);
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
                    insSub.setRating(Constant.DEFAULT_SUBJECT_RATING);

                    subjectRepository.save(insSub);
                    return ResponseEntity.ok(Boolean.TRUE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }
    //</editor-fold>

    //<editor-fold desc="4.07-update-subject-by-subject-id">
    public ResponseEntity<?> updateSubject(int subId, SubjectUpdateRequestDto insSub) throws Exception {
        try {
            if (!subjectRepository.existsById(subId)) {
                throw new IllegalArgumentException(Constant.INVALID_SUBJECT_ID);
            }
            if (subjectRepository.existsSubjectBySubjectNameAndSubjectIdIsNot(insSub.getSubjectName(), subId) == Boolean.TRUE) {
                throw new Exception(Constant.DUPLICATE_SUBJECT_NAME);
            }
            if (insSub.getPrice() < 0) {
                throw new Exception(Constant.INVALID_SUBJECT_PRICE);
            }
            if (!curriculumRepository.existsById(insSub.getCurriculumId())) {
                throw new IllegalArgumentException(Constant.INVALID_CURRICULUM_ID);
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
    //</editor-fold>
}