package com.stylingandroid.ble;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayFragment extends Fragment {
	public static DisplayFragment newInstance() {
		return new DisplayFragment();
	}

	private TextView mDeviceId = null;
	private TextView mTemperature = null;
	private TextView mHumidity = null;
	private TextView mLuxometer = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_display, container, false);
		if (v != null) {
			mDeviceId = (TextView) v.findViewById(R.id.deviceId);
			mTemperature = (TextView) v.findViewById(R.id.temperature);
			mHumidity = (TextView) v.findViewById(R.id.humidity);
			mLuxometer = (TextView) v.findViewById(R.id.luxometer);

			setDeviceId(BleActivity.deviceId);
		}
		return v;
	}

	public void setData(float temperature, float humidity) {
		if (mTemperature != null) {
			mTemperature.setText(getString(R.string.temp_format, temperature));
		}
		if (mHumidity != null) {
			mHumidity.setText(getString(R.string.humidity_format, humidity));
		}
	}

	public void setLuxometerData(float luxometer) {
		if (mLuxometer != null) {
			mLuxometer.setText(getString(R.string.luxometer_format, luxometer));
		}
	}

	public void setDeviceId(String deviceId) {
		if (mDeviceId != null) {
			mDeviceId.setText(deviceId);
		}
	}
}
