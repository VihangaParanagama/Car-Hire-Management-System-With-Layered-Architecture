package lk.ijse.Car_Hire_Management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentDto {
    private String id;
    private Integer CarId;
    private String Car;
    private String Customer_name;
    private Double balance;
    private Date from_date;
    private Date to_date;
    private String is_return;
    private Integer CustomerId;
    private Double perDay_rent;
    private Double advanced_payment;
    private Double total;
    private Double GrandTotal;




}
