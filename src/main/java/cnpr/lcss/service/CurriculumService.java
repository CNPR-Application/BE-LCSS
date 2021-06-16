package cnpr.lcss.service;

import cnpr.lcss.dao.Curriculum;
import cnpr.lcss.model.CurriculumPagingResponseDto;
import cnpr.lcss.model.CurriculumRequestDto;
import cnpr.lcss.repository.CurriculumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class CurriculumService {

    @Autowired
    CurriculumRepository curriculumRepository;

    private final String CURRICULUM_ID_DOES_NOT_EXIST = "Curriculum Id does not exist!";
    private final String DUPLICATE_CODE = "Duplicate Curriculum Code!";
    private final String DUPLICATE_NAME = "Duplicate Curriculum Name!";

    // Find Curriculums by Curriculum Name LIKE keyword
    public CurriculumPagingResponseDto findByCurriculumNameContainsAndIsAvailableIsTrue(String keyword, int pageNo, int pageSize) {
        // pageNo starts at 0
        // always set first page = 1 ---> pageNo - 1
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Curriculum> page = curriculumRepository.findByCurriculumNameContainingIgnoreCaseAndIsAvailableIsTrue(keyword, pageable);
        List<Curriculum> curriculumList = page.getContent();
        int pageTotal = page.getTotalPages();

        CurriculumPagingResponseDto curPgResDtos = new CurriculumPagingResponseDto(pageNo, pageSize, pageTotal, curriculumList);

        return curPgResDtos;
    }

    // Find Curriculums by Curriculum Code LIKE keyword
    public CurriculumPagingResponseDto findByCurriculumCodeContainsAndIsAvailableIsTrue(String keyword, int pageNo, int pageSize) {
        // pageNo starts at 0
        // always set first page = 1 ---> pageNo - 1
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Curriculum> page = curriculumRepository.findByCurriculumCodeContainingIgnoreCaseAndIsAvailableIsTrue(keyword, pageable);
        List<Curriculum> curriculumList = page.getContent();
        int pageTotal = page.getTotalPages();

        CurriculumPagingResponseDto curPgResDtos = new CurriculumPagingResponseDto(pageNo, pageSize, pageTotal, curriculumList);

        return curPgResDtos;
    }

    // Get Curriculum Details by Curriculum Id
    public ResponseEntity<?> findOneByCurriculumId(int curriculumId) throws Exception {
        try {
            return ResponseEntity.ok(curriculumRepository.findOneByCurriculumId(curriculumId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Delete Curriculum by Curriculum Id
    // Change isAvailable from True to False
    public ResponseEntity<?> deleteByCurriculumId(int curriculumId) throws Exception {
        try {
            if (!curriculumRepository.existsById(curriculumId)) {
                throw new IllegalArgumentException(CURRICULUM_ID_DOES_NOT_EXIST);
            } else {
                Curriculum delCur = curriculumRepository.findOneByCurriculumId(curriculumId);
                if (delCur.getIsAvailable().equals(Boolean.TRUE)) {
                    delCur.setIsAvailable(Boolean.FALSE);
                    curriculumRepository.save(delCur);
                    return ResponseEntity.ok(Boolean.TRUE);
                } else {
                    return ResponseEntity.ok(Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Create new Curriculum
    @Transactional
    public ResponseEntity<?> createNewCurriculum(CurriculumRequestDto newCur) throws Exception {

        Date creatingDate = new Date();

        try {
            if (curriculumRepository.existsCurriculumByCurriculumCode(newCur.getCurriculumCode()) == Boolean.TRUE) {
                throw new Exception(DUPLICATE_CODE);
            } else {
                if (curriculumRepository.existsCurriculumByCurriculumName(newCur.getCurriculumName()) == Boolean.TRUE) {
                    throw new Exception(DUPLICATE_NAME);
                } else {
                    Curriculum insCur = new Curriculum();

                    insCur.setCurriculumCode(newCur.getCurriculumCode().trim().replaceAll("\\s+", ""));
                    insCur.setCurriculumName(newCur.getCurriculumName().trim());
                    insCur.setDescription(newCur.getDescription().trim());
                    insCur.setCreatingDate(creatingDate);
                    insCur.setIsAvailable(Boolean.TRUE);
                    insCur.setImage(newCur.getImage().trim());
                    insCur.setLinkClip(newCur.getLinkClip().trim());
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

    // Update Curriculum by Curriculum id
    public ResponseEntity<?> updateCurriculum(int curId, CurriculumRequestDto insCur) throws Exception {

        try {
            if (!curriculumRepository.existsById(curId)) {
                throw new IllegalArgumentException(CURRICULUM_ID_DOES_NOT_EXIST);
            } else {
                Curriculum updateCur = curriculumRepository.findOneByCurriculumId(curId);

                updateCur.setCurriculumCode(insCur.getCurriculumCode().trim().replaceAll("\\s+", ""));
                updateCur.setCurriculumName(insCur.getCurriculumName().trim());
                updateCur.setDescription(insCur.getDescription().trim());
                // KEEP THE CREATING DATE
                updateCur.setCreatingDate(updateCur.getCreatingDate());
                updateCur.setImage(insCur.getImage().trim());
                updateCur.setLinkClip(insCur.getLinkClip().trim());
                updateCur.setLearningOutcome(insCur.getLearningOutcome().trim());

                curriculumRepository.save(updateCur);
                return ResponseEntity.ok(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }
}
