package com.shurik16.SpringVaadin.service;

import com.shurik16.SpringVaadin.model.Ttest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface TtestRepository extends JpaRepository<Ttest, Long> {
    List<Ttest> findByWorkingid(int key);

    void deleteAllByWorkingid(int key);
//findByCreatedateGreaterThanAndCreatedateLessThan
//    List<Ttest> findByData_testGreaterThan(Date data_start);
}
