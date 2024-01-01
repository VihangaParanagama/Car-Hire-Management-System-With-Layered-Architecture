package lk.ijse.Car_Hire_Management.service.custom;

import com.mysql.cj.Session;
import lk.ijse.Car_Hire_Management.dto.RentDto;
import lk.ijse.Car_Hire_Management.dto.UserDto;

import java.util.List;

public interface UserService extends SuperService{
    boolean saveUser(UserDto userDto);
    UserDto getUser(String userName, String enteredPassword);

    List<UserDto> getAllUsers();

    boolean validateUser(String username, String password);

    String hashPassword(String password);

    Integer getLastUserId();

}
