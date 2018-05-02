package ats.baseitems;

import android.hardware.Sensor;

public class psensor extends lsensor {
	@Override
	public void onTestInit() {
		this.setTitle("Proximity Sensor Test");
		this.setInfo("");
		SensorConfig(Sensor.TYPE_PROXIMITY);
	}

	@Override
	public void UpdateResult() {
		// TODO Auto-generated method stub
		String name = "psensor_";
		
		app.RecordResult(name+"min", min + "");
		app.RecordResult(name+"max", max + "");
		
		app.SyncResult();
		
		addInfo(name+"min : " + min + "\n");
		addInfo(name+"max : " + max + "\n");
	}
}
