package ats.baseitems;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;

public class bt extends TestItemActivity {
	
	BroadcastReceiver br_finish,br_found;
	BluetoothAdapter adapter;
	int count = 0;
	short max = -1000;
	short min = 0;

	@Override
	public void onTestInit() 
	{
		this.setTitle("Bluetooth Test");
	    
		//set infomation
	    this.setInfo("Find Bluetooth Devices ...\r\n");
	    
		adapter = BluetoothAdapter.getDefaultAdapter();
		//enable bluetooth adapter
		if(adapter.isEnabled()==false){
			try{
				adapter.enable();
			}catch(Exception e){};
		};
		//broadcasts
		//adapter = (BluetoothAdapter) getSystemService(this.BLUETOOTH_SERVICE);
	    br_finish = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				bt me = (bt) context;
				me.addInfo("Find Complete.");
			}
		};
	    br_found = new BroadcastReceiver(){
	    	@Override
	        public void onReceive(Context context, Intent intent) {
	    		bt me = (bt)context;
	    		BluetoothDevice dev = (BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
	    		String addr = dev.getAddress();
	    		short rssi = intent.getExtras().getShort("android.bluetooth.device.extra.RSSI");	
	    		me.addInfo("ADDR : "+ addr +"\r\nRSSI : "+rssi+"\r\n");
	    		
	    		count ++;
	    		if(max<rssi) 
	    			max = rssi;
	    		if(min>rssi) 
	    			min = rssi;
	    		
	    		me.app.RecordResult("bt_dev_count", count + "");
	    		if(max != -1000)
	    			me.app.RecordResult("bt_db_max", max + "");
	    		if(min != 0)
	    			me.app.RecordResult("bt_db_min", min + "");
	    		
	    		me.app.SyncResult();
	    		
	        }

	    };


	}
	
	protected void onStart() 
	{
	    super.onStart();
	    registerReceiver(br_finish, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
	    registerReceiver(br_found, new IntentFilter("android.bluetooth.device.action.FOUND"));
	    adapter.startDiscovery();
	}
	
//	private void analyzeScanResults() 
//	{
//	    Set<BluetoothDevice> set = adapter.getBondedDevices();
//	    info = "";
//	    for(BluetoothDevice i : set) {
//	    	info += "ADDR : "+i.getAddress()+"\r\nSTATE : "+i.getBondState()+"\r\n";		    
////	        Log.d("WifiTest", i.BSSID);
////	        Log.d("WifiTest", "" + i.level);
////	        if(targetBssid.equalsIgnoreCase(i.BSSID)) {
////	            level = String.valueOf(i.level);
////	            return true;
////	        }
//	    }
//	    this.setInfo(info);
//	}
	@Override
	public void onTestStop()
	{
	    super.onTestStop();
	    unregisterReceiver(br_finish);
	    unregisterReceiver(br_found);
	    adapter.cancelDiscovery();
	}
}
