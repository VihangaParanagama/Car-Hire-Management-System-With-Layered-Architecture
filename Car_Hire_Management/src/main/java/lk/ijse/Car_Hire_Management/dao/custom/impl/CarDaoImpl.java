    package lk.ijse.Car_Hire_Management.dao.custom.impl;

    import lk.ijse.Car_Hire_Management.dao.custom.CarDao;
    import lk.ijse.Car_Hire_Management.entity.CarEntity;
    import org.hibernate.Session;
    import org.hibernate.query.Query;

    import java.util.List;

    public class CarDaoImpl implements CarDao {
        @Override
        public Boolean save(CarEntity entity, Session session) {
            try {
                session.merge(entity);
                return true;
            } catch (Exception exception) {
                throw exception;
            }
        }


        @Override
        public Boolean update(CarEntity entity, Session session) {
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
                CarEntity carEntity = session.get(CarEntity.class,id);

                if(carEntity!= null){
                    session.remove(carEntity);
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
        public CarEntity get(Integer id, Session session) {
            try {
                String hql = "FROM CarEntity WHERE id = :id";
                Query<CarEntity> query = session.createQuery(hql, CarEntity.class);
                query.setParameter("id", id);

                List<CarEntity> resultList = query.getResultList();

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
        public List<CarEntity> getAll(Session session) {
            try {
                String hql = "FROM CarEntity";
                Query<CarEntity> query = session.createQuery(hql, CarEntity.class);
                List<CarEntity> carEntities = query.list();
                return carEntities;
            } catch (Exception e) {
                throw e;
            }
        }

        @Override
        public boolean updateAvailabilityById(Integer carId, String newAvailability, Session session) {
            try {
                String hql = "UPDATE CarEntity SET availability = :availability WHERE id = :carId";
                Query query = session.createQuery(hql);
                query.setParameter("availability", newAvailability);
                query.setParameter("carId", carId);

                int rowCount = query.executeUpdate();

                return rowCount > 0;
            } catch (Exception exception) {
                throw exception;
            }


    }

        @Override
        public Double getPerdayRentByCarId(Integer carId, Session session) {
            try {
                String hql = "SELECT ce.Price_per_day FROM CarEntity ce WHERE ce.id = :id";
                Query<Double> query = session.createQuery(hql, Double.class);
                query.setParameter("id", carId);

                List<Double> resultList = query.getResultList();

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
        public Integer getLastCarId(Session session) {
            try {
                Query<Integer> query = session.createQuery("SELECT MAX(c.id) FROM CarEntity c", Integer.class);
                return query.uniqueResult();
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while getting the last car ID", e);
            }
        }

        @Override
        public List<Integer> getAvailableCarIdsByCarCategory(String CarCategory, Session session) {
            try {
                session.beginTransaction();

                String hql = "SELECT c.id FROM CarEntity c WHERE c.vehicle_category = :category AND c.availability = 'Yes'";
                Query<Integer> query = session.createQuery(hql, Integer.class);
                query.setParameter("category", CarCategory);

                List<Integer> availableCarIds = query.getResultList();

                session.getTransaction().commit();

                return availableCarIds;
            } catch (Exception e) {
                if (session.getTransaction() != null) {
                    session.getTransaction().rollback();
                }
                e.printStackTrace();
                throw e;
            }
        }

        @Override
        public String checkCarInRentById(Integer carID, Session session) {
            try {

                String hql = "SELECT ce.availability FROM CarEntity ce " +
                        "WHERE ce.id = :carId";


                Query<String> query = session.createQuery(hql, String.class);
                query.setParameter("carId", carID);


                String isReturnStatus = query.uniqueResult();
                return (isReturnStatus != null) ? isReturnStatus : "Car not found";
            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        }

        @Override
        public Integer countAllCarCars(Session session) {
            try {

                String hql = "SELECT COUNT(*) FROM CarEntity ";

                Query<Long> query = session.createQuery(hql, Long.class);

                Long count = query.uniqueResult();
                return count != null ? count.intValue() : 0;
            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        }

        @Override
        public Long countAllCarInaRent(Session session) {
            try {
                String hql = "SELECT COUNT(*) FROM CarEntity c WHERE c.availability = 'No'";
                Query<Long> query = session.createQuery(hql, Long.class);
                return query.uniqueResult();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


    }
