package org.iilab.pb.common;

public class AppConstants {
    //Test values
	public static final boolean SKIP_WIZARD = false;
    //public static final int PHONE_NUMBER_LIMIT = 3;

	//Prod values
    //public static final boolean SKIP_WIZARD = false;
    public static final int PHONE_NUMBER_LIMIT = 3;
    public static final long ONE_MINUTE = 1000 * 60;
	
    public static final int HAPTIC_FEEDBACK_DURATION = 2000;
    public static final int ALERT_CONFIRMATION_VIBRATION_DURATION = 500;

    public static final int DEFAULT_ALARM_INTERVAL = 5;             // in minute

    public static final float GPS_MIN_DISTANCE = 0;
    public static final long GPS_MIN_TIME_IN_FIRST_ONE_MINUTE = 1000 * 20;
    public static final long GPS_MIN_TIME = 1000 * 60 * 2;

    public static final float NETWORK_MIN_DISTANCE = 0;
    public static final long NETWORK_MIN_TIME_IN_FIRST_ONE_MINUTE = 1000 * 20;
    public static final long NETWORK_MIN_TIME = 1000 * 60 * 2;

    public static final int WARNING_TRAINING_MESSAGE_MINIMUM_CHARACTER = 30;

    public static final int WIZARD_FLAG_HOME_NOT_CONFIGURED = 601;
    public static final int WIZARD_FLAG_HOME_NOT_CONFIGURED_ALARM = 602;
    public static final int WIZARD_FLAG_HOME_NOT_CONFIGURED_DISGUISE = 603;
    public static final int WIZARD_FLAG_HOME_READY = 604;

    public static boolean IS_BACK_BUTTON_PRESSED = false;
//    public static boolean IS_ACTION_ITEM_PRESSED = false;
    public static boolean PAGE_FROM_NOT_IMPLEMENTED = false;
    
    public static final int FROM_WIZARD_ACTIVITY = 1;
    public static final int FROM_MAIN_ACTIVITY = 2;

    public static final double IMAGE_SCALABILITY_FACTOR = 0.5;

    public static final String TABLE_PRIMARY_KEY = "_id";

//    public static final String BASE_URL = "https://panicbutton.io";
//    public static final String HELP_DATA_URL = "/api/help.json";
//    public static final String VERSION_CHECK_URL = "/api/version.json";

    public static final int DATABASE_VERSION = 17;

    public static final String DEFAULT_CONFIRMATION_MESSAGE = "Settings saved";

    public static final int SPLASH_DELAY_TIME = 200;

    public static final int IMAGE_DOWNLOAD_TIMEOUT_MS = 3000;

    public static final int IMAGE_FULL_WIDTH = 1;
    public static final int IMAGE_INLINE = 2;

    //added a constant for changing the disguise screen unlock time from 5 sec to 3 sec
    public static final int DISGUISE_UNLOCK_LONGPRESS_TIME = 1800;

    public static final String PARENT_ACTIVITY = "parent_activity";
    public static final String PAGE_ID = "page_id";

    public static final String COLOR_RED="red";
    public static final String PAGE_HOME_READY = "home-ready";
    public static final String PAGE_HOME_ALERTING = "home-alerting";
    public static final String PAGE_CLOSE = "close";
    public static final String PAGE_HOME_NOT_CONFIGURED = "home-not-configured";
    public static final String PAGE_HOME_NOT_CONFIGURED_ALARM = "home-not-configured-alarm";
    public static final String PAGE_HOME_NOT_CONFIGURED_DISGUISE = "home-not-configured-disguise";
    public static final String PAGE_SETUP_ALARM_TEST_HARDWARE_SUCCESS = "setup-alarm-test-hardware-success";
    public static final String PAGE_SETUP_ALARM_TEST_DISGUISE_SUCCESS = "setup-alarm-test-disguise-success";

    public static final String PAGE_STATUS_CHECKED = "checked";

    public static final String DEFAULT_LANGUAGE_ENG = "en";
    public static final String PAGE_SETUP_LANGUAGE = "setup-language";
    public static final String JSON_EXTENSION=".json";

    public static final String JSON_OBJECT_MOBILE = "mobile";
    public static final String JSON_ARRAY_DATA = "data";
    public static final String JSON_OBJECT_HELP = "help";
    public static final String VERSION = "version";
    public static final String PREFIX_MOBILE_DATA = "mobile_";
    public static final String PREFIX_HELP_DATA = "help_";
    public static final String DELIMITER_COMMA=",";

}

