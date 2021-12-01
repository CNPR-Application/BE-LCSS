package cnpr.lcss.dao;

import cnpr.lcss.model.SubjectBasicInfoDto;
import cnpr.lcss.model.SubjectDto;
import cnpr.lcss.model.SubjectSearchDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "subject")
public class Subject implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private int subjectId;
    @Column(name = "subject_code")
    private String subjectCode;
    @Column(name = "subject_name")
    private String subjectName;
    @Column(name = "price")
    private float price;
    @Column(name = "creating_date")
    private Date creatingDate;
    @Column(name = "description")
    private String description;
    @Column(name = "is_available")
    private boolean isAvailable;
    @Column(name = "slot")
    private int slot;
    @Column(name = "slot_per_week")
    private int slotPerWeek;
    @Column(name = "rating")
    private String rating;

    @OneToMany(mappedBy = "subject")
    private List<SubjectDetail> subjectDetailList;
    @OneToMany(mappedBy = "subject")
    private List<Class> classList;
    @OneToMany(mappedBy = "subject")
    private List<TeachingSubject> teachingSubjectList;

    @ManyToOne
    @JoinColumn(name = "curriculum_id")
    private Curriculum curriculum;

    //<editor-fold desc="Modify Constructor">
    public Subject(String subjectCode, String subjectName, float price, Date creatingDate, String description, boolean isAvailable, String image, int slot, int slotPerWeek, String rating) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.price = price;
        this.creatingDate = creatingDate;
        this.description = description;
        this.isAvailable = isAvailable;
        this.slot = slot;
        this.slotPerWeek = slotPerWeek;
        this.rating = rating;
    }
    //</editor-fold>

    //<editor-fold desc="Modify isAvailable">
    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    //</editor-fold>

    //<editor-fold desc="Modify toString()">
    @Override
    public String toString() {
        return "Subject{" +
                "subjectId=" + subjectId +
                ", subjectCode='" + subjectCode + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", price=" + price +
                ", creatingDate=" + creatingDate +
                ", description='" + description + '\'' +
                ", isAvailable=" + isAvailable +
                ", slot=" + slot +
                ", slotPerWeek=" + slotPerWeek +
                ", rating='" + rating + '\'' +
                '}';
    }
    //</editor-fold>

    //<editor-fold desc="Convert to SubjectDto">
    public SubjectDto convertToDto() {
        SubjectDto subjectDto = new SubjectDto(subjectId, subjectCode, subjectName, price, creatingDate, description, isAvailable, slot, slotPerWeek, rating, curriculum.getCurriculumId(), curriculum.getCurriculumCode(), curriculum.getCurriculumName());
        return subjectDto;
    }
    //</editor-fold>

    //<editor-fold desc="Convert to SearchDto">
    public SubjectSearchDto convertToSearchDto() {
        SubjectSearchDto subjectSearchDto = new SubjectSearchDto(subjectId, subjectCode, subjectName, price, creatingDate, description, isAvailable, slot, slotPerWeek, rating, curriculum.getCurriculumId(), curriculum.getCurriculumCode(), curriculum.getCurriculumName());
        return subjectSearchDto;
    }
    //</editor-fold>

    //<editor-fold desc="Convert to SubjectBasicInfoDto">
    public SubjectBasicInfoDto convertToSubjectBasicInfoDto() {
        SubjectBasicInfoDto dto = new SubjectBasicInfoDto();
        dto.setSubjectId(subjectId);
        dto.setSubjectName(subjectName);
        return dto;
    }
    //</editor-fold>
}
