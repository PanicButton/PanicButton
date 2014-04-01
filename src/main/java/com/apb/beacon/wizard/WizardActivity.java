package com.apb.beacon.wizard;


import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apb.beacon.AppConstants;
import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.BaseFragmentActivity;
import com.apb.beacon.CalculatorActivity;
import com.apb.beacon.MainActivity;
import com.apb.beacon.R;
import com.apb.beacon.common.AppUtil;
import com.apb.beacon.common.MyTagHandler;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.sms.SetupContactsFragment;
import com.apb.beacon.sms.SetupMessageFragment;

public class WizardActivity extends BaseFragmentActivity {
////    private WizardViewPager viewPager;
//    private FragmentStatePagerAdapter pagerAdapter;

    Page currentPage;
    String pageId;
    String selectedLang;

    TextView tvToastMessage;
    Boolean flagRiseFromPause = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root_layout);
        
        callFinishActivityReceivier();

        
        tvToastMessage = (TextView) findViewById(R.id.tv_toast);


//        try {
//			pageId = getIntent().getExtras().getString("page_id");
//		} catch (Exception e) {
//			pageId = "home-not-configured";
//			e.printStackTrace();
//		}
        pageId = getIntent().getExtras().getString("page_id");
        selectedLang = ApplicationSettings.getSelectedLanguage(this);

        Log.e("WizardActivity.onCreate", "pageId = " + pageId);

        PBDatabase dbInstance = new PBDatabase(this);
        dbInstance.open();
        currentPage = dbInstance.retrievePage(pageId, selectedLang);
        dbInstance.close();

        if (currentPage == null) {
            Log.e(">>>>>>", "page = null");
            Toast.makeText(this, "Still to be implemented.", Toast.LENGTH_SHORT).show();
            AppConstants.PAGE_FROM_NOT_IMPLEMENTED = true;
            finish();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment fragment = null;

            if (currentPage.getType().equals("simple")) {
                tvToastMessage.setVisibility(View.INVISIBLE);
                fragment = new NewSimpleFragment().newInstance(pageId, AppConstants.FROM_WIZARD_ACTIVITY);
            }
            else if (currentPage.getType().equals("warning")) {
                tvToastMessage.setVisibility(View.INVISIBLE);
                fragment = new WarningFragment().newInstance(pageId);
            }
            else if (currentPage.getType().equals("modal")){
                tvToastMessage.setVisibility(View.INVISIBLE);
                Intent i = new Intent(WizardActivity.this, WizardModalActivity.class);
                i = AppUtil.clearBackStack(i);
                i.putExtra("page_id", pageId);
                i.putExtra("parent_activity", AppConstants.FROM_WIZARD_ACTIVITY);
                startActivity(i);
                finish();
                return;
            }
            else {          // type = interactive
                if (currentPage.getComponent().equals("contacts"))
                    fragment = new SetupContactsFragment().newInstance(pageId, AppConstants.FROM_WIZARD_ACTIVITY);
                else if (currentPage.getComponent().equals("message"))
                    fragment = new SetupMessageFragment().newInstance(pageId, AppConstants.FROM_WIZARD_ACTIVITY);
                else if (currentPage.getComponent().equals("code"))
                    fragment = new SetupCodeFragment().newInstance(pageId, AppConstants.FROM_WIZARD_ACTIVITY);
                else if (currentPage.getComponent().equals("language"))
                    fragment = new LanguageSettingsFragment().newInstance(pageId, AppConstants.FROM_WIZARD_ACTIVITY);
                else if (currentPage.getComponent().equals("alarm-test-hardware")){
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if(currentPage.getIntroduction() != null){
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }
                    fragment = new AlarmTestHardwareFragment().newInstance(pageId);
                }
                else if (currentPage.getComponent().equals("alarm-test-disguise")){
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if(currentPage.getIntroduction() != null){
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }
                    fragment = new AlarmTestDisguiseFragment().newInstance(pageId);
                }
                else if (currentPage.getComponent().equals("disguise-test-open")){
                    findViewById(R.id.wizard_layout_root).setBackgroundColor(Color.BLACK);
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if(currentPage.getIntroduction() != null){
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }
                    fragment = new TestDisguiseOpenFragment().newInstance(pageId);
                }
                else if (currentPage.getComponent().equals("disguise-test-unlock")){
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if(currentPage.getIntroduction() != null){
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }

                    fragment = new TestDisguiseUnlockFragment().newInstance(pageId);
                }
                else if (currentPage.getComponent().equals("disguise-test-code")){
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if(currentPage.getIntroduction() != null){
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }
                    fragment = new TestDisguiseCodeFragment().newInstance(pageId);
                }
                else
                    fragment = new NewSimpleFragment().newInstance(pageId, AppConstants.FROM_WIZARD_ACTIVITY);
            }
            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("WizardActivity.onPause", ".");
        if(currentPage.getId().equals("home-ready") && ApplicationSettings.isFirstRun(WizardActivity.this)) {
        		ApplicationSettings.setFirstRun(WizardActivity.this, false);
        		Intent i = new Intent(WizardActivity.this, MainActivity.class);
                i.putExtra("page_id", currentPage.getId());
                startActivity(i);

        		getPackageManager().setComponentEnabledSetting(
                        new ComponentName("com.apb.beacon", "com.apb.beacon.HomeActivity-calculator"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                getPackageManager().setComponentEnabledSetting(
                        new ComponentName("com.apb.beacon", "com.apb.beacon.HomeActivity-setup"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }

        if(!pageId.equals("setup-alarm-test-hardware")){            // we block this page for pause-resume action
            Log.e(">>>>>>", "assert flagRiseFromPause = " + true);
            flagRiseFromPause = true;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("WizardActivity.onStop", ".");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("WizardActivity.onStart", ".");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("WizardActivity.onResume", "flagRiseFromPause = " + flagRiseFromPause);

        int wizardState = ApplicationSettings.getWizardState(WizardActivity.this);

        if(wizardState == AppConstants.WIZARD_FLAG_COMPLETE){
            return;
        }

        if(AppConstants.PAGE_FROM_NOT_IMPLEMENTED){
            Log.e("WizardActivity.onResume", "returning from not-implemented page.");
            AppConstants.PAGE_FROM_NOT_IMPLEMENTED = false;
            return;
        }

        if(AppConstants.IS_BACK_BUTTON_PRESSED){
            Log.e("WizardActivity.onResume", "back button pressed");
            AppConstants.IS_BACK_BUTTON_PRESSED = false;
            return;
        }

        if(!ApplicationSettings.isFirstRun(WizardActivity.this) && currentPage.getId().equals("home-ready")){
            
        	callFinishActivityReceivier();
            finish();
        	
        	Intent i = new Intent(WizardActivity.this, CalculatorActivity.class);
            i = AppUtil.clearBackStack(i);
            startActivity(i);
            overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);

            
            return;
        }

        if (!pageId.equals("setup-alarm-test-hardware-success") && flagRiseFromPause) {
//        if (flagRiseFromPause) {

            if (wizardState == AppConstants.WIZARD_FLAG_HOME_NOT_COMPLETED) {
                pageId = "home-not-configured";
//            	pageId = "setup-contacts";
            } else if (wizardState == AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED_ALARM) {
                pageId = "home-not-configured-alarm";
            } else if (wizardState == AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED_DISGUISE) {
                pageId = "home-not-configured-disguise";
            } else if (wizardState == AppConstants.WIZARD_FLAG_HOME_READY) {
                pageId = "home-ready";
            }

            Intent i = new Intent(WizardActivity.this, WizardActivity.class);
            i = AppUtil.clearBackStack(i);
            i.putExtra("page_id", pageId);
            startActivity(i);
//            overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);

            callFinishActivityReceivier();

            finish();
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        hideToastMessageInInteractiveFragment();
    }

    public void hideToastMessageInInteractiveFragment(){
        tvToastMessage.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onBackPressed() {
        if(pageId.equals("home-ready")){
            // don't go back
        	finish();
        	startActivity(AppUtil.behaveAsHomeButton());
        }else{
            super.onBackPressed();
        }
        AppConstants.IS_BACK_BUTTON_PRESSED = true;
    }



}
