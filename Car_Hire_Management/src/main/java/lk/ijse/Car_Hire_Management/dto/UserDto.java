package lk.ijse.Car_Hire_Management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer id;
    private String name;
    private String email;
    private String mobile;
    private String username;
    private String hashedPassword;
    private String NIC;


}
