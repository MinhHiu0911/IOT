package com.example.IoTDemo.Action;

import com.example.IoTDemo.Action.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {

    Page<Status> findAll(Pageable pageable);
}
