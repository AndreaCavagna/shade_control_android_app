package com.example.shade_control_android_app;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    MqttAndroidClient client;
    TextView subText;
    boolean pressed_button = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.setContentView(R.layout.activity_main);
        subText = (TextView)findViewById(R.id.mqtt_reply);

        Button btn_up = (Button) findViewById(R.id.go_up);
        Button btn_down = (Button) findViewById(R.id.go_down);
        Button btn_full_up = (Button) findViewById(R.id.full_up);
        Button btn_stop_movement= (Button) findViewById(R.id.stop_movement);

        Button btn_full_down = (Button) findViewById(R.id.full_down);
        TextView tv_mqtt_reply = (TextView) findViewById(R.id.mqtt_reply);
        TextView tv_mqtt_connection_gate = (TextView) findViewById(R.id.check_connection_gate);
        TextView check_connection_gate_posix = (TextView) findViewById(R.id.check_connection_gate_posix);

        String clientId = MqttClient.generateClientId();


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String mqtt_server = preferences.getString("mqtt_server", "");
        String mqtt_port = preferences.getString("mqtt_port", "");
        String mqtt_protocol = preferences.getString("mqtt_protocol", "");



        if(mqtt_server.equalsIgnoreCase("") || mqtt_port.equalsIgnoreCase("")|| mqtt_protocol.equalsIgnoreCase(""))
        {
            Intent myIntent = new Intent(MainActivity.this, Insert_mqtt_credentials.class);
            MainActivity.this.startActivity(myIntent);
        }



        client = new MqttAndroidClient(this.getApplicationContext(), mqtt_protocol + "://" + mqtt_server + ":" + mqtt_port, clientId);


        // Register the onClick listener with the implementation above



        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String payload_str = new String(message.getPayload());


                int base_color = Color.rgb(202, 119, 104);
                btn_up.setBackgroundColor(base_color);
                btn_down.setBackgroundColor(base_color);

                if (topic.equals("homeAssistant/casaBonate/cover/cancello/state")){
                    check_connection_gate_posix.setText(payload_str.toUpperCase());
                    check_connection_gate_posix.setTextColor(Color.parseColor("#CCCCCC"));


                    if (pressed_button) {
                        subText.setText(payload_str);

                        btn_up.setTextColor(Color.WHITE);
                        btn_down.setTextColor(Color.WHITE);
                        btn_full_up.setTextColor(Color.WHITE);
                        btn_full_down.setTextColor(Color.WHITE);
                        btn_stop_movement.setTextColor(Color.WHITE);

                        tv_mqtt_reply.setTextColor(Color.WHITE);

                        int opening_color = Color.rgb(0, 255, 0);
                        int open_color = Color.rgb(0, 255, 0);
                        int closing_color = Color.rgb(255, 0, 0);
                        int closed_color = Color.rgb(223, 0, 0);

                        if (payload_str.equals("opening")) {
                            btn_up.setBackgroundColor(opening_color);
                            btn_down.setBackgroundColor(opening_color);
                            btn_full_up.setTextColor(opening_color);
                            btn_full_down.setTextColor(opening_color);
                            btn_stop_movement.setTextColor(opening_color);

                            tv_mqtt_reply.setBackgroundColor(opening_color);
                            tv_mqtt_reply.setText("Sto Aprendo!");

                            Animation mAnimation = new AlphaAnimation(1, 0);
                            mAnimation.setDuration(1000);
                            mAnimation.setInterpolator(new LinearInterpolator());
                            mAnimation.setRepeatCount(20);
                            mAnimation.setRepeatMode(Animation.REVERSE);
                            tv_mqtt_reply.startAnimation(mAnimation);


                        } else if (payload_str.equals("open")) {

                            btn_up.setBackgroundColor(open_color);
                            btn_down.setBackgroundColor(open_color);
                            btn_full_up.setTextColor(opening_color);
                            btn_full_down.setTextColor(opening_color);
                            btn_stop_movement.setTextColor(opening_color);
                            tv_mqtt_reply.setBackgroundColor(open_color);
                            tv_mqtt_reply.setText("Aperto!");

                        }else if (payload_str.equals("closing")) {
                            btn_up.setBackgroundColor(closing_color);
                            btn_down.setBackgroundColor(closing_color);
                            btn_full_up.setTextColor(opening_color);
                            btn_full_down.setTextColor(opening_color);
                            btn_stop_movement.setTextColor(opening_color);
                            tv_mqtt_reply.setBackgroundColor(closing_color);
                            tv_mqtt_reply.setText("Sto Chiudendo!");

                            Animation mAnimation = new AlphaAnimation(1, 0);
                            mAnimation.setDuration(1000);
                            mAnimation.setInterpolator(new LinearInterpolator());
                            mAnimation.setRepeatCount(20);
                            mAnimation.setRepeatMode(Animation.REVERSE);
                            tv_mqtt_reply.startAnimation(mAnimation);

                        }else if (payload_str.equals("closed")) {
                            btn_up.setBackgroundColor(closed_color);
                            btn_down.setBackgroundColor(closed_color);
                            btn_full_up.setTextColor(opening_color);
                            btn_full_down.setTextColor(opening_color);
                            btn_stop_movement.setTextColor(opening_color);
                            tv_mqtt_reply.setBackgroundColor(closed_color);
                            tv_mqtt_reply.setText("Chiuso!");

                        }else {
                            btn_up.setBackgroundColor(Color.RED);
                            btn_down.setBackgroundColor(Color.RED);
                            btn_full_up.setTextColor(Color.RED);
                            btn_full_down.setTextColor(Color.RED);
                            btn_stop_movement.setTextColor(Color.RED);
                            tv_mqtt_reply.setBackgroundColor(Color.RED);
                            tv_mqtt_reply.setText("Errore");
                        }
                    }

                }

                if (topic.equals("homeAssistant/casaBonate/cover/cancello/confirmOnline")) {
                    if (payload_str.equals("Yep!")) {

                        tv_mqtt_connection_gate.setTextColor(Color.GREEN);
                        tv_mqtt_connection_gate.setText("Cancello connessso!");

                        btn_up.setEnabled(true);
                        btn_down.setEnabled(true);
                        btn_full_up.setEnabled(true);
                        btn_full_down.setEnabled(true);
                        btn_stop_movement.setEnabled(true);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        View view = findViewById(android.R.id.content).getRootView();
        conn(view);




    }




    public void full_down(View v){

        String topic = "ambient/bonate/esterno_camera_andrea/dht/set";
        String message = "DF";
        pressed_button = true;
        try {
            client.publish(topic, message.getBytes(),0,false);
            Toast.makeText(this,"Aperto",Toast.LENGTH_SHORT).show();
        } catch ( MqttException e) {
            e.printStackTrace();
        }
    }


    public void full_up(View v){

        String topic = "ambient/bonate/esterno_camera_andrea/dht/set";
        String message = "UF";
        pressed_button = true;
        try {
            client.publish(topic, message.getBytes(),0,false);
            Toast.makeText(this,"Aperto",Toast.LENGTH_SHORT).show();
        } catch ( MqttException e) {
            e.printStackTrace();
        }
    }


    public void go_down(View v){

        String topic = "ambient/bonate/esterno_camera_andrea/dht/set";
        String message = "D";
        pressed_button = true;
        try {
            client.publish(topic, message.getBytes(),0,false);
            Toast.makeText(this,"Aperto",Toast.LENGTH_SHORT).show();
        } catch ( MqttException e) {
            e.printStackTrace();
        }
    }


    public void go_up(View v){

        String topic = "ambient/bonate/esterno_camera_andrea/dht/set";
        String message = "U";
        pressed_button = true;
        try {
            client.publish(topic, message.getBytes(),0,false);
            Toast.makeText(this,"Aperto",Toast.LENGTH_SHORT).show();
        } catch ( MqttException e) {
            e.printStackTrace();
        }
    }


    public void stop_motion(View v){

        String topic = "ambient/bonate/esterno_camera_andrea/dht/set";
        String message = "S";
        pressed_button = true;
        try {
            client.publish(topic, message.getBytes(),0,false);
            Toast.makeText(this,"Fermato",Toast.LENGTH_SHORT).show();
        } catch ( MqttException e) {
            e.printStackTrace();
        }
    }



    private void setSubscription(){

        try{

            client.subscribe("homeAssistant/casaBonate/cover/cancello/state",0);
            client.subscribe("homeAssistant/casaBonate/cover/cancello/confirmOnline",0);


        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void conn(View v){

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this,"connected!!",Toast.LENGTH_LONG).show();
                    setSubscription();
                    TextView tv_mqtt_connection = (TextView)findViewById(R.id.check_connection);
                    tv_mqtt_connection.setTextColor(Color.GREEN);
                    tv_mqtt_connection.setText("Server connessso!");

                    try {
                        client.publish("homeAssistant/casaBonate/cover/cancello/confirmOnline", "uThere?".getBytes(),0,false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this,"connection failed!!",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void disconn(View v){

        try {
            IMqttToken token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this,"Disconnected!!",Toast.LENGTH_LONG).show();


                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this,"Could not diconnect!!",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void openOptions(View v){
        Intent myIntent = new Intent(MainActivity.this, Insert_mqtt_credentials.class);
        //myIntent.putExtra("key", value); //Optional parameters
        MainActivity.this.startActivity(myIntent);

    }



}
