package lk.ijse.Car_Hire_Management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rent")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentEntity implements SuperEntity{

    @Id
    private Integer id;
    private Date from_date;
    private Date to_date;
    private Double perDay_rent;
    private Double advanced_payment;
    private Double balance;
    private Double total;
    private String is_return;
    private String Customer_name;
    private String Car;
    private Double GrandTotal;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Car_id", nullable = false)
    private CarEntity carEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Customer_id", nullable = false)
    private CustomerEntity customerEntity;

}
