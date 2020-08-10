package com.shurik16.SpringVaadin.service;

import com.shurik16.SpringVaadin.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

//JpaRepository<User,Long> {
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByUsername(String username);
}