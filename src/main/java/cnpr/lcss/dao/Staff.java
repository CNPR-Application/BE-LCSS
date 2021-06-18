package cnpr.lcss.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "staff")
public class Staff implements Serializable {

    @Id
    @Column(name = "staff_username")
    private String staffUsername;

    @OneToOne
    @JoinColumn(name = "username")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
}
