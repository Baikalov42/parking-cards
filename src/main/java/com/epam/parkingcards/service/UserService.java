package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.RoleDao;
import com.epam.parkingcards.dao.UserDao;
import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.exception.ValidationException;
import com.epam.parkingcards.model.RoleEntity;
import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.service.utils.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;

    public long register(UserEntity userEntity) {
        try {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setRoleEntities(getDefaultRoles());
            return userDao.save(userEntity).getId();

        } catch (DataAccessException e) {
            throw new DaoException(String.format("Creation error, User: %s ", userEntity), e);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_admin') or @userSecurity.hasUserId(authentication, #id)")
    public UserEntity findById(long id) {
        IdValidator.validate(id);
        return userDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("By id %d, User not found", id)));
    }

    public long getIdByEmail(String email) {
        return this.findByEmail(email).getId();
    }

    public UserEntity findByEmail(String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("By email %s, User not found", email)));
    }

    public UserEntity findByLicensePlate(String licensePlate) {
        return userDao.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new NotFoundException(String.format("By license plate %s, User not found", licensePlate)));
    }

    public List<UserEntity> findAll(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<UserEntity> result = userDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("Result is empty, page number = %d, page size = %d", pageNumber, PAGE_SIZE));
        }
        return result;
    }

    public List<UserEntity> findByKeyword(String keyword) {
        List<UserEntity> result = userDao.findByKeyword(keyword.toLowerCase());
        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("By keyword %s, Users not found", keyword));
        }
        return result;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_admin') or @userSecurity.hasUserId(authentication, #userEntity.id)")
    public UserEntity update(UserEntity userEntity) {

        IdValidator.validate(userEntity.getId());
        validateForExistence(userEntity.getId());

        try {
            return userDao.updateWithoutPasswordAndCars(userEntity);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Updating error, User: %s", userEntity), e);
        }
    }

    public void deleteById(long id) {
        IdValidator.validate(id);
        validateForExistence(id);

        try {
            userDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Deleting error: user id=%d ", id), e);
        }
    }

    private Set<RoleEntity> getDefaultRoles() {
        RoleEntity roleEntityUser = roleDao.findByName("ROLE_user").orElseThrow(
                () -> new NotFoundException("Role not found"));

        Set<RoleEntity> roleEntities = new HashSet<>();
        roleEntities.add(roleEntityUser);

        return roleEntities;
    }


    public void addRole(long userId, long roleId) {
        this.validateForExistence(userId);
        roleService.validateForExistence(roleId);

        UserEntity userEntity = this.findById(userId);
        userEntity.getRoleEntities().add(roleDao.getOne(roleId));
        try {
            userDao.saveAndFlush(userEntity);
        } catch (DataAccessException e) {
            throw new DaoException(String
                    .format("Adding role with id %d to user with id %d failed", roleId, userId), e);
        }
    }

    public void removeRole(long userId, long roleId) {
        this.validateForExistence(userId);
        roleService.validateForExistence(roleId);

        UserEntity userEntity = userDao.getOne(userId);
        userEntity.getRoleEntities().remove(roleDao.getOne(roleId));
        try {
            userDao.saveAndFlush(userEntity);
        } catch (DataAccessException e) {
            throw new DaoException(String
                    .format("Deleting role with id %d to user with id %d failed", roleId, userId), e);
        }
    }

    public void validateForExistence(long id) {
        if (!userDao.existsById(id)) {
            throw new ValidationException(String.format("Not exist, id=%d", id));
        }
    }
}
