package lk.ijse.Car_Hire_Management.service.custom;

import lk.ijse.Car_Hire_Management.dto.RentDto;
import lk.ijse.Car_Hire_Management.entity.RentEntity;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface RentService extends SuperService{
    boolean saveRent(RentDto rentDto);
    boolean deleteRent(Integer id);

    boolean updateRent(RentDto rentDto);

    RentEntity getRent(Integer id);

    List<RentDto> getAllRent();

    String getLastRentIsReturnByCustomerId(Integer customerId);

    boolean rentalDeurationLimit(LocalDate startDate, LocalDate endDate);

    Double calculatePricaPerDay(Double perDayRentCar,Double PricePerDayCarCategory);

    Double calculateTotal(Double perDayRent,LocalDate startDate, LocalDate endDate);

    Double calculateBalance(Double total,Double AdvancedPayent);

    Double calculateExtraDates(Date ToDate,LocalDate ReturnDate);

    Date getToDateById(Integer rentId);

    Double calculateChargesForExtraDates(Double ExtraDates,Integer RentId);

    Double calculateGrandTotal(Double ExtraCharges,Double balance);

    boolean updateIsReturnByRentId(Integer rentId, String isReturn);

    List<RentDto> getAllOverdueRents();

    Integer getLastRentId();

    List<Integer> getAllCustomerIdsInARent();

    List<Integer> getAllRentIdsInARent();

    boolean saveGrandTotalByRentId(Integer rentId, Double Grandtotal);

    Double getValueBetweenTotalAndBalanceByRentId(Integer rentId);

    String checkCustomerInRentById(Integer customerID);

    String checkCarInRentById(Integer carID);

    Integer countAllRents();

    Integer countOverdueRents();

    Double getTotalRentAmount();

    Double getTotalDifferenceAmount();

    Double getGrandTotalAmount();
}
