package lk.ijse.Car_Hire_Management.dao.custom.impl;

import lk.ijse.Car_Hire_Management.dao.custom.UserDao;
import lk.ijse.Car_Hire_Management.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public Boolean save(UserEntity userEntity, Session session) {
        session.save(userEntity);
        return true;
    }

    @Override
    public Boolean update(UserEntity entity, Session session) {
        return null;
    }

    @Override
    public Boolean delete(Integer id, Session session) {

        return null;
    }

    @Override
    public UserEntity get(Integer id, Session session) {
        try {
            Query<Object[]> query = session.createQuery("SELECT id, name FROM UserEntity WHERE id = :id", Object[].class);
            query.setParameter("id", id);

            Object[] result = query.uniqueResult();

            if (result != null) {
                UserEntity userEntity = new UserEntity();
                userEntity.setId((Integer) result[0]);
                userEntity.setName((String) result[1]);


                return userEntity;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserEntity getByUsername(String username, Session session) {
        try {
            Query<UserEntity> query = session.createQuery("FROM UserEntity WHERE username = :username", UserEntity.class);
            query.setParameter("username", username);

            return query.uniqueResult();
        } catch (Exception e) {

            throw new RuntimeException("Error occurred while fetching user by username", e);
        }
    }

    @Override
    public List<UserEntity> getAll(Session session) {
        try {
            String hql = "FROM UserEntity ";
            Query<UserEntity> query = session.createQuery(hql, UserEntity.class);
            List<UserEntity> userEntities = query.list();
            return userEntities;
        } catch (Exception e) {
            throw e;
        }
    }
    @Override
    public Integer getLastUserId(Session session) {
        try {
            Query<Integer> query = session.createQuery("SELECT MAX(u.id) FROM UserEntity u", Integer.class);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting the last user ID", e);
        }
    }



}
