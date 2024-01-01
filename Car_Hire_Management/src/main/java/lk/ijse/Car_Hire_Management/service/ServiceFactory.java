package lk.ijse.Car_Hire_Management.service;

import lk.ijse.Car_Hire_Management.service.custom.SuperService;
import lk.ijse.Car_Hire_Management.service.custom.impl.*;
import lk.ijse.Car_Hire_Management.service.util.ServiceType;
import lombok.SneakyThrows;

public class ServiceFactory {
    public static final String name = null;
    private static ServiceFactory instance;

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return (instance == null) ? instance = new ServiceFactory() : instance;
    }

    @SneakyThrows
    public <T extends SuperService> T getService(ServiceType type) throws Exception {
        switch (type) {

            case USER:
                return (T) new UserServiceImpl();


            case CUSTOMER:
                return (T) new CustomerServiceImpl();


            case CAR_CATEGORY:
                return (T) new CarCategoryServiceImpl();

            case CAR:
                return (T) new CarServiceImpl();

            case RENT:
                return (T) new RentServiceImpl();

            default:
                throw new RuntimeException("invalid service type!");
        }
    }
}
