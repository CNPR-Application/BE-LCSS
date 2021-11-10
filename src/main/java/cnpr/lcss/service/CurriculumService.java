package cnpr.lcss.service;

import cnpr.lcss.dao.Curriculum;
import cnpr.lcss.dao.Subject;
import cnpr.lcss.model.CurriculumDto;
import cnpr.lcss.model.CurriculumRequestDto;
import cnpr.lcss.repository.CurriculumRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CurriculumService {
    @Autowired
    CurriculumRepository curriculumRepository;
    @Autowired
    SubjectRepository subjectRepository;

    //<editor-fold desc="3.01-search-curriculum-by-curriculum-name">
    public ResponseEntity<?> findByCurriculumNameContainingIgnoreCaseAndIsAvailable(String keyword, boolean isAvailable, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Curriculum> page = curriculumRepository.findByCurriculumNameContainingIgnoreCaseAndIsAvailable(keyword, isAvailable, pageable);
        List<CurriculumDto> curriculumDtoList = page.getContent().stream()
                .map(curriculum -> curriculum.convertToDto()).collect(Collectors.toList());
        HashMap<String, Object> mapObj = new LinkedHashMap<>();
        mapObj.put("pageNo", pageNo);
        mapObj.put("pageSize", pageSize);
        mapObj.put("totalPage", page.getTotalPages());
        mapObj.put("curriculumResponseDtos", curriculumDtoList);
        return ResponseEntity.ok(mapObj);
    }
    //</editor-fold>

    //<editor-fold desc="3.02-search-curriculum-by-curriculum-code">
    public ResponseEntity<?> findByCurriculumCodeContainingIgnoreCaseAndIsAvailable(String keyword, boolean isAvailable, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Curriculum> page = curriculumRepository.findByCurriculumCodeContainingIgnoreCaseAndIsAvailable(keyword, isAvailable, pageable);
        List<CurriculumDto> curriculumDtoList = page.getContent().stream()
                .map(curriculum -> curriculum.convertToDto()).collect(Collectors.toList());
        HashMap<String, Object> mapObj = new LinkedHashMap<>();
        mapObj.put("pageNo", pageNo);
        mapObj.put("pageSize", pageSize);
        mapObj.put("totalPage", page.getTotalPages());
        mapObj.put("curriculumResponseDtos", curriculumDtoList);
        return ResponseEntity.ok(mapObj);
    }
    //</editor-fold>

    //<editor-fold desc="3.03-get-curriculum-details-by-curriculum-id">
    public ResponseEntity<?> findOneByCurriculumId(int curriculumId) throws Exception {
        try {
            Curriculum curriculum = curriculumRepository.findOneByCurriculumId(curriculumId);
            CurriculumDto curriculumDto = curriculum.convertToDto();
            return ResponseEntity.ok(curriculumDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="3.04-delete-curriculum-by-curriculum-id">

    /**
     * Delete Curriculum by Curriculum Id.
     * <p>
     * Check whether all Curriculum's Subjects are available or not.
     * <p>
     * If there is one is available -> MUST NOT delete Curriculum.
     * Else, able to delete.
     * <p>
     * DELETE = Change isAvailable from True to False.
     */
    public ResponseEntity<?> deleteByCurriculumId(int curriculumId) throws Exception {

        Boolean curriculumAbleToDelete = true;

        try {
            if (!curriculumRepository.existsById(curriculumId)) {
                throw new IllegalArgumentException(Constant.INVALID_CURRICULUM_ID);
            } else {
                Curriculum delCur = curriculumRepository.findOneByCurriculumId(curriculumId);
                if (delCur.getIsAvailable()) {
                    // Check whether this Curriculum has Subject(s)
                    int noOfSubjects = subjectRepository.countByCurriculum_CurriculumId(curriculumId);

                    List<Subject> subjectList = subjectRepository.findAllByCurriculum_CurriculumId(curriculumId);
                    for (Subject subject : subjectList) {
                        if (subject.getIsAvailable()) {
                            curriculumAbleToDelete = false;
                        }
                    }

                    if (noOfSubjects == 0
                            || (noOfSubjects > 0 && curriculumAbleToDelete == true)) {
                        delCur.setIsAvailable(Boolean.FALSE);
                        curriculumRepository.save(delCur);
                    } else if (noOfSubjects > 0 && curriculumAbleToDelete == false) {
                        throw new Exception(Constant.UNABLE_TO_DELETE_CURRICULUM_);
                    } else {
                        throw new Exception();
                    }

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

    //<editor-fold desc="3.05-create-curriculum">
    @Transactional
    public ResponseEntity<?> createNewCurriculum(CurriculumRequestDto newCur) throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(Constant.TIMEZONE));
        Date creatingDate = calendar.getTime();

        try {
            if (curriculumRepository.existsCurriculumByCurriculumCode(newCur.getCurriculumCode()) == Boolean.TRUE) {
                throw new Exception(Constant.DUPLICATE_CURRICULUM_CODE);
            } else {
                if (curriculumRepository.existsCurriculumByCurriculumName(newCur.getCurriculumName()) == Boolean.TRUE) {
                    throw new Exception(Constant.DUPLICATE_CURRICULUM_NAME);
                } else {
                    Curriculum insCur = new Curriculum();

                    insCur.setCurriculumCode(newCur.getCurriculumCode().trim().replaceAll("\\s+", ""));
                    insCur.setCurriculumName(newCur.getCurriculumName().trim());
                    insCur.setDescription(newCur.getDescription().trim());
                    insCur.setCreatingDate(creatingDate);
                    insCur.setIsAvailable(Boolean.TRUE);
                    insCur.setLearningOutcome(newCur.getLearningOutcome().trim());

                    curriculumRepository.save(insCur);
                    return ResponseEntity.ok(Boolean.TRUE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }
    //</editor-fold>

    //<editor-fold desc="3.06-edit-curriculum-by-curriculum-id">
    public ResponseEntity<?> updateCurriculum(int curId, CurriculumRequestDto insCur) throws Exception {
        try {
            if (!curriculumRepository.existsById(curId)) {
                throw new IllegalArgumentException(Constant.INVALID_CURRICULUM_ID);
            } else {
                Curriculum updateCur = curriculumRepository.findOneByCurriculumId(curId);

                updateCur.setCurriculumCode(insCur.getCurriculumCode().trim().replaceAll("\\s+", ""));
                updateCur.setCurriculumName(insCur.getCurriculumName().trim());
                updateCur.setDescription(insCur.getDescription().trim());
                // KEEP THE CREATING DATE
                updateCur.setCreatingDate(updateCur.getCreatingDate());
                updateCur.setIsAvailable(insCur.getIsAvailable());
                updateCur.setLearningOutcome(insCur.getLearningOutcome().trim());

                curriculumRepository.save(updateCur);
                return ResponseEntity.ok(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }
    //</editor-fold>
}
