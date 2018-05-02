package ats.baseitems;

import android.hardware.Sensor;

public class mag extends acc
{
	@Override
	public void onTestInit()
	{
		this.setName("mag");
		this.setTitle("Magnetic Sensor Test");
		SensorConfig(Sensor.TYPE_MAGNETIC_FIELD);
	}
}
