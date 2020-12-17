package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.RoleDao;
import com.epam.parkingcards.dao.UserDao;
import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.exception.ValidationException;
import com.epam.parkingcards.model.Role;
import com.epam.parkingcards.model.User;
import com.epam.parkingcards.service.utils.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private IdValidator idValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;


    public long register(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(getDefaultRoles());
            return userDao.save(user).getId();

        } catch (DataAccessException e) {
            throw new DaoException(String.format("Creation error, User: %s ", user), e);
        }
    }

    public User findById(long id) {

        idValidator.validate(id);
        return userDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("By id %d, User not found", id)));
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("By email %s, User not found", email)));
    }

    public User findByLicensePlate(String licensePlate) {
        //TODO implement
        return null;
    }

    public List<User> findAll(int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<User> result = userDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("Result is empty, page number = %d, page size = %d", pageNumber, PAGE_SIZE));
        }
        return result;
    }

    @Transactional
    public User update(User user) {

        idValidator.validate(user.getId());
        validateForExistence(user.getId());
        User fromDb = userDao.getOne(user.getId());
        user.setPassword(fromDb.getPassword());
        //TODO fix. Named query for update
        try {
            return userDao.saveAndFlush(user);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Updating error, User: %s", user), e);
        }
    }

    public void assignRole(long userId, long roleId) {
        //TODO implement
    }

    public void revokeRole(long userId, long roleId) {
        //TODO implement
    }

    public void deleteById(long id) {
        idValidator.validate(id);
        validateForExistence(id);

        try {
            userDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Deleting error: id=%d ", id), e);
        }
    }

    private Set<Role> getDefaultRoles() {

        Role roleUser = roleDao.findById(1L).orElseThrow(
                () -> new NotFoundException("Role not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);

        return roles;
    }

    public void validateForExistence(long id) {
        if (!userDao.existsById(id)) {
            throw new ValidationException(String.format("Not exist, id=%d", id));
        }
    }
}
