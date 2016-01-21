package org.iilab.pb.trigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.iilab.pb.alert.PanicAlert;
import org.iilab.pb.common.ApplicationSettings;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static org.iilab.pb.common.AppConstants.WIZARD_FLAG_HOME_READY;
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = BootReceiver.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Map<String, String> eventLog = new HashMap<String, String>();
        eventLog.put("Restarted the app on booting", new Date(System.currentTimeMillis()).toString());
        if(intent.getAction().equals(ACTION_BOOT_COMPLETED)) {
//        	new PanicAlert(context).deActivate();
        	if(ApplicationSettings.isAlertActive(context)){
                Log.e(TAG, "Alarm is active");
	        	ApplicationSettings.setAlertActive(context, false);
	        	new PanicAlert(context).activate();
        	}

            int wizardState = ApplicationSettings.getWizardState(context.getApplicationContext());
            Log.e(TAG, "wizardState = " + wizardState);
            if (wizardState == WIZARD_FLAG_HOME_READY && ApplicationSettings.isHardwareTriggerServiceEnabled(context)) {
                Log.e(TAG, "BootReceiver in Panic Button");
                context.startService(new Intent(context, HardwareTriggerService.class));
            }
        }
    }
}
