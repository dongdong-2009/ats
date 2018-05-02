package ats.baseitems;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorTest extends TestItemActivity implements SensorEventListener 
{
	private Sensor sensor;
    private SensorManager sm;
    private int type;		//����,ʱ�侫ȷ��
	@Override
	public void onTestInit()
	{
		this.setTitle("Light Sensor Test");
		this.setInfo("");   
		String[] res_labels = new String[1];
		res_labels[0] = new String("Light Value");
		SensorConfig(Sensor.TYPE_LIGHT);
	}
	public void SensorConfig(int type)
	{
		this.type = type;
	}
	@Override
	public void onStart()
	{
		super.onStart();
		sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		sensor = sm.getDefaultSensor(type);
		sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	@Override
	public void onTestStop() 
	{
		super.onTestStop();
		sm.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		DellResult(event.values);
	}
	public void DellResult(float[] values) {
		// TODO Auto-generated method stub
		//200ms�Ļ㱨Ƶ��.
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}