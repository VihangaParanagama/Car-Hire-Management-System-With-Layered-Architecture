package lk.ijse.Car_Hire_Management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentTableModelDto {
    private String id;
    private Integer carId;
    private String car;
    private String customerName;
    private Double balance;
    private Date fromDate;
    private Date toDate;
    private String isReturn;
}
