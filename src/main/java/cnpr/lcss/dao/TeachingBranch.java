package cnpr.lcss.dao;

import cnpr.lcss.model.TeachingBranchBasicInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "teaching_branch")
public class TeachingBranch implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "starting_date")
    private Date startingDate;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    //<editor-fold desc="Convert to TeachingBranchBasicInfoDto">
    public TeachingBranchBasicInfoDto convertToTeachingBranchBasicInfoDto() {
        TeachingBranchBasicInfoDto dto = new TeachingBranchBasicInfoDto();
        dto.setBranchId(branch.getBranchId());
        dto.setStartingDate(startingDate);
        return dto;
    }
    //</editor-fold>
}
