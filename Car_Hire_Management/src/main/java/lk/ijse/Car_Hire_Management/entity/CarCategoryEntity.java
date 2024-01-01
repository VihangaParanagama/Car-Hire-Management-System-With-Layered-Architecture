package lk.ijse.Car_Hire_Management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Car_Categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarCategoryEntity implements SuperEntity{
    @Id

    private Integer id;
    private String name;
    private Double dailyRentalRate;


    @OneToMany(mappedBy = "carCategoryEntity" , targetEntity = CarEntity.class , cascade = CascadeType.REMOVE)
    List<CarEntity> carEntities;
}
