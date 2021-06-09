package cnpr.lcss.service;

import cnpr.lcss.dao.Curriculum;
import cnpr.lcss.model.CurriculumPagingResponseDto;
import cnpr.lcss.repository.CurriculumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;

@Service
public class CurriculumService {

    @Autowired
    CurriculumRepository curriculumRepository;

    private static final Logger logger = LoggerFactory.getLogger(CurriculumService.class);

    // Find Curriculums by Curriculum Name LIKE keyword
    public CurriculumPagingResponseDto findByCurriculumNameContains(String keyword, int pageNo, int pageSize) {
        // pageNo starts at 0
        // always set first page = 1 ---> pageNo - 1
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Curriculum> curriculumList = curriculumRepository.findByCurriculumNameContainingIgnoreCase(keyword, pageable);

        CurriculumPagingResponseDto curPgResDtos = new CurriculumPagingResponseDto(pageNo, pageSize, curriculumList);

        return curPgResDtos;
    }

    // Find Curriculums by Curriculum Code LIKE keyword
    public CurriculumPagingResponseDto findByCurriculumCodeContains(String keyword, int pageNo, int pageSize) {
        // pageNo starts at 0
        // always set first page = 1 ---> pageNo - 1
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Curriculum> curriculumList = curriculumRepository.findByCurriculumCodeContainingIgnoreCase(keyword, pageable);

        CurriculumPagingResponseDto curPgResDtos = new CurriculumPagingResponseDto(pageNo, pageSize, curriculumList);

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
}
