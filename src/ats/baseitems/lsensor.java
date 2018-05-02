package ats.baseitems;

import android.hardware.Sensor;

public class lsensor extends ats.baseitems.SensorTest {
	boolean is_first_in = true;

	@Override
	public void onTestInit() {
		this.setTitle("Light Sensor Test");
		this.setInfo("");
		SensorConfig(Sensor.TYPE_LIGHT);
		is_first_in = true;
	}

	float min = 0.f, max = 0.f;

	public void DellResult(float[] values) {

		float n = values[0];

		setInfo("Current Value : " + n + "\n");

		if (is_first_in) {
			is_first_in = false;
			min = max = n;
			return;
		}

		if (n < min)
			min = n;
		if (n > max)
			max = n;

		UpdateResult();
	}

	public void UpdateResult() 
	{
		// TODO Auto-generated method stub
		String name = "lsensor_";

		app.RecordResult(name + "min", min + "");
		app.RecordResult(name + "max", max + "");

		app.SyncResult();

		addInfo(name + "min : " + min + "\n");
		addInfo(name + "max : " + max + "\n");
	}
}
