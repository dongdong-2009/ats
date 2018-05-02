package ats.baseitems;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.os.Bundle;
import android.content.res.Resources;


public class tp extends BaseActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		
		Resources res = getResources();
		final String packageName = getPackageName();
		int id_touch_view = res.getIdentifier("touch_view", "layout", packageName);
		int id_textview_info2 = res.getIdentifier("textview_info2", "id", packageName);
		if(id_touch_view==0 || id_textview_info2==0)
			finish();
		
		setContentView(id_touch_view);
		

		TextView tv_info = (TextView) findViewById(id_textview_info2);
		
		
	
		tv_info.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				String info;
				TextView tv_info = (TextView)arg0;
				if(arg1.getAction()==MotionEvent.ACTION_UP){
					tv_info.setText("");
					return true;
				}
				int c = arg1.getPointerCount();
				info = "触摸点数 : "+c+"\n";
				for(int i=0;i<c;i++){
					int x = (int) arg1.getX(i);
					int y = (int) arg1.getY(i);
					info += "触摸点"+i+"坐标: ["+x+","+y+"]\n";
				}
				
				tv_info.setText(info);
				return true;
			}
			
		});
		
	}
	
}
