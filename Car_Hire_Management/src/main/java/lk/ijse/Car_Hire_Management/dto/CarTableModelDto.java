package lk.ijse.Car_Hire_Management.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarTableModelDto {
    private Integer id;
    private String brand;
    private String model;
    private String year;
    private String vehicle_number;
    private Double price_per_day;
    private String availability;
    private String vehicle_category;
}
