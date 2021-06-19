package cnpr.lcss.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "subject")
public class    Subject implements Serializable {

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
    @Column(name="curriculum_id")
    private int curriculumIdd;

    @OneToMany(mappedBy = "subject",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SubjectDetail> subjectDetailList;

//    @ManyToOne
//    @JoinColumn(name = "curriculum_id")
//    private Curriculum curriculum;
}
