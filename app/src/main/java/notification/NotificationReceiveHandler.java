package notification;

import com.lab360io.jobio.officeApp.ApplicationOffice;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import utility.Logger;

/**
 * Created by SAI on 11/19/2016.
 */
public class NotificationReceiveHandler implements OneSignal.NotificationReceivedHandler {
    @Override
    public void notificationReceived(OSNotification notification) {
        Logger.debug("Notification receive ");

        JSONObject data = notification.payload.additionalData;
        Notification.performActionWhileNotificationReceive(data, ApplicationOffice.getContext());
    }
}

