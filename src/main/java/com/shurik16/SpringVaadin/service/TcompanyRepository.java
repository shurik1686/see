package com.shurik16.SpringVaadin.service;

import com.shurik16.SpringVaadin.model.Tcompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcompanyRepository extends JpaRepository<Tcompany, Long> {
}
