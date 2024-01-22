package com.mobitel.storageMigration.repository;

import com.mobitel.storageMigration.entity.PrimaryKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PrimaryKeyDataRepository extends JpaRepository<PrimaryKeyEntity, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO inventory.primary_key(box_id, new_box_id, division, status) " +
            "VALUES(:box_id, :new_box_id, :division, :status) " , nativeQuery = true)
    int addPrimaryKeyDetails(@Param("box_id") String boxId, @Param("new_box_id") String newBoxId,
                             @Param("division") String division, @Param("status") String status);
}