package com.mobitel.storageMigration.repository;

import com.mobitel.storageMigration.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserDataRepository extends JpaRepository<UserEntity, Integer> {


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO inventory.business_center(division, received_date, expire_date, box_range) "
            + "VALUES(:division, :received_date, :expire_date, :box_range) ", nativeQuery = true)
    public int addEmpDetails(@Param("division") String division,
                             @Param("received_date") String receivedDate,
                             @Param("expire_date") String expireDate,
                             @Param("box_range") String boxRange);

    @Query(value = "SELECT COUNT(*) FROM inventory.business_center WHERE box_range = :box_range OR box_range IN (CONCAT(:box_range,'/IS'), CONCAT(:box_range,'/IS-1'), CONCAT(:box_range,'/IS-2'), CONCAT(:box_range,'/IS-3'), CONCAT(:box_range,'/IS-4'))", nativeQuery = true)
    int checkData(@Param("box_range") String box_range);


}
