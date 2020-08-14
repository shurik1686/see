package com.shurik16.SpringVaadin.service;

import com.shurik16.SpringVaadin.model.Tfoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TfotoRepository extends JpaRepository<Tfoto,Long> {
    Tfoto findById(int id);
}
