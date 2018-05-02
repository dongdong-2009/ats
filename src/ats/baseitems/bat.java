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
	             * �����׽����action��ACTION_BATTERY_CHANGED�� ������onBatteryInfoReceiver()
	             */
	            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
	            	
	            	// ��ص���
	            	int bat_level = intent.getIntExtra("level", 0);
	                int scale = intent.getIntExtra("scale", 100);
	                bat_level = bat_level*100/scale;
	                
	                // ��ط���
	                int bat_voltage = intent.getIntExtra("voltage", 0);
	                // ����¶�
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
	                    BatteryStatus = "�����";
	                    bat_state = "CHARGING";
	                    break;
	                case BatteryManager.BATTERY_STATUS_DISCHARGING:
	                    BatteryStatus = "�ŵ���";
	                    bat_state = "DISCHARGING";
	                    break;
	                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
	                    BatteryStatus = "δ���";
	                    bat_state = "NOT_CHARGING";
	                    break;
	                case BatteryManager.BATTERY_STATUS_FULL:
	                    BatteryStatus = "������";
	                    bat_state = "FULL";
	                    break;
	                case BatteryManager.BATTERY_STATUS_UNKNOWN:
	                default:
	                    BatteryStatus = "δ֪";
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
	                	BatteryPluged = "δ֪("+plugged+")";
	                }

	                switch (intent.getIntExtra("health",
	                        BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
	                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
	                default:
	                	BatteryHealth = "δ֪";
	                    break;
	                case BatteryManager.BATTERY_HEALTH_GOOD:
	                	BatteryHealth = "����";
	                	bat_health = "GOOD";
	                    break;
	                case BatteryManager.BATTERY_HEALTH_DEAD:
	                	BatteryHealth = "������";
	                	bat_health = "DEAD";
	                    break;
	                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
	                	BatteryHealth = "��ѹ����";
	                	bat_health = "OVER_VOLTAGE";
	                    break;
	                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
	                	BatteryHealth =  "��ع���";
	                	bat_health = "OVERHEAT";
	                    break;
	                }
	                
	                setInfo("");
	                me.addInfo("����: " + bat_level + "\n");
	                me.addInfo("��ѹ: "+ bat_voltage +"\n");
	                me.addInfo("�¶�: "+ bat_temp +"\n");
	                me.addInfo("����״̬: "+ BatteryStatus+"\n");
	                me.addInfo("��緽ʽ: "+ BatteryPluged+"\n");
	                me.addInfo("����״̬: "+ BatteryHealth+"\n");
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
