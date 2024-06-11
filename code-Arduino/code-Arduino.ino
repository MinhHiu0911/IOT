#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <DHT.h>

// WiFi
const char *ssid = "Lắc Đẹp Zai";     
const char *password = "hieudamthoi";

// MQTT Broker
const char *mqtt_broker = "192.168.81.33";
const char *mqtt_topic_led1 = "den";
const char *mqtt_topic_fan = "quat";
const char *mqtt_topic_temp = "sensor/temperature";
const char *mqtt_topic_hum = "sensor/humidity";
const char *mqtt_topic_light = "sensor/light"; 
const int mqtt_port = 1883;

// GPIO pins
const int LED_PIN1 = 4;  // GPIO 4 D2
const int LED_PIN2 = 14;  // GPIO 14 D5  
const int DHT_PIN = 5; // GPIO 5 D1
const int LED_PIN3 = 0; // GPIO 5 D3
const int LDR_PIN = A0;  // Chân analog A0 nối với chân của cảm biến ánh sáng

#define DHTTYPE DHT11

DHT dht(DHT_PIN, DHTTYPE);

WiFiClient espClient;
PubSubClient mqttClient(espClient);

bool ledState1 = false;
bool ledState2 = false;
bool ledState3 = false;

void callback(char *topic, byte *payload, unsigned int length) {
 
    String message;
    for (int i = 0; i < length; i++) {
        message += (char) payload[i]; 
    }
     
    if (strcmp(topic, mqtt_topic_led1) == 0) {
        Serial.print("Message arrived in topic: ");
        Serial.println(topic);
        Serial.print("Message: ");
        Serial.println(message);

        if (message == "on" && !ledState1) {
            digitalWrite(LED_PIN1, HIGH);  // Turn on the LED
            ledState1 = true;
            mqttClient.publish(mqtt_topic_led1, String(message).c_str());
        }
        if (message == "off" && ledState1) {
            digitalWrite(LED_PIN1, LOW); // Turn off the LED
            ledState1 = false;
            mqttClient.publish(mqtt_topic_led1, String(message).c_str());
        }

        Serial.println("-----------------------");
    }

        if (strcmp(topic, mqtt_topic_fan) == 0) {
        Serial.print("Message arrived in topic: ");
        Serial.println(topic);
        Serial.print("Message: ");
        Serial.println(message);

        if (message == "on" && !ledState2) {
            digitalWrite(LED_PIN2, HIGH);  // Turn on the LED
            ledState2 = true;
            // mqttClient.publish(mqtt_topic_led2, String(message).c_str());
        }
        if (message == "off" && ledState2) {
            digitalWrite(LED_PIN2, LOW); // Turn off the LED
            ledState2 = false;
            // mqttClient.publish(mqtt_topic_led2, String(message).c_str());
        }

        Serial.println("-----------------------");
    }

}

void setup() {
    Serial.begin(9600);
    delay(2000);

    // Setup LED pin
    pinMode(LED_PIN1, OUTPUT);
    pinMode(LED_PIN2, OUTPUT);
    pinMode(LED_PIN3, OUTPUT);
    digitalWrite(LED_PIN1, LOW);
    digitalWrite(LED_PIN2, LOW);
    digitalWrite(LED_PIN3, LOW);

    // Initialize DHT sensor
    dht.begin();

    // Connect to WiFi
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.println("Connecting to WiFi...");
    }
Serial.println("Connected to WiFi");

    // Connect to MQTT
    mqttClient.setServer(mqtt_broker, mqtt_port);
    mqttClient.setCallback(callback);
    while (!mqttClient.connected()) {
        if (mqttClient.connect("esp8266-client")) {
            Serial.println("Connected to MQTT broker");
            mqttClient.subscribe(mqtt_topic_led1); 
            mqttClient.subscribe(mqtt_topic_fan);
        } else {
            Serial.print("MQTT connection failed, rc=");
            Serial.print(mqttClient.state());
            Serial.println(" Retrying...");
            delay(2000);
        }
    }
}

void loop() {
    // Handle MQTT messages
    mqttClient.loop();

    // Read temperature and humidity
    float temp = dht.readTemperature();
    float hum = dht.readHumidity();


    Serial.println("Nhiệt độ");
    Serial.println(temp);
    Serial.println("Độ ẩm");
    Serial.println(hum);

    // Publish temperature and humidity to MQTT
    if (mqttClient.connected()) {
        mqttClient.publish(mqtt_topic_temp, String(temp).c_str());
        mqttClient.publish(mqtt_topic_hum, String(hum).c_str());
    }

    // Read light level from LDR sensor
    int lightLevel = analogRead(LDR_PIN);

    Serial.println("Ánh sáng");
    Serial.println(lightLevel);

    // Publish light level to MQTT
    if (mqttClient.connected()) {
      mqttClient.publish(mqtt_topic_light, String(lightLevel).c_str());
    }

    delay(2000); // Wait for a while before reading again
}