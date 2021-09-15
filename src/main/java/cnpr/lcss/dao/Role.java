package cnpr.lcss.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "role")
public class Role implements Serializable {
    @Id
    @Column(name = "role_id")
    private String roleId;
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "role")
    private List<Account> accountList;
}
