package notification;

import com.stackio.jobio.officeApp.ApplicationOffice;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONObject;

import utility.Logger;

/**
 * Created by SAI on 11/28/2016.
 */
public class NotificationExtender extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        Logger.debug("in NotificationExtender");
        JSONObject data = receivedResult.payload.additionalData;
        if (data != null) {
            String title = data.optString("heading", "").equals("") ? receivedResult.payload.title : data.optString("heading", "");
            String body = data.optString("content", "").equals("") ? receivedResult.payload.body : data.optString("content", "");
            Notification.performActionWhileNotificationReceive(data, ApplicationOffice.getContext());
            Notification.showAndroidNotification(data, title, body, ApplicationOffice.getContext());
        }
        return true;
    }
}

