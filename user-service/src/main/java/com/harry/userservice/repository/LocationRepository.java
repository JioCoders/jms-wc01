package com.harry.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.harry.userservice.entity.Location;

import jakarta.transaction.Transactional;

import static com.harry.userservice.utils.DbConstant.TABLE_LOCATION;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query(value = "select * from " + TABLE_LOCATION + " where is_active=1", nativeQuery = true)
    List<Location> findAllActive(); 

    @Query(value = "select * from " + TABLE_LOCATION + " where is_active=1 and id=?1", nativeQuery = true)
    Optional<Location> findActiveByLocationId(Integer locationId);

    @Modifying
    @Transactional
    @Query(value = "update " + TABLE_LOCATION + " u set u.is_active=true where u.id=?1", nativeQuery = true)
    int activeLocation(Integer locationId);
    @Modifying
    @Transactional
    @Query(value = "update " + TABLE_LOCATION + " u set u.is_active=false where u.id=?1", nativeQuery = true)
    int deactiveLocation(Integer locationId);

}
