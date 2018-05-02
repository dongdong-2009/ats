package ats.baseitems;

import android.hardware.Sensor;

public class gyro extends acc
{
	@Override
	public void onTestInit()
	{
		this.setName("gyro");
		this.setTitle("Gyroscope Sensor Test");
		SensorConfig(Sensor.TYPE_GYROSCOPE);
	}

}
