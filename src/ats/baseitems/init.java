package ats.baseitems;

import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;
import android.content.res.Resources;

public class init  extends Activity
{  
	TheApp app;
	public Button bt_exit;			//退出按钮
	public TextView tv_info;		//显示信息框
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//不添加自己的实例
		app= (TheApp)getApplication();
		//app.addThisInstance(this);
		super.onCreate(savedInstanceState);
		// Disable the Lock of Screen.
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.setTitle("ATS Autommi Init");
		
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
		
		


	}
	@Override
	public void onStart()
	{
		super.onStart();
		for(Activity a : app.activity_list){
			try{
				if(a != null);
					a.finish();
			} catch (Exception e) {   
	            e.printStackTrace();   
	        } finally {   
	            System.exit(0);   
	        }   
		}
	}
}
