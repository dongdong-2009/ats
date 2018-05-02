package ats.baseitems;

import java.io.IOException;

import android.os.SystemProperties;
import android.telephony.TelephonyManager;

public class ver extends TestItemActivity
{
	@Override
	public void onTestInit() {
		this.setTitle("Version Test");
		
		TelephonyManager tm = (TelephonyManager)getSystemService("phone");
		
		String sn = SystemProperties.get("ro.serialno");
		addInfo("SN: "+sn+"\n");
		app.RecordResultString("ver_sn",sn);
			
		String imei = tm.getDeviceId();
		addInfo("IMEI: "+imei+"\n");
		app.RecordResultString("ver_imei",imei);
		
		try {
			String build_prop = shell_exec("cat /system/build.prop");
			
			this.tv_info.setTextSize(14);
			//addInfo(build_prop);
			String info[] = build_prop.split("\\n+");
			StringBuilder res=new StringBuilder();
			for(String s:info){
				if(s.startsWith("ro.")){
					res.append(s+"\n");
					String version_perfix = "ro.build.innner.version=";
					if(s.startsWith(version_perfix)){
						String version = s.substring(version_perfix.length(), s.length());
						addInfo("VERSION: "+version+"\n");
						app.RecordResultString("ver",version);
					}
						
				}
			}
			addInfo("------------------------------------------------------------\n");
			addInfo(res.toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		app.SyncResult();
		
    }
	
}
