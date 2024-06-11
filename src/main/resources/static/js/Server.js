const mqtt = require('mqtt');

// Thông tin kết nối tới MQTT broker
const brokerUrl = 'tcp://192.168.1.194:1883'; // Thay đổi thành URL của MQTT broker bạn đang sử dụng
const options = {
    clientId: 'my-nodejs-client', // Đặt tên client của bạn
};

// Kết nối tới MQTT broker
const client = mqtt.connect(brokerUrl, options);

client.on('connect', () => {
    console.log('Đã kết nối tới MQTT broker');

    // Subscribe tới một topic
    client.subscribe('sensor/temperature');

    // Gửi dữ liệu lên một topic
    client.publish('my/topic', 'Hello from Node.js');

    // Nhận dữ liệu từ MQTT broker
    client.on('message', (topic, message) => {
        console.log(`Nhận dữ liệu từ ${topic}: ${message.toString()}`);
        displayTemperature(message.toString());
    });
});

client.on('error', (error) => {
    console.error(`Lỗi kết nối: ${error}`);
});

// Ngắt kết nối khi cần thiết
// client.end();

// Hàm hiển thị dữ liệu lên trang HTML
function displayTemperature(temperature) {
    // Thực hiện hiển thị dữ liệu lên trang HTML tại đây
    document.getElementById('temperature').textContent = `Temperature: ${temperature} °C`;
}