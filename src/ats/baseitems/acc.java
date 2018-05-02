package ats.baseitems;

import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.hardware.Sensor;

public class acc extends ats.baseitems.SensorTest
{
	String name="";
	@SuppressLint("NewApi")
	@Override
	public void onTestInit()
	{
		if(name.isEmpty())
			setName("acc");
		this.setTitle("Accelerometer Sensor Test");
		
		SensorConfig(Sensor.TYPE_ACCELEROMETER);
	}
	public void setName(String string) {
		// TODO Auto-generated method stub
		name=string;
	}
	
	float x,y,z,dx,dy,dz;
	
	ArrayList<float[]> list = new ArrayList<float[]>();
	public void DellResult(float[] values) {
		// TODO Auto-generated method stub
		//200msµÄ»ã±¨ÆµÂÊ.
		final int count = 5;
		list.add(values.clone());
		if(list.size()>=count){
			float[] xs=new float[count];
			float[] ys=new float[count];
			float[] zs=new float[count];
			for(int i=0;i<count;i++){
				xs[i] = list.get(i)[0];
				ys[i] = list.get(i)[1];
				zs[i] = list.get(i)[2];
			}
			Arrays.sort(xs);
			Arrays.sort(ys);
			Arrays.sort(zs);
			
			x = xs[count/2];
			y = ys[count/2];
			z = zs[count/2];
			dx = xs[count-1]-xs[0];
			dy = ys[count-1]-ys[0];
			dz = zs[count-1]-zs[0];
			dx = dx>=0?dx:-dx;
			dy = dy>=0?dy:-dy;
			dz = dz>=0?dz:-dz;
			
			UpdataResults();
			
			list.clear();
			
		}
	}
	private void UpdataResults() {
		// TODO Auto-generated method stub
		
		String name = this.name + "_";

		app.RecordResult(name+"x", x + "");
		app.RecordResult(name+"y", y + "");
		app.RecordResult(name+"z", z + "");
		app.RecordResult(name+"dx", dx + "");
		app.RecordResult(name+"dy", dy + "");
		app.RecordResult(name+"dz", dz + "");

		setInfo("");
		addInfo(name+"x : " + x + "\n");
		addInfo(name+"y : " + y + "\n");
		addInfo(name+"z : " + z + "\n");
		addInfo(name+"dx : " + dx + "\n");
		addInfo(name+"dy : " + dy + "\n");
		addInfo(name+"dz : " + dz + "\n");

		app.SyncResult();

	}
}
