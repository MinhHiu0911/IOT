import paho.mqtt.client as mqtt
import time


broker_address = "10.21.159.72" 
port = 1883  
topic = "dulieu"  

client = mqtt.Client()


client.connect(broker_address, port)

try:
    while True:
        
        message = "Hello, MQTT!"
        
     
        client.publish(topic, message)
        
        print(f"Published: {message}")
        
        
        time.sleep(2)

except KeyboardInterrupt:

    client.disconnect()
