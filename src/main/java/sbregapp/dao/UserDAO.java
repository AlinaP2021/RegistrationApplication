package sbregapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sbregapp.form.UserForm;
import sbregapp.model.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

@Repository
public class UserDAO {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    public User findUserByUserName(String userName) {
        String sql = "select * from users where user_name=?";
        Query query = entityManager.createNativeQuery(sql, User.class);
        query.setParameter(1, userName);
        List<User> users = query.getResultList();
        if (users.size() != 0) {
            return users.get(0);
        } else return null;
    }

    public User findUserByEmail(String email) {
        String sql = "select * from users where email=?";
        Query query = entityManager.createNativeQuery(sql, User.class);
        query.setParameter(1, email);
        List<User> users = query.getResultList();
        if (users.size() != 0) {
            return users.get(0);
        } else return null;
    }

    public List<User> getUsers() {
        String sql = "select * from users";
        Query query = entityManager.createNativeQuery(sql, User.class);
        return query.getResultList();
    }

    @Transactional
    public User createUser(UserForm form) {
        String sql = "insert into users(user_name, user_first_name, user_last_name," +
                "enabled, gender, email, encryted_password, country) " +
                "values (?, ?, ?, false, ?, ?, ?, ?)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, form.getUserName());
        query.setParameter(2, form.getUserFirstName());
        query.setParameter(3, form.getUserLastName());
        query.setParameter(4, form.getGender());
        query.setParameter(5, form.getEmail());
        String encrytedPassword = passwordEncoder.encode(form.getPassword());
        query.setParameter(6, encrytedPassword);
        query.setParameter(7, form.getCountryCode());
        query.executeUpdate();
        sql = "select * from users where user_name=?";
        query = entityManager.createNativeQuery(sql, User.class);
        query.setParameter(1, form.getUserName());
        return (User) query.getResultList().get(0);
    }

}
