package lk.ijse.Car_Hire_Management.dao;

import lk.ijse.Car_Hire_Management.dao.custom.impl.*;


public class DaoFactory {
    private static DaoFactory daoFactory;

    private DaoFactory(){}

    public static DaoFactory getInstance(){
        if(daoFactory == null){
            daoFactory = new DaoFactory();
        }
        return daoFactory;
    }

    public static SuperDao getDao(DaoTypes type){
        switch (type) {

            case USER:
                return new UserDaoImpl();

            case CUSTOMER:
                return new CustomerDaoImpl();

            case CAR_CATEGORY:
                return new CarCategoryDaoImpl();

            case CAR:
                return new CarDaoImpl();

            case RENT:
                return new RentDaoImpl();


            default:
                return null ;
        }
    }

    public enum DaoTypes{
        USER, CAR_CATEGORY, CUSTOMER , CAR ,RENT
    }
}
