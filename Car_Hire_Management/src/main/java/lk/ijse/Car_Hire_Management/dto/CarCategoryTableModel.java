package lk.ijse.Car_Hire_Management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarCategoryTableModel {
    private Integer id;
    private String name;
    private Double dailyRentalRate;
}
