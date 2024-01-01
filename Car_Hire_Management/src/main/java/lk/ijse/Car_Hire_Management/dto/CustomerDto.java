package lk.ijse.Car_Hire_Management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Integer id;
    private String nic;
    private String name;
    private String Address;
    private String mobile;
}
