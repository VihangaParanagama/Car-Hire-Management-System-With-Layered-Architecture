package lk.ijse.Car_Hire_Management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements SuperEntity{



    @Id
    private Integer id;
    private String name;
    private String email;
    private String mobile;
    private String username;
    private String password;
    private String NIC;


}
