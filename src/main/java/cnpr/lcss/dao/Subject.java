package cnpr.lcss.dao;

import cnpr.lcss.model.SubjectDetailDto;
import cnpr.lcss.model.SubjectDto;
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
    @Column(name = "image")
    private String image;
    @Column(name = "slot")
    private int slot;
    @Column(name = "slot_per_week")
    private int slotPerWeek;
    @Column(name = "rating")
    private String rating;

    @OneToMany(mappedBy = "subject")
    private List<SubjectDetail> subjectDetailList;

    @ManyToOne
    @JoinColumn(name = "curriculum_id")
    private Curriculum curriculum;

    public Subject(String subjectCode, String subjectName, float price, Date creatingDate, String description, boolean isAvailable, String image, int slot, int slotPerWeek, String rating) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.price = price;
        this.creatingDate = creatingDate;
        this.description = description;
        this.isAvailable = isAvailable;
        this.image = image;
        this.slot = slot;
        this.slotPerWeek = slotPerWeek;
        this.rating = rating;

    }
    public SubjectDto convertToDto() {
        SubjectDto subjectDto = new SubjectDto(subjectCode,subjectName,price,creatingDate,description,isAvailable,image,slot,slotPerWeek,rating, curriculum.getCurriculumId());
        return subjectDto;
    }

    // Modify Getter & Setter
    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
