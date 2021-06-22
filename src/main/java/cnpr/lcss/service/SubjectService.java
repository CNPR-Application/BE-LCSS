package cnpr.lcss.service;

import cnpr.lcss.dao.Curriculum;
import cnpr.lcss.dao.Subject;
import cnpr.lcss.model.CurriculumRequestDto;
import cnpr.lcss.model.SubjectDto;
import cnpr.lcss.model.SubjectPagingResponseDto;
import cnpr.lcss.repository.CurriculumRepository;
import cnpr.lcss.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    CurriculumRepository curriculumRepository;
    private final String CURRICULUM_ID_DOES_NOT_EXIST = "Curriculum Id does not exist!";
    private final String DUPLICATE_CODE = "Duplicate Subject Code!";
    private final String DUPLICATE_NAME = "Duplicate Subject Name!";

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


    @Transactional
    public ResponseEntity<?> createNewSubject(SubjectDto newSub) throws Exception {

        Date creatingDate = new Date();

        try {
            if (subjectRepository.existsSubjectBySubjectCode(newSub.getSubjectCode()) == Boolean.TRUE) {
                throw new Exception(DUPLICATE_CODE);
            } else {
                if (subjectRepository.existsSubjectBySubjectName(newSub.getSubjectName()) == Boolean.TRUE) {
                    throw new Exception(DUPLICATE_NAME);
                }
                //NOT EXIST CURRICULUM then throw EXCEPTION
                if(curriculumRepository.existsByCurriculumId(newSub.getCurriculumId()) == Boolean.FALSE) {
                    throw new Exception(CURRICULUM_ID_DOES_NOT_EXIST);
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
                    insSub.setRating(newSub.getRating());

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