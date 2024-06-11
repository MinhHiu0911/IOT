var a = Math.floor(Math.random()*100)
var b = Math.floor(Math.random()*100)
var c = Math.floor(Math.random()*100)
var d = Math.floor(Math.random()*100)
var f = Math.floor(Math.random()*100)


// Lấy tham chiếu đến thẻ canvas
const ctx = document.getElementById('myChart').getContext('2d');

// Khai báo dữ liệu cho biểu đồ
const data = {
    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    datasets: [
        {
            label: 'Nhiệt độ',
            data: [a, b, c, d, 80, f, 70], // Dữ liệu nhiệt độ
            borderColor: 'red',
            borderWidth: 1
        },
        {
            label: 'Độ ẩm',
            data: [c, d, a, b, 60, 10, f], // Dữ liệu ánh sáng
            borderColor: 'green',
            borderWidth: 1
        },
        {
            label: 'Ánh sáng',
            data: [b, 45, f, c, 10, a, d], // Dữ liệu độ ẩm
            borderColor: 'blue',
            borderWidth: 1
        }
    ]
};
    
return {
    temperature: temperature,
    humidity: humidity,
    light: light
};

// Sử dụng hàm để khởi tạo 3 giá trị
var values = generateRandomValues();
var temperature = values.temperature;
var humidity = values.humidity;
var light = values.light;

// In giá trị ra console để kiểm tra
console.log("Nhiệt độ: " + temperature);
console.log("Độ ẩm: " + humidity);
console.log("Ánh sáng: " + light);
// Khởi tạo biểu đồ Line Chart
const myChart = new Chart(ctx, {
    type: 'line',
    data: data,
    options: {}
});

