package com.stackio.jobio.officeApp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xwray.fontbinding.FontCache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import coaching.ManageMessage;
import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import entity.ClientFieldMessage;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.OptionMenu;
import utility.ServerResponse;
import utility.URLMapping;

public class acMessageList extends AppCompatActivity {
    Typeface ubuntuL, ubuntuC;
    public static final String LOCAL_PK = "localPK";
    public static final String STATUS = "status";
    ScrollView scrollView;
    ImageButton btnSend;
    EditText edMessaage;
    LinearLayout lyList;
    RelativeLayout lyNodataFound;
    String friendAdminUserId, selfAdminUserId;
    String name;
    AppCompatActivity ac;
    Context mContext;
    Helper objHelper = new Helper();
    Handler handler = new Handler();
    public static boolean IS_MESSAGE_LOADING_RUN = false;
    ArrayList<ClientFieldMessage> arrClientFieldMessage;
    Date dtLastDateInMessageList = null;
    Animation myFadeInAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.message_list);
        ac = this;
        mContext = this;
        ubuntuL = Helper.getUbuntuL(mContext);
        ubuntuC = Helper.getUbuntuC(mContext);
        myFadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.blink);
        setData();
        new ManageMessage().sendMessageScreen(ac);
    }

    private void setData() {
        new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                btnSend = (ImageButton) findViewById(R.id.btnSend);
                edMessaage = (EditText) findViewById(R.id.edMessaage);
                lyList = (LinearLayout) findViewById(R.id.lyList);
                scrollView = (ScrollView) findViewById(R.id.scrollView);
                lyNodataFound = (RelativeLayout) findViewById(R.id.lyNodataFound);
                edMessaage.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        setSendButtonVisiblility(s.toString());
                    }
                });
                btnSend.setOnClickListener(messageSendClick);
                selfAdminUserId = Helper.getStringPreference(mContext, ClientAdminUser.Fields.ADMINUSERID, "");
                if (ac.getIntent().getExtras() != null) {
                    friendAdminUserId = ac.getIntent().getStringExtra("friendAdminUserId");
                    name = ac.getIntent().getStringExtra("name");
                    objHelper.setActionBar(ac, name);
                }
                mContext.registerReceiver(objMessageListBroadcast, new IntentFilter(ConstantVal.BroadcastAction.CHANGED_MESSAGE_LIST));
                mContext.registerReceiver(objMessageStatusBroadcast, new IntentFilter(ConstantVal.BroadcastAction.CHANGED_MESSAGE_STATUS));
                IS_MESSAGE_LOADING_RUN = true;
            }

            @Override
            protected Object doInBackground(Object[] params) {
                arrClientFieldMessage = new ClientFieldMessage().getDataFromDatabase(mContext, friendAdminUserId, selfAdminUserId);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                setSendButtonVisiblility(edMessaage.getText().toString());
                prepareMessageData();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setSendButtonVisiblility(String s) {
        if (s.toString().length() > 0) {
            btnSend.setVisibility(View.VISIBLE);
        } else {
            btnSend.setVisibility(View.GONE);
        }
    }

    private void changeMessageStatusToView(final String notViewedIds) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                DataBase db = new DataBase(mContext);
                db.open();
                String where = "id in(" + notViewedIds + ")";
                ContentValues cv = new ContentValues();
                cv.put("is_viewed", "Y");
                db.update(DataBase.field_message_table, DataBase.field_message_int, where, cv);
                db.close();

                String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                HttpEngine objHttpEngine = new HttpEngine();
                URLMapping um = ConstantVal.updateMessageStatus(mContext);
                String[] Data = {String.valueOf(tokenId), notViewedIds, account_id};
                objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), Data, um.getParamNames(), um.isNeedToSync());
                return null;
            }
        }.execute();
    }

    View.OnClickListener messageSendClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AsyncTask() {
                ServerResponse sr;
                ClientFieldMessage objClientFieldMessage;
                View view = null;
                long localPK;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    String message = edMessaage.getText().toString();
                    String isViewed = "N";
                    Date dateTime = new Date();
                    String strDateTime = Helper.convertDateToString(dateTime, ConstantVal.DATE_FORMAT + " " + ConstantVal.TIME_FORMAT);
                    dateTime = Helper.convertStringToDate(strDateTime, ConstantVal.DATE_FORMAT + " " + ConstantVal.TIME_FORMAT);
                    String strFromId = String.valueOf(selfAdminUserId);
                    String strToId = String.valueOf(friendAdminUserId);
                    objClientFieldMessage = new ClientFieldMessage("0", message, strFromId, strToId, isViewed, dateTime, null);
                    edMessaage.setText("");
                    //update list
                    Date currentDate = new Date();
                    if (dtLastDateInMessageList != null) {
                        if ((currentDate.getDate() == dtLastDateInMessageList.getDate()) &&
                                (currentDate.getMonth() == dtLastDateInMessageList.getMonth()) &&
                                (currentDate.getYear() == dtLastDateInMessageList.getYear())) {
                        } else {
                            dtLastDateInMessageList = currentDate;
                            String strDate = Helper.getTextFromDate(mContext, currentDate);
                            addHeaderToLayout(strDate);
                        }
                    } else {
                        dtLastDateInMessageList = currentDate;
                        String strDate = Helper.getTextFromDate(mContext, currentDate);
                        addHeaderToLayout(strDate);
                    }
                    view = addItemToLayout(objClientFieldMessage);

                    //save data to local database
                    DataBase db = new DataBase(mContext);
                    db.open();
                    localPK = db.insert(DataBase.field_message_table, DataBase.field_message_int, new String[]{objClientFieldMessage.getId(),
                            objClientFieldMessage.getMessage(), objClientFieldMessage.getFrom_id(), objClientFieldMessage.getTo_id(),
                            objClientFieldMessage.getIs_viewed(), String.valueOf(objClientFieldMessage.getDate().getTime()), ""});
                    db.close();

                    if (view != null) {
                        view.setId((int) localPK);
                        //view.setTag(String.valueOf(localPK));
                    }
                    if (lyNodataFound.getVisibility() == View.VISIBLE)
                        lyNodataFound.setVisibility(View.GONE);
                    if (scrollView.getVisibility() == View.GONE)
                        scrollView.setVisibility(View.VISIBLE);
                }

                @Override
                protected Object doInBackground(Object[] params) {
                    //save data on server
                    String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                    String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                    HttpEngine objHttpEngine = new HttpEngine();
                    URLMapping um = ConstantVal.saveMessage(mContext);
                    String date = Helper.convertDateToString(objClientFieldMessage.getDate(), ConstantVal.DATE_FORMAT);
                    String time = Helper.convertDateToString(objClientFieldMessage.getDate(), ConstantVal.TIME_FORMAT);
                    String[] Data = {String.valueOf(tokenId), String.valueOf(localPK), objClientFieldMessage.getMessage(), objClientFieldMessage.getFrom_id(),
                            objClientFieldMessage.getTo_id(), objClientFieldMessage.getIs_viewed(), date, time, account_id};
                    sr = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), Data, um.getParamNames(), um.isNeedToSync());
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.NO_INTERNET)) {
                        Helper.displaySnackbar((AppCompatActivity) mContext, mContext.getString(R.string.msgSyncNoInternet), ConstantVal.ToastBGColor.INFO);
                    }
                }
            }.execute();
        }
    };

    private void prepareMessageData() {
        if (arrClientFieldMessage != null && arrClientFieldMessage.size() > 0) {
            lyList.removeAllViews();
            ArrayList<String> dates = new ArrayList<String>();
            HashMap<String, List<ClientFieldMessage>> tempListDataChild = new HashMap<String, List<ClientFieldMessage>>();
            try {
                int mainCount = 0;
                ArrayList<ClientFieldMessage> chileData = null;
                Date dt1 = null;
                for (; mainCount < arrClientFieldMessage.size(); ) {
                    dt1 = arrClientFieldMessage.get(mainCount).getDate();
                    chileData = new ArrayList<>();
                    for (; mainCount < arrClientFieldMessage.size(); mainCount++) {
                        Date dt2 = arrClientFieldMessage.get(mainCount).getDate();
                        if ((dt1.getDate() == dt2.getDate()) && (dt1.getMonth() == dt2.getMonth()) && (dt1.getYear() == dt2.getYear())) {
                            chileData.add(arrClientFieldMessage.get(mainCount));
                        } else {
                            //if (!isHeaderAdded) {
                            String strDate = Helper.getTextFromDate(mContext, dt1);
                            dates.add(strDate);
                            tempListDataChild.put(strDate, chileData);
                            break;
                        }
                    }
                }
                dtLastDateInMessageList = dt1;
                mainCount--;
                String strDate = Helper.getTextFromDate(mContext, dt1);
                dates.add(strDate);
                tempListDataChild.put(strDate, chileData);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.writeToCrashlytics(e);
            }

            String notViewdIds = "";
            for (String key : dates) {
                addHeaderToLayout(key);
                List<ClientFieldMessage> arr = tempListDataChild.get(key);
                for (ClientFieldMessage obj : arr) {
                    View v = addItemToLayout(obj);
                    if (v != null) {
                        v.setId((int) obj.get_ID());
                        //v.setTag(obj.get_ID());
                    }
                    if (obj.getFrom_id().equals(friendAdminUserId) &&
                            obj.getTo_id().equals(selfAdminUserId) &&
                            obj.getIs_viewed().equals("N")) {
                        notViewdIds += obj.getId() + ",";
                    }
                }
            }
            if (notViewdIds.length() > 0) {
                notViewdIds = notViewdIds.substring(0, notViewdIds.length() - 1);
                changeMessageStatusToView(notViewdIds);
            }
            lyNodataFound.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        } else {
            lyNodataFound.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }
    }

    private void addHeaderToLayout(String date) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View header = mInflater.inflate(R.layout.message_date_header, null);
        //View header = DataBindingUtil.inflate(mInflater, R.layout.message_date_header, null, true).getRoot();
        TextView txtHeader = (TextView) header.findViewById(R.id.txtHeader);
        txtHeader.setTypeface(ubuntuC);
        txtHeader.setText(date);
        lyList.addView(header);
    }

    private View addItemToLayout(ClientFieldMessage obj) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = null;
        if (obj.getFrom_id().equals(selfAdminUserId) && obj.getTo_id().equals(friendAdminUserId)) {//sender, from=self, to=friend
            v = mInflater.inflate(R.layout.message_loginuser_sent_left, null);
            ImageView img = (ImageView) v.findViewById(R.id.status);
            int status = getStatusOfMessage(obj);
            setImageofStatus(img, status);
        } else if (obj.getFrom_id().equals(friendAdminUserId) && obj.getTo_id().equals(selfAdminUserId)) {//reciever, from=friend,to=self
            v = mInflater.inflate(R.layout.message_peer_received_right, null);
        }
        TextView message = (TextView) v.findViewById(R.id.txtMessage);
        message.setTypeface(ubuntuC);
        message.setText(obj.getMessage());
        TextView time = (TextView) v.findViewById(R.id.txtTime);
        time.setTypeface(ubuntuL);
        time.setText(Helper.convertDateToAbbrevString(Helper.convertDateToString(obj.getDate(), ConstantVal.DATE_FORMAT + " " + ConstantVal.TIME_FORMAT)));
        if (obj.getFrom_id().equals(friendAdminUserId) && obj.getTo_id().equals(selfAdminUserId) && obj.getIs_viewed().equals("N")) {
            v.startAnimation(myFadeInAnimation);
            v.startAnimation(myFadeInAnimation);
        }
        lyList.addView(v);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, lyList.getBottom());
            }
        });
        return v;
    }

    private void setImageofStatus(ImageView img, int status) {
        if (status == ConstantVal.MessageChatStatus.WAITING)
            img.setImageResource(R.drawable.ic_watch_white);
        if (status == ConstantVal.MessageChatStatus.SENT)
            img.setImageResource(R.drawable.ic_single_tick_white);
        if (status == ConstantVal.MessageChatStatus.VIEW)
            img.setImageResource(R.drawable.ic_double_tick_white);
    }

    private int getStatusOfMessage(ClientFieldMessage obj) {
        if (obj.getId().equals("0") && obj.getTimeStamp() == null) {
            return ConstantVal.MessageChatStatus.WAITING;
        } else if ((!obj.getId().equals("0") && obj.getTimeStamp() != null) && (obj.getIs_viewed().equals("N"))) {
            return ConstantVal.MessageChatStatus.SENT;
        } else if ((!obj.getId().equals("0") && obj.getTimeStamp() != null) && (obj.getIs_viewed().equals("Y"))) {
            return ConstantVal.MessageChatStatus.VIEW;
        }
        return ConstantVal.MessageChatStatus.WAITING;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IS_MESSAGE_LOADING_RUN = false;
        mContext.unregisterReceiver(objMessageListBroadcast);
        mContext.unregisterReceiver(objMessageStatusBroadcast);
    }

    OptionMenu objOptionMenu = new OptionMenu();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        objHelper.registerSessionTimeoutBroadcast(ac);
        updateWindowScreen("Y", friendAdminUserId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        objHelper.unRegisterSesionTimeOutBroadcast(ac);
        updateWindowScreen("N", "");
    }

    private BroadcastReceiver objMessageStatusBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {//Receive the broadcast from asyncMessageList
            final int localPK = intent.getIntExtra(acMessageList.LOCAL_PK, 0);
            final int status = intent.getIntExtra(acMessageList.STATUS, 0);
            //Logger.debug("Broadcast receive:" + localPK + " " + status);
            new AsyncTask() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    View v = findViewFromLyList(localPK);
                    if (v != null) {
                        ImageView img = (ImageView) v.findViewById(R.id.status);
                        if (img != null)
                            setImageofStatus(img, status);
                    }
                }

                @Override
                protected Object doInBackground(Object[] params) {
                    return null;
                }
            }.execute();
        }
    };

    private BroadcastReceiver objMessageListBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {//Receive the broadcast from asyncMessageList
            new AsyncTask() {

                @Override
                protected Object doInBackground(Object[] params) {
                    //Logger.debug("Broadcast has been received");
                    arrClientFieldMessage = new ClientFieldMessage().getDataFromDatabase(context, friendAdminUserId, selfAdminUserId);
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    prepareMessageData();
                }
            }.execute();
        }
    };

    private View findViewFromLyList(int localPK) {
        try {
            View v = lyList.findViewById(localPK);
            if (v != null) {
                return v;
            }
            /*for (int i = 0; i < lyList.getChildCount(); i++) {
                View view = lyList.getChildAt(i);
                if(view.getTag()!=null) {
                    if (view.getTag().toString().equals(String.valueOf(localPK))) {
                        return view;
                    }
                }
            }*/
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        }
    }

    private void updateWindowScreen(final String isWindowOpen, final String toUserId) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                if (isWindowOpen.equals("Y"))
                    Helper.setStringPreference(mContext, ConstantVal.CURRENT_CHAT_FRIEND, toUserId);
                else if (isWindowOpen.equals("N"))
                    Helper.clearPreference(mContext, ConstantVal.CURRENT_CHAT_FRIEND);
                String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                HttpEngine objHttpEngine = new HttpEngine();
                URLMapping um = ConstantVal.updateChatWindowUser(mContext);
                String[] Data = {String.valueOf(tokenId), account_id, selfAdminUserId, isWindowOpen, toUserId};
                objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), Data, um.getParamNames(), um.isNeedToSync());
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}