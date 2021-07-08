package cnpr.lcss.dao;

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
@Table(name = "booking")
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;
    @Column(name = "paying_price")
    private float payingPrice;
    @Column(name = "paying_date")
    private Date payingDate;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;
}
