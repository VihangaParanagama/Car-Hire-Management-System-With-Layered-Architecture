    package lk.ijse.Car_Hire_Management.entity;


    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import javax.persistence.*;
    import java.util.List;

    @Entity
    @Table(name = "Car")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class CarEntity implements SuperEntity {

        @Id
        private Integer id;

        private String brand;
        private String model;
        private String year;
        private String vehicle_category;
        private Double Price_per_day;
        private String availability;
        private String car_Number;

        @ManyToOne
        @JoinColumn(name = "Car_category_id")
        private CarCategoryEntity carCategoryEntity;

        @OneToMany(mappedBy = "carEntity", targetEntity = RentEntity.class, cascade = CascadeType.REMOVE)
        List<RentEntity> rentEntities;
    }
