package com.example.user.kt_mqtt_ser;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Switch simpleSwitch1;
    private Switch simpleSwitchcon1;
    private Switch simpleSwitchcon2;
    private Switch simpleSwitchcon3;
    private Switch simpleSwitchcon4;
    private Switch simpleSwitch2;
    private Switch simpleSwitch3;
    private Switch simpleSwitch4;
    private TextView TextView;
    private TextView Subtext;
    public Button button;
    public Button buttoff;

    static String MQTTHOST  = "tcp://211.38.86.93:1883";

    String chanel_1 = "$open-it/relay/order";
    String chanel_Status_1 = "$open-it/relay/status";


    MqttAndroidClient client;
    Handler setDelay;
    Runnable startDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Subtext = (TextView)findViewById(R.id.subtext);

        setDelay = new Handler();{

        }

        mqttconncetion();
        initView();
        client.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String payload = new String(message.getPayload());
                Subtext.setText(payload);

                if(payload.equals("ch1 on")) {
                    simpleSwitch1.setChecked(true);
                    simpleSwitchcon1.setChecked(true);

                }else if(payload.equals("ch1 off")){
                    simpleSwitch1.setChecked(false);
                    simpleSwitchcon1.setChecked(false);
                }
                else if(payload.equals("ch2 on")){
                    simpleSwitch2.setChecked(true);
                    simpleSwitchcon2.setChecked(true);
                }
                else if(payload.equals("ch2 off")){
                    simpleSwitch2.setChecked(false);
                    simpleSwitchcon2.setChecked(false);
                }
                else if(payload.equals("ch3 on")){
                    simpleSwitch3.setChecked(true);
                    simpleSwitchcon3.setChecked(true);
                }
                else if(payload.equals("ch3 off")){
                    simpleSwitch3.setChecked(false);
                    simpleSwitchcon3.setChecked(false);
                }
                else if(payload.equals("ch4 on")){
                    simpleSwitch4.setChecked(true);
                    simpleSwitchcon4.setChecked(true);
                }
                else if(payload.equals("ch4 off")){
                    simpleSwitch4.setChecked(false);
                    simpleSwitchcon4.setChecked(false);
                }

                else if(payload.equals("all on")){
                    simpleSwitch1.setChecked(true);
                    simpleSwitchcon1.setChecked(true);
                    simpleSwitch2.setChecked(true);
                    simpleSwitchcon2.setChecked(true);
                    simpleSwitch3.setChecked(true);
                    simpleSwitchcon3.setChecked(true);
                    simpleSwitch4.setChecked(true);
                    simpleSwitchcon4.setChecked(true);
                    simpleSwitch4.setChecked(true);
                    simpleSwitchcon4.setChecked(true);
                }
                else if(payload.equals("all off")){
                    simpleSwitch1.setChecked(false);
                    simpleSwitchcon1.setChecked(false);
                    simpleSwitch2.setChecked(false);
                    simpleSwitchcon2.setChecked(false);
                    simpleSwitch3.setChecked(false);
                    simpleSwitchcon3.setChecked(false);
                    simpleSwitch4.setChecked(false);
                    simpleSwitchcon4.setChecked(false);
                    simpleSwitch4.setChecked(false);
                    simpleSwitchcon4.setChecked(false);

                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }

        });

    }

    // connection to MQTT sercver
    public void mqttconncetion(){
        String clientId = MqttClient.generateClientId();
        client =  new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this,"Conneted", Toast.LENGTH_LONG).show();
                    sendMessagestatus();
                    setSubscrition();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this,"Disconnect", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    //set buttons
    private void initView() {

        simpleSwitch1 = (Switch) findViewById(R.id.simpleSwitch1);
        simpleSwitch2 = (Switch) findViewById(R.id.simpleSwitch2);
        simpleSwitch3 = (Switch) findViewById(R.id.simpleSwitch3);
        simpleSwitch4 = (Switch) findViewById(R.id.simpleSwitch4);


        simpleSwitchcon1 = (Switch) findViewById(R.id.switch1);
        simpleSwitchcon2 = (Switch) findViewById(R.id.switch2);
        simpleSwitchcon3 = (Switch) findViewById(R.id.switch3);
        simpleSwitchcon4 = (Switch) findViewById(R.id.switch4);

        TextView = (TextView) findViewById(R.id.Textview);


        // Toast.makeText(MainActivity.this, Condition, Toast.LENGTH_LONG).show();

        //set the switch to ON

        //attach a listener to check for changes in state
        simpleSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (simpleSwitch1.isChecked()) {
                    Chanel_1_ON();
                } else {

                    Chanel_1_Off();
                }
            }

        });

        simpleSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (simpleSwitch2.isChecked()) {
                    Chanel_2_ON();
                } else {
                    Chanel_2_Off();
                }
            }

        });

        simpleSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (simpleSwitch3.isChecked()) {
                    Chanel_3_ON();
                } else {
                    Chanel_3_Off();
                }
            }

        });

        simpleSwitch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (simpleSwitch4.isChecked()) {
                    Chanel_4_ON();
                } else {
                    Chanel_4_Off();
                }
            }

        });

    }

    // mesage is sending
    private void  sendMessagestatus(){
        String topic = chanel_1;
        ArrayList<String>  mStringList= new ArrayList<>();
        mStringList.add("ch1 status");
        mStringList.add("ch2 status");
        mStringList.add("ch3 status");
        mStringList.add("ch4 status");
        mStringList.add("ch5 status");
        for(String masseage: mStringList)

        try{
            client.publish(topic, masseage.getBytes() ,2,false);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    private void setSubscrition(){
        try{
            client.subscribe(chanel_Status_1, 2);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }
    public void Chanel_1_ON(){
        String topic = chanel_1;
        String message = "ch1 on";
        try {
            client.publish(topic, message.getBytes(),2,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void Chanel_1_Off(){
        String topic = chanel_1;
        String message = "ch1 off";
        try {
            client.publish(topic, message.getBytes(),2,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void Chanel_2_ON(){
        String topic = chanel_1;
        String message = "ch2 on";
        try {
            client.publish(topic, message.getBytes(),2,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void Chanel_2_Off(){
        String topic = chanel_1;
        String message = "ch2 off";
        try {
            client.publish(topic, message.getBytes(),2,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void Chanel_3_ON(){
        String topic = chanel_1;
        String message = "ch3 on";
        try {
            client.publish(topic, message.getBytes(),2,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void Chanel_3_Off(){
        String topic = chanel_1;
        String message = "ch3 off";
        try {
            client.publish(topic, message.getBytes(),2,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void Chanel_4_ON(){
        String topic = chanel_1;
        String message = "ch4 on";
        try {
            client.publish(topic, message.getBytes(),2,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void Chanel_4_Off(){
        String topic = chanel_1;
        String message = "ch4 off";
        try {
            client.publish(topic, message.getBytes(),2,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void RelayOff(){
        String topic = chanel_1;
        String message = "all off";
        try {
            client.publish(topic, message.getBytes(),2,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void Relay_on(){
        String topic = chanel_1;
        String message = "all on";
        try {
            client.publish(topic, message.getBytes(),2,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}