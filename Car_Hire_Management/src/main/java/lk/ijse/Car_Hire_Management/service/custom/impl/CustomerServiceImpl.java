package lk.ijse.Car_Hire_Management.service.custom.impl;

import lk.ijse.Car_Hire_Management.dao.DaoFactory;
import lk.ijse.Car_Hire_Management.dao.custom.CarDao;
import lk.ijse.Car_Hire_Management.dao.custom.CustomerDao;
import lk.ijse.Car_Hire_Management.dao.custom.RentDao;
import lk.ijse.Car_Hire_Management.db.SessionFactoryConfiguration;
import lk.ijse.Car_Hire_Management.dto.CustomerDto;
import lk.ijse.Car_Hire_Management.entity.CustomerEntity;
import lk.ijse.Car_Hire_Management.service.custom.CustomerService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao;
    private CarDao carDao;

    private RentDao rentDao;
    public CustomerServiceImpl() {
        customerDao = (CustomerDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CUSTOMER);
        carDao = (CarDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR);
        rentDao = (RentDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.RENT);
    }

    @Override
    public boolean saveCus(CustomerDto customerdto) {
        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setId(Integer.valueOf(customerdto.getId()));
        customerEntity.setName(customerdto.getName());
        customerEntity.setNic(customerdto.getNic());
        customerEntity.setAddress(customerdto.getAddress());
        customerEntity.setMobile(customerdto.getMobile());

        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Boolean save = customerDao.save(customerEntity,session);
            if (save) {
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        }
    }
    @Override
    public String deleteCustomer(Integer id) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            Boolean delete = customerDao.delete(id, session);

            if (delete) {
                transaction.commit();
                return "Delete successful";
            } else {
                transaction.rollback();
                return "Delete failed";
            }

        } catch (Exception exception) {
            throw exception;
        }
    }

    @Override
    public Integer countAllCustomers() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {

            return customerDao.countAllCustomers(session);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean updateCus(CustomerDto customerDto) {

        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setId(Integer.valueOf(customerDto.getId()));
        customerEntity.setName(customerDto.getName());
        customerEntity.setNic(customerDto.getNic());
        customerEntity.setAddress(customerDto.getAddress());
        customerEntity.setMobile(customerDto.getMobile());

        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            Boolean update = customerDao.update(customerEntity,session);
            if (update) {
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        }

    }

    @Override
    public CustomerDto getCus(Integer id) {
        return null;

    }

    @Override
    public List<CustomerDto> getAllCus() {

        try{Session session = SessionFactoryConfiguration.getInstance().getSession();
            Transaction transaction = session.getTransaction();
            transaction.begin();

            List<CustomerEntity> customerEntities = customerDao.getAll(session);

            List<CustomerDto> customerDtos = new ArrayList<>();



            for (CustomerEntity customerEntity : customerEntities) {
                customerDtos.add(new CustomerDto(customerEntity.getId(),
                        customerEntity.getName(),
                        customerEntity.getNic(),
                        customerEntity.getAddress(),
                        customerEntity.getMobile()));


            }
            transaction.commit();
            return customerDtos;

        }

        catch (Exception exception){
            throw exception;
        }



    }

    @Override
    public CustomerEntity getCustomerById(Integer customerId) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            try {

                CustomerEntity customerEntity = session.get(CustomerEntity.class, customerId);

                transaction.commit();
                return customerEntity;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public Integer getLastCustomerId() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            return customerDao.getLastCustomerId(session);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting the last user ID", e);
        }
    }


}
