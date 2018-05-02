package ats.baseitems;

import android.app.Activity;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

public class sim extends TestItemActivity
{

	int sim_count=1;
	int sim1_state=0,sim2_state=0;
    /* ���ǿ���������onResume��onPause����ֹͣlistene */
	TelephonyManager tel;
	MyPhoneStateListener MyListener;
	
	@Override
	public void onTestInit() {
		this.setTitle("Sim Card Test");
		/* Update the listener, and start it */
		MyListener = new MyPhoneStateListener(this);
		tel = (TelephonyManager) getSystemService(Activity.TELEPHONY_SERVICE);
		sim1_state = tel.getSimState();
		tel.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	
    }
	
	/* ��ʼPhoneState���� */
	private class MyPhoneStateListener extends PhoneStateListener {
		sim sim;
		String[] sim_state_tab = new String[6];
		/* �ӵõ����ź�ǿ��,ÿ��tiome��Ӧ���и��� */
		public MyPhoneStateListener(sim sim) {
			// TODO Auto-generated constructor stub
			this.sim = sim;
			sim_state_tab[0] = new String("UNKNOWN");
			sim_state_tab[1] = new String("ABSENT");
			sim_state_tab[2] = new String("PIN_REQUIRED");
			sim_state_tab[3] = new String("PUK_REQUIRED");
			sim_state_tab[4] = new String("NETWORK_LOCKED");
			sim_state_tab[5] = new String("READY");
		}

		
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
		
			String sim_1_sig = String.valueOf(signalStrength.getGsmSignalStrength());
			String info = 
					"SIM������: "+(sim.sim_count==-1?"δ֪":sim.sim_count)+"\n"+
					"SIM1״̬: "+sim.sim1_state+"("+sim_state_tab[sim.sim1_state]+")\n"+
					"SIM2״̬: "+sim.sim2_state+"("+sim_state_tab[sim.sim2_state]+")\n"+
					"Ĭ��SIM���ź�ǿ�� : " + sim_1_sig +"\n";
			
			info += "-------------------------\n";
			info += "sim_count=" + sim.sim_count + "\n";
			info += "sim_1_state=\"" + sim_state_tab[sim.sim1_state] + "\"\n";
			info += "sim_2_state=\"" + sim_state_tab[sim.sim2_state] + "\"\n";
			info += "sim_1_sig=" + sim_1_sig + "\n";
			
			sim.setInfo(info);
			
			app.RecordResult("sim_count", sim.sim_count + "");
			app.RecordResultString("sim_1_state", sim_state_tab[sim.sim1_state] + "");
			app.RecordResultString("sim_2_state", sim_state_tab[sim.sim2_state] + "");
			app.RecordResult("sim_1_sig", sim_1_sig);
			
			app.SyncResult();
		}
	}
}
