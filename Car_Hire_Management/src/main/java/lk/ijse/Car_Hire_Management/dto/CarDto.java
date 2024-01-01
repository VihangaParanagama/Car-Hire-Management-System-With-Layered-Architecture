package lk.ijse.Car_Hire_Management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    private Integer id;

    private String brand;

    private String model;

    private String year;

    private String car_number;

    private Double Price_per_day;

    private String availability;

    private Integer carCategoryId;

    private String vehicle_category;


}
