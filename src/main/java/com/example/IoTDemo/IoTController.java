package com.example.IoTDemo;

import com.example.IoTDemo.Action.Status;
import com.example.IoTDemo.Action.StatusService;
import com.example.IoTDemo.Data.Data;
import com.example.IoTDemo.Data.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class IoTController {

    @Autowired
    private DataService dataService;

    @Autowired
    private StatusService statusService;


    @GetMapping("/home")
    public String show(){
        return "index";
    }

    @GetMapping("/history")
    public String showHistory(
            Model model,
            @RequestParam(name = "pageNo", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "sortColumn", required = false) String sortColumn,
            @RequestParam(name = "sortOrder", required = false) String sortOrder
    ) {
        // Xác định cách sắp xếp mặc định nếu không có tham số
        if (sortColumn == null || sortColumn.isEmpty()) {
            sortColumn = "id"; // Sắp xếp mặc định theo cột id
            sortOrder = "asc";
        }

        // Lấy tất cả dữ liệu từ cơ sở dữ liệu và sắp xếp nó theo yêu cầu
        List<Data> dataList = dataService.getAllSorted(sortColumn, sortOrder);

        // Tính toán trang hiện tại và chia dữ liệu thành từng trang
        int pageSize = 20;
        int totalItems = dataList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        List<Data> currentPageData = dataList.stream()
                .skip((page - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        model.addAttribute("totalPage", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("sortColumn", sortColumn);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("list", currentPageData);

        return "history";
    }

    @GetMapping("/action")
    public String showStatus(Model model, @RequestParam(name = "pageNo", defaultValue = "1") Integer page) {
        Page<Status> list = this.statusService.getAll(page);
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("list", list);
        return "action";
    }

    @GetMapping("/profile")
    public String show3(){
        return "profile";
    }


    @GetMapping("/get-latest-7-data")
    public ResponseEntity<List<Data>> getLatest7Data() {
        List<Data> latest7Data = dataService.getLatest7Data();
        return new ResponseEntity<>(latest7Data, HttpStatus.OK);
    }

    @GetMapping("/get-latest")
    public ResponseEntity<Data> getLatestData() {
        // Lấy giá trị cuối cùng từ cơ sở dữ liệu
        Data latestData = dataService.getLatestData(); // Thay đổi cách bạn truy vấn dữ liệu từ DataService
        System.out.println(latestData);
        if (latestData != null) {
            return new ResponseEntity<>(latestData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/deleteAction/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes ra){
        statusService.deleteById(id);
        return "redirect:/action";
    }

    @GetMapping("/updateAction/{id}")
    public String updateAction(@PathVariable("id") Integer id, Model model){
        Status status = statusService.findById(id);
        model.addAttribute("status", status);
        return "updateAction";
    }

    @PostMapping("/updateAction")
    public String updateAction1(@RequestParam("id") Integer id, @RequestParam("name") String name,
                                @RequestParam("status") String status, @RequestParam("time") String time){
        Status status1 = statusService.findById(id);
        status1.setName(name);
        status1.setStatus(status);
        status1.setTime(time);
        statusService.save(status1);
        return "redirect:/action";
    }

    @GetMapping("/delete/{id}")
    public String deleteAction(@PathVariable("id") Long id, RedirectAttributes ra){
        dataService.deleteById(id);
        return "redirect:/history";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model){
        Data data = dataService.findById(id);
        model.addAttribute("data", data);
        return "updateData";
    }

    @PostMapping("/update")
    public String updateData(@RequestParam("id") Long id, @RequestParam("temperature") String temperature,
                             @RequestParam("humidity") String humidity, @RequestParam("light") String light, @RequestParam("time") String time){
        Data data = dataService.findById(id);
        data.setTemperature(Float.parseFloat(temperature));
        data.setHumidity(Float.parseFloat(humidity));
        data.setLight(Float.parseFloat(light));
        data.setTimestamp(time);
        dataService.save(data);
        return "redirect:/history";
    }
}
