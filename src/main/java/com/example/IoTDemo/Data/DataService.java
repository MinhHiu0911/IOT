package com.example.IoTDemo.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DataService {
    @Autowired
    private DataRepository repo;

    public List<Data> getList() {
        return repo.findAll();
    }

    public Data save(Data data) {
        return repo.save(data);
    }

    public Data getLatestData() {
        Optional<Data> latestData = repo.findTopByOrderByTimestampDesc(); // Đây là một ví dụ, bạn cần tạo phương thức tương ứng trong DataRepository của bạn

        if (latestData.isPresent()) {
            return latestData.get();
        } else {
            return null;
        }
    }

    // Hàm để lấy 7 giá trị cuối cùng từ bảng dữ liệu
    public List<Data> getLatest7Data() {
        List<Data> latestData = repo.findTop7ByOrderByTimestampDesc();
        return latestData;
    }

    public Page<Data> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 20);
        return repo.findAll(pageable);
    }

    public List<Data> getAllSorted(String sortColumn, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortColumn);
        return repo.findAll(sort);
    }

    public List<Data> findByTimeStampBetween(Date startDate, Date endDate) {
        return repo.findByTimeStampBetween(startDate, endDate);
    }

    public void deleteById(Long id){
        repo.deleteById(id);
    }

    public Data findById(Long id){
        return repo.findById(id).orElse(null);
    }


//    public Page<Data> searchByTypeAndKeyword(String type, String keyword, int pageNo, int pageSize) {
//        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
//        return repo.findByTypeAndKeyword(type, keyword, pageable);
//    }
}
