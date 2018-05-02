package ats.baseitems;

import java.util.ArrayList;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;


//beacuse home key can hide activity,so,dont use back button
public class key extends BaseActivity
{
	public Button bt_exit;			//退出按钮
	public TextView tv_info;		//显示信息框
	
	
    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Resources res = getResources();
		final String packageName = getPackageName();
		int id_normal_textinfo = res.getIdentifier("normal_textinfo", "layout", packageName);
		int id_button_ok = res.getIdentifier("button_ok", "id", packageName);
		int id_textview_info = res.getIdentifier("textview_info", "id", packageName);
		
		if(id_normal_textinfo==0 || id_button_ok==0 || id_textview_info==0)
			finish();
		
        setContentView(id_normal_textinfo);
        
		//初始化显示
		bt_exit = (Button) findViewById(id_button_ok);
		tv_info = (TextView) findViewById(id_textview_info);
		
		bt_exit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		registerBoradcastReceiver();
		
		setInfo("Please Press Keys");
		
	}
	
	@Override
	public boolean dispatchKeyEvent(final KeyEvent event)
	{
		int action = event.getAction();
		if(action != KeyEvent.ACTION_UP)
			return true;
		int sk = event.getScanCode();
		int k = event.getKeyCode();
		String kname="";
		switch (k) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			kname = "volup";
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			kname = "voldn";
			break;
		case KeyEvent.KEYCODE_CAMERA:
			kname = "camera";
			break;
		case KeyEvent.KEYCODE_HOME:
			kname = "home";
			break;
		case KeyEvent.KEYCODE_BACK:
			kname = "back";
			break;
		case KeyEvent.KEYCODE_POWER:
			kname = "pow";
			break;
		case KeyEvent.KEYCODE_MENU:
			kname = "menu";
			break;
		case KeyEvent.KEYCODE_SEARCH:
			kname = "search";
			break;
		case KeyEvent.KEYCODE_HEADSETHOOK:
			kname = "headsetkey";
			break;
		default:
			break;
		}
		if(kname.equals("")==false){
			this.ReportKey(kname,sk);
		}
	    //return super.dispatchKeyEvent(event);
	    return true;
	}
	    
	class Key{
		public Key(String kname2, int sk) {
			// TODO Auto-generated constructor stub
			kname = kname2;
			kcode = sk;
		}
		String kname;
		int kcode;
	};
	ArrayList<Key> key_list = new ArrayList<Key>();
	private void ReportKey(String kname, int sk) {
		// TODO Auto-generated method stub
		addKey(new Key(kname,sk));
		CheckAndRecord();
	}
	private void ReportKey(String kname) {
		ReportKey(kname,0);
	}
	String info = "";
	void setInfo(String s)
	{
		info = s;
		this.tv_info.setText(info);
	}
	void addInfo(String s)
	{
		info += s;
		this.tv_info.setText(info);
	}


	private void CheckAndRecord() {
		// TODO Auto-generated method stub
		if(key_list.size()<0)
			return;
		setInfo("");
		String[] k_tab = new String[]{
				"home","menu","back","search","task","enter",
				"pow","volup","voldn","camera","ok",
				"headsetkey",
			};
		String res_name = "";
		String res_code = "";
		Key k = null;
		for(String kname : k_tab){
			k = findKeybyName(kname);
			if(k != null){
				res_name += "[" + k.kname + "]";
				res_code += "[" + k.kcode + "]";
			}
		}
		
		app.RecordResultString("key_name", res_name);
		app.RecordResultString("key_code", res_code+"");
		app.SyncResult();
		
		for(Key i : key_list){
			addInfo("Get Key : [" + i.kname + "][" + i.kcode+ "]\n");
		}
		addInfo("===================\n");
		addInfo("key_name=\""+res_name+"\"\n");
		addInfo("key_code=\""+res_code+"\"\n");
		
		
	}

	private Key findKeybyName(String kname) {
		// TODO Auto-generated method stub
		for(Key k : key_list){
			if(k.kname.equals(kname)){
				return k;
			}
		}
		return null;
	}

	private void addKey(Key key) {
		// TODO Auto-generated method stub
		for(Key k : key_list){
			if(k.kname.equals(key.kname)){
				return;
			}
		}
		key_list.add(key);
	}


	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {

		@Override

		public void onReceive(final Context context, final Intent intent) {

			key me = (key)context;
			final String action = intent.getAction();
	
//			if (Intent.ACTION_SCREEN_OFF.equals(action)){
//				me.ReportKey("pow?");
//			}
			if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)){
				String reason = intent.getStringExtra("reason");
				if(reason.equals("homekey")){
					me.is_press_home_key = true;
					me.ReportKey("home");
				}
				if(reason.equals("recentapps"))
					me.ReportKey("task");
			}	

		}

	};
	
      
    public void registerBoradcastReceiver(){  
    	
    	IntentFilter filter = new IntentFilter();
    	//filter.addAction(Intent.ACTION_SCREEN_OFF);
    	filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    	registerReceiver(mBatInfoReceiver, filter); 
    }  
	public void unregisterBoardcastReceiver(){
    	if(mBatInfoReceiver != null) { 
    		try {          
    			unregisterReceiver(mBatInfoReceiver);  
    		}catch(Exception e) {} 

    	}
    }
	boolean is_press_home_key = false;
	@Override
	public void onStop() 
	{
		super.onStop();
		if(is_press_home_key){
			is_press_home_key = false;
			this.startActivity(this.getIntent());
		}
			
	}
    
    @Override
	public void onDestroy() 
	{
		super.onDestroy();
		unregisterBoardcastReceiver();
		//startActivity(getIntent());
		//onTestStop();
		//finish();
	}
}
