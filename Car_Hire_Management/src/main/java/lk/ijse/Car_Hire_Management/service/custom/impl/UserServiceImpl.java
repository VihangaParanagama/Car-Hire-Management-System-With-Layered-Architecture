package lk.ijse.Car_Hire_Management.service.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.Car_Hire_Management.dao.DaoFactory;
import lk.ijse.Car_Hire_Management.dao.custom.UserDao;
import lk.ijse.Car_Hire_Management.db.SessionFactoryConfiguration;
import lk.ijse.Car_Hire_Management.dto.UserDto;
import lk.ijse.Car_Hire_Management.entity.UserEntity;
import lk.ijse.Car_Hire_Management.service.custom.UserService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl() {
        userDao = (UserDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.USER);
    }

    @Override
    public boolean saveUser(UserDto userDto) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            String newUserName = userDto.getUsername().trim();

            List<UserDto> userDtos = getAllUsers();
            boolean userNameExists = userDtos.stream().anyMatch(u -> u.getUsername().equals(newUserName));

            if (userNameExists) {
                new Alert(Alert.AlertType.WARNING, "Username already exists!").show();
                return false;
            }else {

                UserEntity userEntity = new UserEntity();
                userEntity.setId(userDto.getId());
                userEntity.setName(userDto.getName());
                userEntity.setEmail(userDto.getEmail());
                userEntity.setMobile(userDto.getMobile());
                userEntity.setUsername(newUserName);
                userEntity.setNIC(userDto.getNIC());

                String hashedPassword = hashPassword(userDto.getHashedPassword());
                userEntity.setPassword(hashedPassword);

                boolean save = userDao.save(userEntity, session);

                if (save) {
                    transaction.commit();
                    return true;
                } else {
                    transaction.rollback();
                    return false;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving user", e);
        }
    }

    @Override
    public UserDto getUser(String userName, String enteredPassword) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            UserEntity userEntity = userDao.getByUsername(userName, session);

            transaction.commit();

            if (userEntity != null) {

                String hashedEnteredPassword = hashPassword(enteredPassword);
                return new UserDto(
                        userEntity.getId(),
                        userEntity.getName(),
                        userEntity.getEmail(),
                        userEntity.getMobile(),
                        userEntity.getUsername(),
                        hashedEnteredPassword,
                        userEntity.getNIC()
                );

            } else {
                return null;
            }
        } catch (Exception e) {

            throw new RuntimeException("Error occurred while fetching user", e);
        }
    }



    @Override
    public List<UserDto> getAllUsers() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            try {
                List<UserEntity> userEntities = userDao.getAll(session);
                List<UserDto> userDtoList = new ArrayList<>();

                for (UserEntity userEntity : userEntities) {
                    UserDto userDto = new UserDto();
                    userDto.setId(userEntity.getId());
                    userDto.setName(userEntity.getName());
                    userDto.setUsername(userEntity.getUsername());
                    userDto.setEmail(userEntity.getEmail());
                    userDto.setMobile(userEntity.getMobile());
                    userDto.setHashedPassword(userEntity.getPassword());
                    userDto.setNIC(userEntity.getNIC());

                    userDtoList.add(userDto);
                }

                transaction.commit();
                return userDtoList;

            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        } catch (Exception exception) {
            throw exception;
        }
    }



    @Override
    public boolean validateUser(String username, String password) {
        UserDto dto = getUser(username, password);

        if (dto != null) {
            String hashedPassword = hashPassword(password);
            return hashedPassword.equals(dto.getHashedPassword());
        } else {
            return false;
        }
    }

    @Override
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());


            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error occurred while hashing the password", e);
        }
    }

    @Override
    public Integer getLastUserId() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            return userDao.getLastUserId(session);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting the last user ID", e);
        }
    }




}
