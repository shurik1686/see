package com.shurik16.SpringVaadin.service;

import com.shurik16.SpringVaadin.model.Tposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TpositRepository extends JpaRepository<Tposit, Long> {

}
