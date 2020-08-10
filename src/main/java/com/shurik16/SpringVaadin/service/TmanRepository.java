package com.shurik16.SpringVaadin.service;


import com.shurik16.SpringVaadin.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TmanRepository extends JpaRepository<Tman, Long> {
    List<Tman> findAllBy(Pageable pageable);

    //List<Tman> findByFirstnameIsLike(String s);
    //LikeIgnoreCase(String nameFilter);
    List<Tman> findByFirstnameLikeIgnoreCase(String nameFilter);

    List<Tman> findByCompany(Tcompany comFilter);

    List<Tman> findByStanding(Tstanding staFilter);

    List<Tman> findByPosit(Tposit tposit);

    List<Tman> findByOtdel(Totdel totdel);

    List<Tman> findByFirstnameLikeIgnoreCaseAndCompany(String nameFilter, Tcompany comFilter);

    List<Tman> findByFirstnameLikeIgnoreCaseAndCompanyAndStanding(String nameFilter, Tcompany comFilter, Tstanding tstanding);

    List<Tman> findByFirstnameLikeIgnoreCaseAndStanding(String nameFilter, Tstanding staFilter);

    List<Tman> findByCompanyAndStanding(Tcompany tcompany, Tstanding tstanding);
    //List<Tman> findAllByIdExists(List<Long> longs);
}
