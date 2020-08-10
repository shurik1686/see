package com.shurik16.SpringVaadin.service;

import com.shurik16.SpringVaadin.model.HistoryMan;
import com.shurik16.SpringVaadin.model.Tstanding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface HistoryManRepository extends JpaRepository<HistoryMan, Long> {

    List<HistoryMan> findByMankey(int key);

    List<HistoryMan> findByStatus(Tstanding key);

    void deleteAllByStatus(int key);

    void deleteAllByMankey(int key);

}
