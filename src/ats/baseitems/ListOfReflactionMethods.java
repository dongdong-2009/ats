package ats.baseitems;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import android.content.Context;

public class ListOfReflactionMethods extends TestItemActivity
{
	@Override
	public void onTestInit() {
		this.setTitle("Version Test");
		
		String info = "";
		
		info += getServiceMethodsInfo(Context.TELEPHONY_SERVICE);
		info += getServiceMethodsInfo(Context.AUDIO_SERVICE);

		setInfo(info);
    }

	private String getServiceMethodsInfo(String service_name) {
		return getMethodsInfo(this.getSystemService(service_name));	
	}

	private String getMethodsInfo(Object o) {
		String info ="";
		info += "=============================";
		info += "class "+ o.getClass().getName() +" :\n";
		Method[] methods = o.getClass().getMethods();
		info += "Method Count : " + methods.length + "\n";
		if(methods.length == 0)
			return info;
		for(Method method : methods){
			info += "method : " + method.getName() + "\n";
			info += getAnnotation(method); 
		}
		return info;
	}

	private String getAnnotation(Method method) {
		String res = "";
		Annotation[] annos = method.getAnnotations();
		if(annos.length==0)
			return res;
		for(Annotation anno : annos){
			res += anno.toString() + "\n";
		}
		return res;
	}
	
}
