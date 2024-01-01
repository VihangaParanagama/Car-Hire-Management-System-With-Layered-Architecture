package lk.ijse.Car_Hire_Management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OverDueRentsTableDto {
    private String id;
    private Integer CarId;
    private Integer CustomerId;
    private Date to_date;
    private Double balance;
    private String Customer_name;
    private String Car;
}
