package ats.baseitems;

//import ats.autommi.R;


import android.os.BatteryManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class bat extends TestItemActivity
{

    
	@Override
	public void onTestInit() {
		this.setTitle("Battery Test");
		registerBoradcastReceiver();
    }
	@Override
	public void onTestStop() {
		unregisterBoardcastReceiver();
    }
	
	public void registerBoradcastReceiver(){  
    	
    	IntentFilter filter = new IntentFilter();
    	//filter.addAction(Intent.ACTION_SCREEN_OFF);
    	filter.addAction(Intent.ACTION_BATTERY_CHANGED);
    	registerReceiver(mBatInfoReceiver, filter); 
    }  

	public void unregisterBoardcastReceiver(){
    	if(mBatInfoReceiver != null) { 
    		try {          
    			unregisterReceiver(mBatInfoReceiver);  
    		}catch(Exception e) {} 

    	}
    }
	
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {

	        public void onReceive(Context context, Intent intent) {
	        	bat me = (bat) context;
	            String action = intent.getAction();
	            /*
	             * 如果捕捉到的action是ACTION_BATTERY_CHANGED， 就运行onBatteryInfoReceiver()
	             */
	            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
	            	
	            	// 电池电量
	            	int bat_level = intent.getIntExtra("level", 0);
	                int scale = intent.getIntExtra("scale", 100);
	                bat_level = bat_level*100/scale;
	                
	                // 电池伏数
	                int bat_voltage = intent.getIntExtra("voltage", 0);
	                // 电池温度
	                float bat_temp = intent.getIntExtra("temperature", 0)/10.f;
	                
	                String BatteryStatus;
	                String BatteryPluged;
	                String BatteryHealth;
	                
	                String bat_state = "UNKNOW";
	                String bat_plugged = "UNKNOW";
	                String bat_health = "UNKNOW";

	                switch (intent.getIntExtra("status",
	                        BatteryManager.BATTERY_STATUS_UNKNOWN)) {
	                case BatteryManager.BATTERY_STATUS_CHARGING:
	                    BatteryStatus = "充电中";
	                    bat_state = "CHARGING";
	                    break;
	                case BatteryManager.BATTERY_STATUS_DISCHARGING:
	                    BatteryStatus = "放电中";
	                    bat_state = "DISCHARGING";
	                    break;
	                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
	                    BatteryStatus = "未充电";
	                    bat_state = "NOT_CHARGING";
	                    break;
	                case BatteryManager.BATTERY_STATUS_FULL:
	                    BatteryStatus = "充满电";
	                    bat_state = "FULL";
	                    break;
	                case BatteryManager.BATTERY_STATUS_UNKNOWN:
	                default:
	                    BatteryStatus = "未知";
	                    break;
	                }
	                
	                int plugged = intent.getIntExtra("plugged",BatteryManager.BATTERY_PLUGGED_AC);
	                switch (plugged) {
	                case BatteryManager.BATTERY_PLUGGED_AC:
	                	BatteryPluged = "AC";
	                	bat_plugged = "AC";
	                    break;
	                case BatteryManager.BATTERY_PLUGGED_USB:
	                	BatteryPluged = "USB";
	                	bat_plugged = "USB";
	                    break;
	                default:
	                	BatteryPluged = "未知("+plugged+")";
	                }

	                switch (intent.getIntExtra("health",
	                        BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
	                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
	                default:
	                	BatteryHealth = "未知";
	                    break;
	                case BatteryManager.BATTERY_HEALTH_GOOD:
	                	BatteryHealth = "良好";
	                	bat_health = "GOOD";
	                    break;
	                case BatteryManager.BATTERY_HEALTH_DEAD:
	                	BatteryHealth = "电量低";
	                	bat_health = "DEAD";
	                    break;
	                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
	                	BatteryHealth = "电压过高";
	                	bat_health = "OVER_VOLTAGE";
	                    break;
	                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
	                	BatteryHealth =  "电池过热";
	                	bat_health = "OVERHEAT";
	                    break;
	                }
	                
	                setInfo("");
	                me.addInfo("电量: " + bat_level + "\n");
	                me.addInfo("电压: "+ bat_voltage +"\n");
	                me.addInfo("温度: "+ bat_temp +"\n");
	                me.addInfo("工作状态: "+ BatteryStatus+"\n");
	                me.addInfo("充电方式: "+ BatteryPluged+"\n");
	                me.addInfo("健康状态: "+ BatteryHealth+"\n");
	                me.addInfo("==================\n");
	                me.addInfo("bat_level=" + bat_level + "\n");
	                me.addInfo("bat_voltage=" + bat_voltage + "\n");
	                me.addInfo("bat_temp=" + bat_temp + "\n");
	                me.addInfo("bat_state=\"" + bat_state + "\"\n");
	                me.addInfo("bat_plugged=\"" + bat_plugged + "\"\n");
	                me.addInfo("bat_health=\"" + bat_health + "\"\n");
	                
	                me.app.RecordResult("bat_level", bat_level + "");
	                me.app.RecordResult("bat_voltage", bat_voltage + "");
	                me.app.RecordResult("bat_temp", bat_temp + "");
	                me.app.RecordResultString("bat_state", bat_state);
	                me.app.RecordResultString("bat_plugged", bat_plugged);
	                me.app.RecordResultString("bat_health", bat_health);
	                
	                me.app.SyncResult();

	                
	            }
	        }

	    };
}
