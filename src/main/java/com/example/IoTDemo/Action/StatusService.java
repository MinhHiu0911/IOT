package com.example.IoTDemo.Action;

import com.example.IoTDemo.Data.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    @Autowired
    private StatusRepository repo;

    public List<Status> statusList() {
        return (List<Status>) repo.findAll();
    }

    public void save(Status status) {
        repo.save(status);
    }

    public Page<Status> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 20);
        return repo.findAll(pageable);
    }

    public void deleteById(Integer id){
        repo.deleteById(id);
    }

    public Status findById(Integer id){
        return repo.findById(id).orElse(null);
    }

}
