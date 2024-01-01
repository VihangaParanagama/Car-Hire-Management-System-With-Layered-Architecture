package lk.ijse.Car_Hire_Management.service.custom.impl;

import lk.ijse.Car_Hire_Management.dao.DaoFactory;
import lk.ijse.Car_Hire_Management.dao.custom.CarDao;
import lk.ijse.Car_Hire_Management.dao.custom.CustomerDao;
import lk.ijse.Car_Hire_Management.dao.custom.RentDao;
import lk.ijse.Car_Hire_Management.db.SessionFactoryConfiguration;
import lk.ijse.Car_Hire_Management.dto.RentDto;
import lk.ijse.Car_Hire_Management.entity.CarEntity;
import lk.ijse.Car_Hire_Management.entity.CustomerEntity;
import lk.ijse.Car_Hire_Management.entity.RentEntity;
import lk.ijse.Car_Hire_Management.service.custom.RentService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public class RentServiceImpl implements RentService {

    CustomerDao customerDao;

    RentDao rentDao;

    CarDao carDao;

    public RentServiceImpl() {
        customerDao = (CustomerDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CUSTOMER);
        rentDao = (RentDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.RENT);
        carDao = (CarDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR);


    }
    @Override
    public boolean saveRent(RentDto rentDto) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            CarEntity carEntity = carDao.get(rentDto.getCarId(), session);
            CustomerEntity customerEntity = customerDao.get(rentDto.getCustomerId(), session);

            RentEntity rentEntity = new RentEntity();
            rentEntity.setId(Integer.valueOf(rentDto.getId()));
            rentEntity.setFrom_date(rentDto.getFrom_date());
            rentEntity.setTo_date(rentDto.getTo_date());
            rentEntity.setPerDay_rent(rentDto.getPerDay_rent());
            rentEntity.setTotal(rentDto.getTotal());
            rentEntity.setAdvanced_payment(rentDto.getAdvanced_payment());
            rentEntity.setBalance(rentDto.getBalance());
            rentEntity.setIs_return(rentDto.getIs_return());
            rentEntity.setCarEntity(carEntity);
            rentEntity.setCustomerEntity(customerEntity);

            rentEntity.setCustomer_name(rentDto.getCustomer_name());
            rentEntity.setCar(rentDto.getCar());

            System.out.println("Model + Brand Service " + rentEntity.getCar());
            System.out.println("Is Return Value(Service) =" + rentEntity.getIs_return());

            boolean save = rentDao.save(rentEntity, session);
            if (save) {
                System.out.println("* Rent save method running*");
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        }
    }

    @Override
    public boolean deleteRent(Integer id) {

        try (Session session = SessionFactoryConfiguration.getInstance().getSession()){

            Transaction transaction = session.getTransaction();
            transaction.begin();
            Boolean delete = rentDao.delete(id, session);
            if (delete) {
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        }
        catch (Exception exception){
            throw exception;
        }

    }

    @Override
    public boolean updateRent(RentDto rentDto) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            RentEntity rentEntity = rentDao.get(Integer.valueOf(rentDto.getId()), session);

            if (rentEntity != null) {
                Double value = getValueBetweenTotalAndBalanceByRentId(Integer.valueOf(rentDto.getId()));

                Double grandTottal = rentDto.getGrandTotal() + value;

                rentEntity.setGrandTotal(grandTottal);
                System.out.println("Is Return Value (Service) =" + rentEntity.getIs_return());

                boolean update = rentDao.update(rentEntity, session);

                if (update) {
                    System.out.println("** Rent update method running**");
                    transaction.commit();
                    return true;
                } else {
                    transaction.rollback();
                    return false;
                }
            } else {
                transaction.rollback();
                return false;
            }
        }
    }


    @Override
    public RentEntity getRent(Integer id) {

        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            Optional<RentEntity> optionalRentEntity = Optional.ofNullable(rentDao.get(id, session));
            return optionalRentEntity.orElse(null);

        } catch (Exception exception) {
            throw exception;
        }


    }

    @Override
    public List<RentDto> getAllRent() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            try {
                List<RentEntity> rentEntities = rentDao.getAll(session);
                List<RentDto> rentDtoList = new ArrayList<>();

                for (RentEntity rentEntity : rentEntities) {
                    RentDto rentDto = new RentDto();
                    rentDto.setId(String.valueOf(rentEntity.getId()));

                    CarEntity carEntity = rentEntity.getCarEntity();
                    if (carEntity != null) {
                        rentDto.setCarId(carEntity.getId());
                    }


                    rentDto.setCar(rentEntity.getCar());
                    rentDto.setCustomer_name(rentEntity.getCustomer_name());
                    rentDto.setBalance(rentEntity.getBalance());
                    rentDto.setFrom_date(rentEntity.getFrom_date());
                    rentDto.setTo_date(rentEntity.getTo_date());
                    rentDto.setIs_return(rentEntity.getIs_return());
                    rentDto.setAdvanced_payment(rentEntity.getAdvanced_payment());
                    rentDto.setPerDay_rent(rentEntity.getPerDay_rent());
                    rentDto.setTotal(rentEntity.getTotal());
                    CustomerEntity customerEntity = rentEntity.getCustomerEntity();
                    if (customerEntity != null) {
                        rentDto.setCustomerId(customerEntity.getId());
                    }
                    rentDtoList.add(rentDto);
                }

                transaction.commit();
                return rentDtoList;

            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        } catch (Exception exception) {
            throw exception;
        }
    }

    @Override
    public String getLastRentIsReturnByCustomerId(Integer customerId) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            try {
                String lastRentIsReturn = rentDao.getLastRentIsReturnByCustomerId(customerId, session);

                transaction.commit();

                if (lastRentIsReturn == null || "Yes".equals(lastRentIsReturn)) {
                    System.out.println("Can rent a Car");
                } else if ("No".equals(lastRentIsReturn)) {
                    System.out.println("This customer has already rented a car");
                }

                return lastRentIsReturn;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }

        }
    }

    @Override
    public boolean rentalDeurationLimit(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
            System.out.println("Number of days: " + daysBetween);
            if(daysBetween > 0 && daysBetween <= 30){
                return true;
            }else{
                return false;
            }

        } else {

            System.out.println("Please provide both start and end dates.");
            return false;
        }
    }

    @Override
    public Double calculatePricaPerDay(Double perDayRentCar, Double PricePerDayCarCategory) {
        Double calculatePricaPerDay = perDayRentCar+ PricePerDayCarCategory;
        return calculatePricaPerDay;
    }

    @Override
    public Double calculateTotal(Double perDayRent, LocalDate startDate, LocalDate endDate) {
        Double daysBetween = (double) ChronoUnit.DAYS.between(startDate, endDate);
        Double total = daysBetween*perDayRent;
        return total;
    }

    @Override
    public Double calculateBalance(Double total,Double AdvancedPayent) {
        Double balance = total - AdvancedPayent;
        return balance;
    }

    @Override
    public Double calculateExtraDates(Date Todate, LocalDate ReturnDate) {
        LocalDate localTodate = Todate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Double ExtraDays = (double) ChronoUnit.DAYS.between(localTodate, ReturnDate);
        if(ExtraDays>0) {
            return ExtraDays;
        }else{
            return null;
        }
    }


    @Override
    public Date getToDateById(Integer rentId) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            try {
                Date toDate = rentDao.getToDateById(rentId, session);
                transaction.commit();

                if (toDate == null) {
                    System.out.println("ToDate is null");
                }

                return toDate;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }

    }

    @Override
    public Double calculateChargesForExtraDates(Double extraDates, Integer rentId) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            try {
                Double pricePerDay = rentDao.getCarPricePerDayByRentId(rentId, session);
                transaction.commit();
                System.out.println(pricePerDay);

                Double extraCharge = pricePerDay * 1.5 * extraDates;

                return extraCharge;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public Double calculateGrandTotal(Double ExtraCharges, Double balance) {
        Double grandTotal = ExtraCharges + balance;
        return grandTotal;
    }

    @Override
    public boolean updateIsReturnByRentId(Integer rentId, String isReturn) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();

            try {
                transaction.begin();


                RentEntity rentEntity = rentDao.get(rentId, session);

                if (rentEntity != null) {

                    rentEntity.setIs_return(isReturn);


                    rentDao.update(rentEntity, session);

                    transaction.commit();

                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Error updating is_return for RentId: " + rentId, e);
            }
        }
    }
    @Override
    public List<RentDto> getAllOverdueRents() {
        List<RentDto> rentDtos = new ArrayList<>();

        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            List<RentEntity> rentEntities = rentDao.getAllOverdueRents(session);

            for (RentEntity rentEntity : rentEntities) {
                RentDto rentDto = new RentDto();

                rentDto.setId(String.valueOf(rentEntity.getId()));
                rentDto.setCarId(rentEntity.getCarEntity().getId());
                rentDto.setCustomerId(rentEntity.getCarEntity().getId());
                rentDto.setFrom_date(rentEntity.getFrom_date());
                rentDto.setTo_date(rentEntity.getTo_date());
                rentDto.setPerDay_rent(rentEntity.getPerDay_rent());
                rentDto.setAdvanced_payment(rentEntity.getAdvanced_payment());
                rentDto.setBalance(rentEntity.getBalance());
                rentDto.setTotal(rentEntity.getTotal());
                rentDto.setIs_return(rentEntity.getIs_return());
                rentDto.setCustomer_name(rentEntity.getCustomer_name());
                rentDto.setCar(rentEntity.getCar());

                rentDtos.add(rentDto);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rentDtos;
    }

    @Override
    public Integer getLastRentId() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            return rentDao.getLastRentId(session);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting the last user ID", e);
        }
    }

    @Override
    public List<Integer> getAllCustomerIdsInARent() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            return rentDao.getAllCustomerIdsInARent(session);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Integer> getAllRentIdsInARent() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            return rentDao.getAllRentIdsInARent(session);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean saveGrandTotalByRentId(Integer rentId, Double Grandtotal) {
        return false;
    }

    @Override
    public Double getValueBetweenTotalAndBalanceByRentId(Integer rentId) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            return rentDao.getValueBetweenTotalAndBalanceByRentId(rentId, session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String checkCustomerInRentById(Integer customerID) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()){
            return rentDao.checkCustomerInRentById(customerID,session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String checkCarInRentById(Integer carID) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()){
            return rentDao.checkCarInRentById(carID,session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer countAllRents() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {

            return rentDao.countAllRents(session);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer countOverdueRents() {
        List<RentDto> overdueRents = getAllOverdueRents();
        return overdueRents.size();
    }

    @Override
    public Double getTotalRentAmount() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {

            return rentDao.getTotalRentAmount(session);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public Double getTotalDifferenceAmount() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {

            return rentDao.getTotalDifferenceAmount(session);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public Double getGrandTotalAmount() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {

            return rentDao.getGrandTotalAmount(session);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }


}
