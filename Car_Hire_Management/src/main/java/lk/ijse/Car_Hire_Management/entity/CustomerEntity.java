package lk.ijse.Car_Hire_Management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity implements SuperEntity{
    @Id
    private Integer id;

    @Column(unique = true)
    private String name;
    private String nic;

    private String Address;
    private String mobile;


    @OneToMany(mappedBy = "carEntity" , targetEntity = RentEntity.class , cascade = CascadeType.REMOVE)
    List<RentEntity> rentEntities;
}
