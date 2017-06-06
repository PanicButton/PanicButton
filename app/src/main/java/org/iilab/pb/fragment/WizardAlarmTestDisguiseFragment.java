package org.iilab.pb.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;
import org.iilab.pb.trigger.MultiClickEvent;

import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppUtil.vibrateForConfirmationOfAlertTriggered;
import static org.iilab.pb.common.AppUtil.vibrateForHapticFeedback;

/**
 * Created by aoe on 1/16/14.
 */
public class WizardAlarmTestDisguiseFragment extends Fragment {

    private Activity activity;

    private int[] buttonIds = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight,
            R.id.nine, R.id.zero, R.id.equals_sign, R.id.plus, R.id.minus, R.id.multiply, R.id.divide, R.id.decimal_point, R.id.char_c};

    private int lastClickId = -1;

    Page currentPage;
    private static final String TAG = WizardAlarmTestDisguiseFragment.class.getName();

    public static WizardAlarmTestDisguiseFragment newInstance(String pageId) {
        WizardAlarmTestDisguiseFragment f = new WizardAlarmTestDisguiseFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView before inflate");
        View view = inflater.inflate(R.layout.calculator_layout, container, false);
        Log.d(TAG, "onCreateView before registerButtonEvents");
        registerButtonEvents(view);
        Log.d(TAG, "onCreateView after registerButtonEvents");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        if (activity != null) {
            String pageId = getArguments().getString(PAGE_ID);
            String selectedLang = ApplicationSettings.getSelectedLanguage(activity);

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause WizardAlarmTestDisguiseFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume WizardAlarmTestDisguiseFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView WizardAlarmTestDisguiseFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy WizardAlarmTestDisguiseFragment");
    }


    private void registerButtonEvents(View view) {
        for (int buttonId : buttonIds) {
            Button button = (Button) view.findViewById(buttonId);
            button.setOnClickListener(clickListener);
        }
    }

    private void unregisterButtonEvents(Activity activity) {
        for (int buttonId : buttonIds) {
            Button button = (Button) activity.findViewById(buttonId);
            button.setOnClickListener(null);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            Log.d(TAG, "onClick id " + id);
            Log.d(TAG, "onClick lastClickId " + lastClickId);

            MultiClickEvent multiClickEvent = (MultiClickEvent) view.getTag();
            if (multiClickEvent == null) {
                Log.d(TAG, "multiClickEvent reset");
                multiClickEvent = resetEvent(view);
            }

            if (id != lastClickId) multiClickEvent.reset();
            lastClickId = id;
            multiClickEvent.registerClick(System.currentTimeMillis());

            if(multiClickEvent.skipCurrentClick()){
                Log.d(TAG, "multiClickEvent skip");
                multiClickEvent.resetSkipCurrentClickFlag();
                return;
            }
            if(multiClickEvent.canStartVibration()) {
//                vibrate(ONE_SECOND*Integer.parseInt(DEFAULT_HAPTIC_FEEDBACK_DURATION));
                vibrateForHapticFeedback(getActivity());
                CharSequence text = ((Button) view).getText();
                Toast.makeText(activity, "Press the button '" + text + "' once the vibration ends to trigger alerts", Toast.LENGTH_LONG).show();
            }
            else if(multiClickEvent.isActivated()){
                Log.d(TAG, "multiClickEvent isActivated");
//                vibrate(ALERT_CONFIRMATION_VIBRATION_DURATION);
                vibrateForConfirmationOfAlertTriggered(getActivity());
                resetEvent(view);
                //unregisterButtonEvents(activity);

                String pageId = currentPage.getSuccessId();
                Intent i = new Intent(activity, WizardActivity.class);
                i.putExtra(PAGE_ID, pageId);
                activity.startActivity(i);
                activity.finish();
            }
        }
    };

    private void vibrate(int vibrationDuration) {
        Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibrationDuration);
    }

    private MultiClickEvent resetEvent(View view) {
        MultiClickEvent multiClickEvent = new MultiClickEvent(getActivity());
        view.setTag(multiClickEvent);
        return multiClickEvent;
    }
}
