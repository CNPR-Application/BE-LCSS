package cnpr.lcss.dao;

import cnpr.lcss.model.RegisteringGuestSearchResponseDto;
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
@Table(name = "registering_guest")
public class RegisteringGuest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "city")
    private String city;
    @Column(name = "booking_date")
    private Date bookingDate;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "curriculum_id")
    private Curriculum curriculum;

    public RegisteringGuestSearchResponseDto convertToDto() {
        RegisteringGuestSearchResponseDto registeringGuestSearchResponseDto = new RegisteringGuestSearchResponseDto(id, customerName, phone, city, bookingDate, curriculum.getCurriculumId(),curriculum.getCurriculumName(), description, branch.getBranchId(), status);
        return registeringGuestSearchResponseDto;
    }
}
