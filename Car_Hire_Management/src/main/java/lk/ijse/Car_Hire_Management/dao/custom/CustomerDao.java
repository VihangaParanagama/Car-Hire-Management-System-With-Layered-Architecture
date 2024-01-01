package lk.ijse.Car_Hire_Management.dao.custom;

import lk.ijse.Car_Hire_Management.dao.CrudDao;
import lk.ijse.Car_Hire_Management.dto.CustomerDto;
import lk.ijse.Car_Hire_Management.entity.CustomerEntity;
import org.hibernate.Session;

import java.util.List;

public interface CustomerDao extends CrudDao<CustomerEntity> {
    String getCustomerById(Integer customerId, Session session);

    Integer getLastCustomerId(Session session);

    Integer countAllCustomers(Session session);
}
