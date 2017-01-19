package notification;

import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.lab360io.jobio.officeApp.ApplicationOffice;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONObject;

import java.math.BigInteger;

import utility.Logger;

/**
 * Created by SAI on 11/28/2016.
 */
public class NotificationExtender extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        Logger.debug("in NotificationExtender");

        /*OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                builder.setSound(null);
                builder.setDefaults(0);
                return builder;
            }
        };*/

        JSONObject data = receivedResult.payload.additionalData;
        Notification.performActionWhileNotificationReceive(data, ApplicationOffice.getContext());
        Notification.showAndroidNotification(data, receivedResult.payload.title, receivedResult.payload.body, ApplicationOffice.getContext());
        return true;
    }
}

