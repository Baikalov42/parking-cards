package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.RoleDao;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class RoleService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private RoleDao roleDao;

    public List<Role> findAll(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<Role> result = roleDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("Result is empty, page number = %d, page size = %d", pageNumber, PAGE_SIZE));
        }
        return result;
    }
}
