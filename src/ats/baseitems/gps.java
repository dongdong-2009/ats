package ats.baseitems;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.LocationListener;
import android.location.GpsStatus;
//import android.os.Handler;
import android.location.LocationManager;
//import android.widget.TextView;
import android.os.Bundle;

import java.util.Iterator;
import android.location.Location;

public class gps extends TestItemActivity implements LocationListener, GpsStatus.Listener
{
    public static final String TAG = "GPSTest";
    //private Handler hd;
    LocationManager mgr;
    //private int nosta;
    //private PowerManager pm;
    String prefered;
//    private String res;
//    private TextView tip;
//    private TextView tip2;
//    private TextView tip3;

	@Override
	public void onTestInit() {
		this.setTitle("GPS Test");
		
		mgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,  
				 1000, 0, this);  
        //pm = (PowerManager)getSystemService("power");
		this.setInfo(" GPS is Searching...\r\n");
    }
//	protected void onResume() {
//        super.onResume();
//        mgr.requestLocationUpdates("gps", 0x0, 0.0f, this);
//        mgr.addGpsStatusListener(this);
//        startCount();
//    }
//    
//    private void startCount() {
//        hd.sendEmptyMessageDelayed(0x1, 0x3e8);
//    }
//    
//    private void stopCount() {
//        hd.removeMessages(0x1);
//    }
    
//    protected void onPause() {
//        super.onPause();
//        mgr.removeUpdates(this);
//        mgr.removeGpsStatusListener(this);
//        Settings.Secure.setLocationProviderEnabled(getContentResolver(), "gps", false);
//        stopCount();
//        finish();
//    }

	@Override
	public void onGpsStatusChanged(int event) {
		// TODO Auto-generated method stub
		this.addInfo("GPS State Changed.\r\n");
		
	}
	
	@Override
	public void onLocationChanged(Location location) {
		
		this.setInfo("Location Changed.\r\n");

		// TODO Auto-generated method stub
		double la = location.getLatitude();
        double lo = location.getLongitude();
        double al = location.getAltitude();
        this.addInfo( "���� : "+lo+"\r\n");
        this.addInfo( "γ�� : "+la+"\r\n");
        this.addInfo( "���� : "+al+"\r\n");
        
        app.RecordResult("gps_long", lo+"");
        app.RecordResult("gps_lat", la+"");
		app.RecordResult("gps_alt", al+"");
		
		GpsStatus status = mgr.getGpsStatus(null); //ȡ��ǰ״̬  

// 		StringBuilder sb2 = new StringBuilder("");  
        if (status == null) {  
        	this.addInfo( "�����������Ǹ���_��" +0+"\r\n"); 
        	app.RecordResult("gps_count", 0+"");
        } else {
            int maxSatellites = status.getMaxSatellites();  
            Iterator<GpsSatellite> it = status.getSatellites().iterator();  
            int count = 0;  
            while (it.hasNext() && count <= maxSatellites) {  
                GpsSatellite s = it.next();  
                count++; 
                this.addInfo( "����"+count+" : "+s.getAzimuth()+"\r\n");
                		
                 
            }  
            this.addInfo("�����������Ǹ�����" + count +"\r\n"); 
            app.RecordResult("gps_count", count+"");
        }  
        
         app.SyncResult();
    }  
		

	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		this.addInfo( " State Changed.\r\n");
	}
	
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		//this.addInfo( "onProviderEnabled.\r\n");
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		this.setInfo("GPS is disabled.\r\n");
	}
}
