package ats.baseitems;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestItemActivity extends BaseActivity
{
	public Button bt_exit;			//退出按钮
	public TextView tv_info;		//显示信息框
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//app.clearTestResult();
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
		
		onTestInit();
		
	}
	public void onTestInit()
	{
		this.setTitle("Test Item Frame");
		this.setInfo("Test Infomation.");
	}
	public void onTestStop()
	{
		
	}
	@Override
	public void onStop() 
	{
		super.onStop();
		onTestStop();
		finish();
	}
	private String info="";
	public void setInfo(String string) {
		// TODO Auto-generated method stub
		info = string;
		tv_info.setText(info);
	};
	public void addInfo(String string) {
		// TODO Auto-generated method stub
		info += string;
		tv_info.setText(info);
	};
}