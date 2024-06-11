package com.example.IoTDemo.Lamp;

import com.example.IoTDemo.Action.Status;
import com.example.IoTDemo.Action.StatusService;
import com.example.IoTDemo.MqttService;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
class LampController {

    @Autowired
    private StatusService statusService;

    @Autowired
    private MqttService service;
    private Map<String, String> lampStatuses = new HashMap<>();

    // Mặc định khi trang web được tải, cả hai đèn đều tắt (status = off)
    @PostConstruct
    public void init() {
        lampStatuses.put("lamp1", "on");
        lampStatuses.put("lamp2", "off");
    }

    @GetMapping("/get-lamp-status")
    public Map<String, String> getLampStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", lampStatuses.get("lamp1"));
        // Để lấy trạng thái đèn 2, cần triển khai tương tự
        // response.put("lamp2Status", lampStatuses.get("lamp2"));
        return response;
    }

    @PostMapping("/update-lamp-status")
    public Map<String, String> updateLampStatus(@RequestBody Map<String, String> request) throws MqttException {
        String lampName = request.get("name");
        String status = request.get("status");
        lampStatuses.put(lampName, status);
        LampDTO lampDTO = new LampDTO(lampName, status);
        service.updateLampStatus(lampDTO);
        Status status1 = new Status();
        status1.setName(lampName);
        status1.setStatus(status);
        status1.setTime(String.valueOf(new Date()));
        statusService.save(status1);

        // Gửi yêu cầu đến Arduino để bật/tắt đèn (cần triển khai)

        Map<String, String> response = new HashMap<>();
        response.put("success", "true");
        return response;
    }

}

