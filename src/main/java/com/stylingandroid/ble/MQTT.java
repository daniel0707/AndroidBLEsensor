package com.stylingandroid.ble;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sc on 2017/03/12.
 */

public class MQTT implements IoTCallbacks{
    private MqttAndroidClient mMQTTclient = null;
    private String connectionURI = "tcp://quickstart.messaging.internetofthings.ibmcloud.com:1883";
    private String eventTopic = "iot-2/evt/status/fmt/json";
    private Context context;

    public static final String TAG = "MQTT";

    public MQTT(Context context) {
        super();
        this.context = context;
    }

    public static IMqttActionListener cretateMQTTListener () {
        return new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "onSuccess");

                 /*   try {
                        mMQTTclient.subscribe("hoge/#", 0);
                        Log.d(TAG, "subscribe");
                    } catch (MqttException e) {
                        Log.d(TAG, e.toString());
                    }
                    */
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        };
    }

    public void createMQTTClient(String deviceId, IMqttActionListener listener)  {
        try {
            if (mMQTTclient != null) {
                mMQTTclient.disconnect();
                mMQTTclient.unregisterResources();
                Log.d(TAG, "disconnect");
            }
            String clientID= "d:quickstart:Android:"+deviceId;

            mMQTTclient = new MqttAndroidClient(context, connectionURI, clientID);
            mMQTTclient.setCallback(this);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName("hoge");
            char[] passwd = {'0','0','0','0'};
            options.setPassword(passwd);
            mMQTTclient.connect(options, context, listener);
        } catch (MqttException e){
            Log.d(TAG,e.toString());
        }
    }

    public IMqttDeliveryToken publishMQTT(String msg, IMqttActionListener listener) throws MqttException {
        if(mMQTTclient == null)
            return null;
        if(mMQTTclient.isConnected()) {
            DateFormat isoDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            String isoTimestamp = isoDateTimeFormat.format(new Date());
            if (!isoTimestamp.endsWith("Z")) {
                int pos = isoTimestamp.length() - 2;
                isoTimestamp = isoTimestamp.substring(0, pos) + ':' + isoTimestamp.substring(pos);
            }
            msg = msg+"\"timestamp\":\"" + isoTimestamp + "\" " + "} }";
            MqttMessage mqttMsg = new MqttMessage(msg.getBytes());
            mqttMsg.setRetained(false);
            mqttMsg.setQos(0);
           try {
                return mMQTTclient.publish(eventTopic, mqttMsg, context, listener);
            } catch (MqttPersistenceException e) {
                Log.e(TAG, "MqttPersistenceException caught while attempting to publish a message", e.getCause());
                throw e;
            } catch (MqttException e) {
                Log.e(TAG, "MqttException caught while attempting to publish a message", e.getCause());
                throw e;
            }
        }
        return null;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        Log.e(TAG, ".connectionLost() entered");

        if (throwable != null) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        Log.d(TAG, ".messageArrived() entered");

        String payload = new String(mqttMessage.getPayload());
        Log.d(TAG, ".messageArrived - Message received on topic " + topic
                + ": message is " + payload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Log.d(TAG, ".deliveryComplete() entered");
    }

}
