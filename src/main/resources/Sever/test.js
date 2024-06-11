import React, { useEffect, useState } from 'react';
import mqtt from 'mqtt';

const MQTTComponent = () => {
    const [mqttData, setMqttData] = useState('');

    useEffect(() => {
        const client = mqtt.connect('mqtt://192.168.1.194'); // Thay thế 'broker.example.com' bằng địa chỉ MQTT broker của bạn

        client.on('connect', () => {
            console.log('Connected to MQTT broker');
            client.subscribe('topic'); // Thay thế 'topic' bằng chủ đề MQTT bạn muốn nhận dữ liệu
        });

        client.on('message', (topic, message) => {
            setMqttData(message.toString());
        });

        // Hàm này sẽ được gọi khi component unmount để ngắt kết nối với MQTT broker
        return () => {
            client.end();
        };
    }, []); // Tham số trống đảm bảo rằng useEffect chỉ chạy một lần khi component được render

    return (
        <div>
            <h1>MQTT Data</h1>
            <div>Data from MQTT: {mqttData}</div>
        </div>
    );
};

export default MQTTComponent;
