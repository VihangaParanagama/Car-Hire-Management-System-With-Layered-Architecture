package lk.ijse.Car_Hire_Management.dao.custom.impl;

import lk.ijse.Car_Hire_Management.dao.custom.CustomerDao;
import lk.ijse.Car_Hire_Management.dto.CustomerDto;
import lk.ijse.Car_Hire_Management.entity.CarCategoryEntity;
import lk.ijse.Car_Hire_Management.entity.CustomerEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public Boolean save(CustomerEntity customerentity, Session session) {
        session.save(customerentity);
        return true;
    }

    @Override
    public Boolean update(CustomerEntity customerEntity, Session session) {

        try{
            session.update(customerEntity);
            return true;
        }
        catch (Exception exception){
            throw exception;
        }
    }

    @Override
    public Boolean delete(Integer id, Session session) {

        try{
            CustomerEntity customerEntity = session.get(CustomerEntity.class,id);

            if(customerEntity!= null){
                session.remove(customerEntity);
                return true;
            }else{
                return false;
            }
        }
        catch (Exception exception){
            throw exception;
        }

    }

    @Override
    public CustomerEntity get(Integer id, Session session) {
        try {
            String hql = "FROM CustomerEntity WHERE id = :id";
            Query<CustomerEntity> query = session.createQuery(hql, CustomerEntity.class);
            query.setParameter("id", id);

            List<CustomerEntity> resultList = query.getResultList();

            if (resultList.isEmpty()) {
                return null;
            } else {
                return resultList.get(0);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CustomerEntity> getAll(Session session) {

        try {
            String hql = "FROM  CustomerEntity";
            Query<CustomerEntity> query = session.createQuery(hql , CustomerEntity.class);
            List<CustomerEntity> customerEntityList = query.list();
            return customerEntityList;


        } catch (Exception e) {
            throw e;


        }
    }


    @Override
    public String getCustomerById(Integer customerId,Session session) {
        try {
            String hql = "SELECT ce.name FROM CustomerEntity ce WHERE ce.id = :id";
            Query<String> query = session.createQuery(hql, String.class);
            query.setParameter("id", customerId);

            List<String> resultList = query.getResultList();

            if (resultList.isEmpty()) {
                return null;
            } else {
                return resultList.get(0);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getLastCustomerId(Session session) {
        try {
            Query<Integer> query = session.createQuery("SELECT MAX(c.id) FROM CustomerEntity c", Integer.class);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting the last user ID", e);
        }
    }

    @Override
    public Integer countAllCustomers(Session session) {
        try {

            String hql = "SELECT COUNT(*) FROM CustomerEntity";

            Query<Long> query = session.createQuery(hql, Long.class);

            Long count = query.uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }


}
