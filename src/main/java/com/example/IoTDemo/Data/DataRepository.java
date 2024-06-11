package com.example.IoTDemo.Data;

import com.example.IoTDemo.Data.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DataRepository extends JpaRepository<Data, Long> {


    Optional<Data> findTopByOrderByTimestampDesc();

    List<Data> findTop7ByOrderByTimestampDesc();

    Page<Data> findAll(Pageable pageable);

    @Query("SELECT d FROM Data d WHERE d.timestamp BETWEEN :startDate AND :endDate")
    List<Data> findByTimeStampBetween(Date startDate, Date endDate);

}
