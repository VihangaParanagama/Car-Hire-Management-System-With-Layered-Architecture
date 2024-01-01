package lk.ijse.Car_Hire_Management.service.custom;

import lk.ijse.Car_Hire_Management.dto.CarCategoryDto;

import java.util.List;

public interface CarCategoryService extends SuperService{
    boolean saveCategory(CarCategoryDto carCategoryDto);

    boolean deleteCategory(CarCategoryDto id);

    boolean UpdateCAtegory(CarCategoryDto carCategoryDto);

    Object getCategory(String name);

    List<CarCategoryDto> getAllCategory();

    CarCategoryDto getCarCategoryByName(String categoryName);

    Integer getLastCarCategoryId();

    Double getPerDayRentByCarCategory(String carCategory);

    Integer countAllCarcategories();

}
