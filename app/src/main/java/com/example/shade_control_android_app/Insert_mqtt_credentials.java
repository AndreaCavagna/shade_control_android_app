package com.example.shade_control_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

public class Insert_mqtt_credentials extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_mqtt_credentials);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String mqtt_server = preferences.getString("mqtt_server", "MQTT Server");
        String mqtt_port = preferences.getString("mqtt_port", "MQTT Port");
        String mqtt_protocol = preferences.getString("mqtt_protocol", "MQTT Protocol");

        EditText te_mqtt_server = (EditText)findViewById(R.id.editText_mqtt_server);
        EditText te_mqtt_port   = (EditText)findViewById(R.id.editText_mqttPort);
        EditText te_mqtt_protocol   = (EditText)findViewById(R.id.editText_mqttProtocol);


        te_mqtt_server.setText(mqtt_server);
        te_mqtt_port.setText(mqtt_port);
        te_mqtt_protocol.setText(mqtt_protocol);

    }



    public void saveOptions(View v){

        EditText mqtt_server = (EditText)findViewById(R.id.editText_mqtt_server);
        EditText mqtt_port   = (EditText)findViewById(R.id.editText_mqttPort);
        EditText mqtt_protocol   = (EditText)findViewById(R.id.editText_mqttProtocol);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mqtt_server",mqtt_server.getText().toString());
        editor.apply();

        editor.putString("mqtt_port",mqtt_port.getText().toString());
        editor.apply();

        editor.putString("mqtt_protocol",mqtt_protocol.getText().toString());
        editor.apply();


        Intent myIntent = new Intent(Insert_mqtt_credentials.this, MainActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        Insert_mqtt_credentials.this.startActivity(myIntent);

    }
}