package com.example.IoTDemo;

import com.example.IoTDemo.Action.Status;
import com.example.IoTDemo.Action.StatusService;
import com.example.IoTDemo.Data.Data;
import com.example.IoTDemo.Data.DataService;
import com.example.IoTDemo.Lamp.LampDTO;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MqttService {
    @Autowired
    private DataService service;

    @Autowired
    private StatusService statusService;

    private final String mqttServer = "tcp://192.168.1.194:1883";
    private final String mqttClientId = "192.168.1.194";
    private final String[] mqttTopics = {"sensor/temperature", "sensor/humidity", "sensor/light", "led1", "led2", "led3", "sensor/bui"};
    private MqttClient mqttClient;

    private Map<String, String> currentStatus = new HashMap<>();

    public MqttService() {
        try {
            mqttClient = new MqttClient(mqttServer, mqttClientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false); // Để duy trì kết nối khi không có session
            mqttClient.connect(connOpts);

            for (String topic : mqttTopics) {
                mqttClient.subscribe(topic);
            }

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    System.out.println("Connection to MQTT Broker lost!");
                }

                private float temperature = 0;
                private float humidity = 0;
                private float light = 0;

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    String message = new String(mqttMessage.getPayload());
                    if (topic.equals("sensor/temperature")) {
                        temperature = Float.parseFloat(message);
                    } else if (topic.equals("sensor/humidity")) {
                        humidity = Float.parseFloat(message);
                    } else if (topic.equals("sensor/light")) {
                        light = Float.parseFloat(message);
                    }

                    // Kiểm tra xem tất cả ba giá trị đã được nhận
                    if (temperature != 0 && humidity != 0 && light != 0) {
                        Data data = new Data();
                        data.setTemperature(Float.valueOf(temperature));
                        data.setHumidity(Float.valueOf(humidity));
                        data.setLight(Float.valueOf(light));
                        // Định dạng thời gian theo "yyyy/MM/dd HH:mm:ss"
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String formattedDate = sdf.format(new Date());

                        data.setTimestamp(formattedDate);
                        service.save(data);


                        // Đặt lại các giá trị về 0 để chờ nhận giá trị mới
                        temperature = 0;
                        humidity = 0;
                        light = 0;
                    }

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    // This method is not used in this example
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Để dừng kết nối MQTT khi cần thiết
    public void stop() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // gửi trạng thái đèn sang Arduino
    public void updateLampStatus(LampDTO lampDTO) throws MqttException {
        String lamp = lampDTO.getLamp();
        String status = lampDTO.getStatus();
        MqttMessage message = new MqttMessage(status.getBytes());
        mqttClient.publish(lamp, message);
    }

}

