package ats.autommi;

import java.util.ArrayList;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import ats.autommi.R;
import ats.baseitems.BaseActivity;

class TestItemButton{
	View button;
	Class<?> cls;
	TestItemButton(View btn,Class<?> c){
		button = btn;
		cls =c;
	}
} 
public class LaunchView extends BaseActivity implements OnClickListener {

	ArrayList<TestItemButton> testlist;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("ATS Auto MMI Test Suite");
		setContentView(R.layout.activity_launch_view);
		
		testlist = new ArrayList<TestItemButton>();
		
		//system info
		addTestItem(R.id.ButtonVer,			ver.class);
		//addTestItem(R.id.ButtonBtFlag,		bitflg.class);
		addTestItem(R.id.ButtonSimCard,			sim.class);
		addTestItem(R.id.ButtonSDCard,			sd.class);
		addTestItem(R.id.ButtonListMethods,		service_class_info.class);	
		

		//audio
		addTestItem(R.id.ButtonSpeaker,		spk.class);
		addTestItem(R.id.ButtonReciver,		rec.class);
		addTestItem(R.id.ButtonMMicRec,		mic.class);
		addTestItem(R.id.ButtonHeadsetLoop,		headset.class);
//		/*
//		 * 副麦克依赖平台不能通用
//		 */
		//addTestItem(R.id.ButtonEMic1Rec,	emic1.class);
		//addTestItem(R.id.ButtonEMic2Rec,	emic2.class);
		
		//sensor
		addTestItem(R.id.ButtonLSensor,		lsensor.class);
		addTestItem(R.id.ButtonPSensor,		psensor.class);
		addTestItem(R.id.ButtonAcc,			acc.class);
		addTestItem(R.id.ButtonGyro,		gyro.class);
		addTestItem(R.id.ButtonMag,			mag.class);
		
		//radio
		addTestItem(R.id.ButtonWifi,		wifi.class);
		addTestItem(R.id.ButtonBlueTooth,	bt.class);
		addTestItem(R.id.ButtonGPS,			gps.class);
		//addTestItem(R.id.ButtonFM,			fm.class);
		
		//misc
		addTestItem(R.id.ButtonKeys,		key.class);
		addTestItem(R.id.ButtonVib,		vib.class);
		addTestItem(R.id.ButtonTP,		tp.class);
		addTestItem(R.id.ButtonBat,		bat.class);
		addTestItem(R.id.ButtonForeCamera,		fcam.class);
		addTestItem(R.id.ButtonBackCamera,		bcam.class);
		addTestItem(R.id.ButtonFlashLed,		flash.class);	
		addTestItem(R.id.ButtonLcdRed,			red.class);	
		addTestItem(R.id.ButtonLcdBlue,			blue.class);
		addTestItem(R.id.ButtonLcdGreen,		green.class);
		
		for(int i=0;i<testlist.size();i++){
			View v = testlist.get(i).button;
			if(v!=null)
				v.setOnClickListener(this);
		}
		
	}
	public void addTestItem(int id,Class<?> c)
	{
		View btn = findViewById(id);
		btn.setVisibility(View.VISIBLE);
		btn.setEnabled(true);
		testlist.add(new TestItemButton(btn,c));
	}
	
	@Override
	public void onClick(View v) {	
		Intent it = null;
		for(int i=0;i<testlist.size();i++){
			View btn = testlist.get(i).button;
			if(btn!=null && v.getId()==btn.getId()){
				it = new Intent(this, testlist.get(i).cls);
				if(it!=null) startActivity(it);
				break;
			}
		}
		
	}
}
