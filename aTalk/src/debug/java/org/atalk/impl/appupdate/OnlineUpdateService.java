package org.atalk.impl.appupdate;

import static org.atalk.impl.androidtray.NotificationPopupHandler.getPendingIntentFlag;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import net.java.sip.communicator.service.update.UpdateService;
import net.java.sip.communicator.util.ServiceUtils;

import org.atalk.ohos.R;
import org.atalk.ohos.gui.AndroidGUIActivator;
import org.atalk.ohos.gui.settings.SettingsFragment;
import org.atalk.impl.androidnotification.AndroidNotifications;
import org.atalk.service.configuration.ConfigurationService;

import java.util.Calendar;
public class OnlineUpdateService extends IntentService
{
    public static final String ACTION_AUTO_UPDATE_APP = "org.atalk.ohos.ACTION_AUTO_UPDATE_APP";
    public static final String ACTION_AUTO_UPDATE_START = "org.atalk.ohos.ACTION_AUTO_UPDATE_START";
    public static final String ACTION_AUTO_UPDATE_STOP = "org.atalk.ohos.ACTION_AUTO_UPDATE_STOP";
    private static final String ACTION_UPDATE_AVAILABLE = "org.atalk.ohos.ACTION_UPDATE_AVAILABLE";
    private static final String ONLINE_UPDATE_SERVICE = "OnlineUpdateService";
    private static final String UPDATE_AVAIL_TAG = "aTalk Update Available";

    // in unit of seconds
    public static int CHECK_INTERVAL_ON_LAUNCH = 30;
    public static int CHECK_NEW_VERSION_INTERVAL = 24 * 60 * 60;
    private static final int UPDATE_AVAIL_NOTIFY_ID = 1;

    private NotificationManager mNotificationMgr;

    public OnlineUpdateService()
    {
        super(ONLINE_UPDATE_SERVICE);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mNotificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ACTION_AUTO_UPDATE_APP:
                        checkAppUpdate();
                        break;
                    case ACTION_UPDATE_AVAILABLE:
                        UpdateServiceImpl updateService = (UpdateServiceImpl) ServiceUtils
                                .getService(AndroidGUIActivator.bundleContext, UpdateService.class);
                        if (updateService != null) {
                            updateService.checkForUpdates();
                        }
                        break;
                    case ACTION_AUTO_UPDATE_START:
                        setNextAlarm(CHECK_INTERVAL_ON_LAUNCH);
                        break;
                    case ACTION_AUTO_UPDATE_STOP:
                        stopAlarm();
                        break;
                }
            }
        }
    }

    private void checkAppUpdate()
    {
        boolean isAutoUpdateCheckEnable = true;
        ConfigurationService cfg = AndroidGUIActivator.getConfigurationService();
        if (cfg != null)
            isAutoUpdateCheckEnable = cfg.getBoolean(SettingsFragment.AUTO_UPDATE_CHECK_ENABLE, true);

        UpdateService updateService = ServiceUtils.getService(AndroidGUIActivator.bundleContext, UpdateService.class);
        if (updateService != null) {
            boolean isLatest = updateService.isLatestVersion();

            if (!isLatest) {
                NotificationCompat.Builder nBuilder;
                nBuilder = new NotificationCompat.Builder(this, AndroidNotifications.DEFAULT_GROUP);

                String msgString = getString(R.string.update_vew_version_available,
                        updateService.getLatestVersion());
                nBuilder.setSmallIcon(R.drawable.ic_notification);
                nBuilder.setWhen(System.currentTimeMillis());
                nBuilder.setAutoCancel(true);
                nBuilder.setTicker(msgString);
                nBuilder.setContentTitle(getString(R.string.application_name));
                nBuilder.setContentText(msgString);

                Intent intent = new Intent(this.getApplicationContext(), OnlineUpdateService.class);
                intent.setAction(ACTION_UPDATE_AVAILABLE);
                PendingIntent pending = PendingIntent.getService(this, 0, intent,
                        getPendingIntentFlag(false, true));
                nBuilder.setContentIntent(pending);
                mNotificationMgr.notify(UPDATE_AVAIL_TAG, UPDATE_AVAIL_NOTIFY_ID, nBuilder.build());
            }
        }

        if (isAutoUpdateCheckEnable)
            setNextAlarm(CHECK_NEW_VERSION_INTERVAL);
    }

    private void setNextAlarm(int nextAlarmTime)
    {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this.getApplicationContext(), OnlineUpdateService.class);
        intent.setAction(ACTION_AUTO_UPDATE_APP);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
                getPendingIntentFlag(false, true));
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.SECOND, nextAlarmTime);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    private void stopAlarm()
    {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this.getApplicationContext(), OnlineUpdateService.class);
        intent.setAction(ACTION_AUTO_UPDATE_APP);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
                getPendingIntentFlag(false, true));
        alarmManager.cancel(pendingIntent);
    }
}