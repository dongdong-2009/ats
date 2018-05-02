package ats.baseitems;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;

public class headset extends mic
{
	HeadsetPlugReceiver headsetPlugReceiver;
	@Override
	public void onTestInit()
	{
		super.onTestInit();
		if(HeadsetIsPlug()){
			setInfoPlugged();
		}else{
			setInfoUnplugged();
		}
		setTitle("Headset Loopback Test");
		registerHeadsetPlugReceiver();
	}

	private void setInfoUnplugged() {
		// TODO Auto-generated method stub
		this.tv_info.setTextColor(Color.RED);
		setInfo("***Please Plugging Headset!");
	}

	private void setInfoPlugged() {
		// TODO Auto-generated method stub
		this.tv_info.setTextColor(Color.BLACK);
		setInfo("Headset has plugged,Please input sound to the mic of headset,then listen from headset!");
	}

	@SuppressWarnings("deprecation")
	private boolean HeadsetIsPlug() {
		// TODO Auto-generated method stub
		return am.isWiredHeadsetOn();
	}

	@Override
	public void onTestStop() 
	{
		super.onTestStop();
	}

	class HeadsetPlugReceiver extends BroadcastReceiver {
		//private static final String TAG = "HeadsetPlugReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			//headset me = (headset) context;
			if (intent.hasExtra("state")) {
				if (intent.getIntExtra("state", 0) == 0) {
					setInfoUnplugged();
				} else if (intent.getIntExtra("state", 0) == 1) {
					setInfoPlugged();
				}
			}
		}
	}

	private void registerHeadsetPlugReceiver() {
		headsetPlugReceiver = new HeadsetPlugReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.HEADSET_PLUG");
		registerReceiver(headsetPlugReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(headsetPlugReceiver);
		super.onDestroy();
	}
}