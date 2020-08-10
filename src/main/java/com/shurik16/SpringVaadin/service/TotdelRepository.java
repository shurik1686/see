package com.shurik16.SpringVaadin.service;

import com.shurik16.SpringVaadin.model.Totdel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotdelRepository extends JpaRepository<Totdel, Long> {
}
