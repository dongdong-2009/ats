package ats.baseitems;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.wifi.WifiManager;
import java.util.List;
import android.net.wifi.ScanResult;
import android.content.Intent;
import android.content.IntentFilter;

public class wifi extends TestItemActivity {
	
	BroadcastReceiver br;
	WifiManager wm;
	String info;
	int max = -1000;
	int min = 0;

	@Override
	public void onTestInit() 
	{
		this.setTitle("Wifi Test");
	    wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
	    if(wm.isWifiEnabled()==false){
	    	try{
	    		wm.setWifiEnabled(true);
	    	}catch(Exception e){}
	    }
	    
	    br = new BroadcastReceiver() {
	    	@Override
	        public void onReceive(Context context, Intent intent) {
	    		wifi thiz = (wifi)context;
	    		if(intent.getAction().equals("android.net.wifi.SCAN_RESULTS")) {
	    			thiz.analyzeScanResults((wifi)context);
	    		}
	        }

	    };
	    this.setInfo("Find Wifi AP ...\r\n");
	}
	@Override
	protected void onStart() 
	{
	    super.onStart();
	    registerReceiver(br, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
	    wm.startScan();
	}
	
	private void analyzeScanResults(wifi me) 
	{
	    List<ScanResult> rs = wm.getScanResults();
	    info = "";
	    for(ScanResult i : rs) {
	    	info += "BSSID : "+i.BSSID+"\r\nLEVEL : "+i.level+"\r\n";		    
//	        Log.d("WifiTest", i.BSSID);
//	        Log.d("WifiTest", "" + i.level);
//	        if(targetBssid.equalsIgnoreCase(i.BSSID)) {
//	            level = String.valueOf(i.level);
//	            return true;
//	        }	
		    if(max<i.level) 
				max = i.level;
			if(min>i.level) 
				min = i.level;
	    }
	 
	    this.addInfo(info);
	    
		me.app.RecordResult("wifi_ap_count", rs.size() + "");
		if(max != -1000)
			me.app.RecordResult("wifi_db_max", max + "");
		if(min != 0)
			me.app.RecordResult("wifi_db_min", min + "");
		
		me.app.SyncResult();
	}
	@Override
	public void onTestStop()
	{
	    super.onTestStop();
	    unregisterReceiver(br);
	}
}
