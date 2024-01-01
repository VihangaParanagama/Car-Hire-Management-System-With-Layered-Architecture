package lk.ijse.Car_Hire_Management.dao.custom.impl;

import lk.ijse.Car_Hire_Management.dao.DaoFactory;
import lk.ijse.Car_Hire_Management.dao.custom.CarDao;
import lk.ijse.Car_Hire_Management.dao.custom.RentDao;
import lk.ijse.Car_Hire_Management.entity.RentEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class RentDaoImpl implements RentDao {

    private CarDao carDao;

    public RentDaoImpl() {
        carDao = (CarDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR);
    }
    @Override
    public Boolean save(RentEntity entity, Session session) {
        session.persist(entity); // Changed from save to persist
        return true;
    }



    @Override
    public Boolean update(RentEntity entity, Session session) {
        try{
            session.update(entity);
            return true;
        }
        catch (Exception exception){
            throw exception;
        }
    }

    @Override
    public Boolean delete(Integer id, Session session) {


        try{
            RentEntity rentEntity = session.get(RentEntity.class,id);

            Integer carId = getCarIdByRentId(id,session);

            boolean updateAvailability = carDao.updateAvailabilityById(carId,"Yes",session);

            if(rentEntity!= null && updateAvailability){
                session.remove(rentEntity);
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
    public RentEntity get(Integer id, Session session) {
        try {
            String hql = "FROM RentEntity WHERE id = :id";
            Query<RentEntity> query = session.createQuery(hql, RentEntity.class);
            query.setParameter("id", id);

            List<RentEntity> resultList = query.getResultList();

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
    public List<RentEntity> getAll(Session session) {
        try {
            String hql = "FROM  RentEntity ";
            Query<RentEntity> query = session.createQuery(hql , RentEntity.class);
            List<RentEntity> rentEntities = query.list();
            return rentEntities;


        } catch (Exception e) {
            throw e;


        }
    }

    @Override
    public String getLastRentIsReturnByCustomerId(Integer customerId, Session session) {
        try {
            String hql = "SELECT r.is_return FROM RentEntity r " +
                    "WHERE r.customerEntity.id = :customerId " +
                    "ORDER BY r.id DESC";

            Query<String> query = session.createQuery(hql, String.class);
            query.setParameter("customerId", customerId);
            query.setMaxResults(1);  // To get only the latest rent

            return query.uniqueResult();
        } catch (HibernateException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while fetching the last rent's is_return value", e);
        }
    }

    @Override
    public Date getToDateById(Integer rentId, Session session) {
        try {
            String hql = "SELECT re.to_date FROM RentEntity re WHERE re.id = :id";
            Query<Date> query = session.createQuery(hql, Date.class);
            query.setParameter("id", rentId);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Double getCarPricePerDayByRentId(Integer rentId, Session session) {
        try {
            String hql = "SELECT CAST(re.carEntity.Price_per_day AS double) FROM RentEntity re WHERE re.id = :id";
            Query<Double> query = session.createQuery(hql, Double.class);
            query.setParameter("id", rentId);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Double getValueBetweenTotalAndBalanceByRentId(Integer rentId, Session session) {
        try {
            String hql = "SELECT (re.total - re.balance) FROM RentEntity re WHERE re.id = :id";
            Query<Double> query = session.createQuery(hql, Double.class);
            query.setParameter("id", rentId);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getCarIdByRentId(Integer rentId, Session session) {
        try {
            String hql = "SELECT re.carEntity.id FROM RentEntity re WHERE re.id = :id";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("id", rentId);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean UpdateIsReturnByRentId(Integer RentId, String Isreturn, Session session) {
        try {
            String hql = "UPDATE RentEntity re SET re.is_return = :isReturn WHERE re.id = :rentId";
            Query query = session.createQuery(hql);
            query.setParameter("isReturn", Isreturn);
            query.setParameter("rentId", RentId);

            int rowCount = query.executeUpdate();

            return rowCount > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error updating is_return for RentId: " + RentId, e);
        }
    }

    @Override
    public List<RentEntity> getAllOverdueRents(Session session) {
        try {
            String hql = "FROM RentEntity r " +
                    "WHERE r.is_return = :isReturn " +
                    "AND r.to_date < :currentDate";

            Query<RentEntity> query = session.createQuery(hql, RentEntity.class);
            query.setParameter("isReturn", "No");
            query.setParameter("currentDate", new Date());

            List<RentEntity> overdueRents = query.getResultList();

            return overdueRents;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Integer getLastRentId(Session session) {
        try {
            Query<Integer> query = session.createQuery("SELECT MAX(r.id) FROM RentEntity r", Integer.class);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting the last user ID", e);
        }
    }

    @Override
    public List<Integer> getAllCustomerIdsInARent(Session session) {
        try{
            String hql = "SELECT DISTINCT r.customerEntity.id FROM RentEntity r WHERE r.is_return = 'No' OR r.is_return IS NULL";

            Query<Integer> query = session.createQuery(hql, Integer.class);

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Integer> getAllRentIdsInARent(Session session) {
        try {
            String hql = "SELECT DISTINCT r.id FROM RentEntity r WHERE r.is_return = 'No' OR r.is_return IS NULL";

            Query<Integer> query = session.createQuery(hql, Integer.class);

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    public void updateForeignKeyToNull(Integer carId, Session session) {
        try {
            String hql = "UPDATE RentEntity SET carEntity = null WHERE carEntity.id = :carId";
            Query query = session.createQuery(hql);
            query.setParameter("carId", carId);
            query.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String checkCustomerInRentById(Integer customerID, Session session) {
        try {

            String hql = "SELECT re.is_return FROM RentEntity re " +
                    "WHERE re.customerEntity.id = :customerId " +
                    "ORDER BY re.id DESC";

            Query<String> query = session.createQuery(hql, String.class);
            query.setParameter("customerId", customerID);
            query.setMaxResults(1);

            String result = query.uniqueResult();
            return (result != null) ? result : "Customer not found in Rent Table";
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public String checkCarInRentById(Integer carID, Session session) {
        try {
            String hql = "SELECT COUNT(*) FROM RentEntity re WHERE re.carEntity.id = :carId";

            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("carId", carID);

            Long count = query.uniqueResult();
            return (count > 0) ? "Yes" : "No";
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer countAllRents(Session session) {
        try {

            String hql = "SELECT COUNT(*) FROM RentEntity ";

            Query<Long> query = session.createQuery(hql, Long.class);

            Long count = query.uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public Double getTotalRentAmount(Session session) {
        try {

            String hql = "SELECT COALESCE(SUM(re.total), 0) FROM RentEntity re";
            Query<Double> query = session.createQuery(hql, Double.class);

            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting the total rent amount", e);
        }
    }

    @Override
    public Double getTotalDifferenceAmount(Session session) {
        try {

            String hql = "SELECT COALESCE(SUM(re.GrandTotal - re.total), 0) FROM RentEntity re";
            Query<Double> query = session.createQuery(hql, Double.class);

            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting the total difference amount", e);
        }
    }

    @Override
    public Double getGrandTotalAmount(Session session) {
        try {

            String hql = "SELECT COALESCE(SUM(re.GrandTotal), 0) FROM RentEntity re";
            Query<Double> query = session.createQuery(hql, Double.class);

            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting the total rent amount", e);
        }
    }
}

