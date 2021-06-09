package cnpr.lcss.service;

import cnpr.lcss.dao.Curriculum;
import cnpr.lcss.errorMessageConfig.ErrorMessage;
import cnpr.lcss.model.CurriculumPagingResponseDto;
import cnpr.lcss.repository.CurriculumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurriculumService {

    @Autowired
    CurriculumRepository curriculumRepository;

    ErrorMessage errorMessage;

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
    public Curriculum findOneByCurriculumId(int curriculumId) throws Exception {
        return curriculumRepository.findOneByCurriculumId(curriculumId);
    }
}
