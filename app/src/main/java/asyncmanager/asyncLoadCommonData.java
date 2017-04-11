package asyncmanager;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lab360io.jobio.officeApp.R;
import com.lab360io.jobio.officeApp.acAssetDetail;
import com.lab360io.jobio.officeApp.acHome;
import com.lab360io.jobio.officeApp.acItemDetail;

import java.util.Date;

import entity.BusinessAccountdbDetail;
import entity.ClientAdminUserEmployee;
import entity.ClientAsset;
import entity.ClientItemMaster1;
import parser.parseCommonData;
import utility.CircleImageView;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 4/25/2016.
 */
public class asyncLoadCommonData {
    Context ctx;
    Date dtCurrentTime;


    public asyncLoadCommonData(Context ctx) {
        this.ctx = ctx;
    }

    public Thread startSync() {
        Thread t = new Thread() {
            public void run() {

            }
        };
        t.start();
        return t;
    }


    public void loadAssetPhotoById(final CircleImageView img, final ClientAsset objClientAsset, final View.OnClickListener imgClick, final int noPic) {
        new AsyncTask() {
            String photo = "";
            String strServerResponse = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setPreExecutionPhotoToImageView(ctx, objClientAsset.getPhoto(), img, imgClick, noPic);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (objClientAsset.getPhoto() == null || objClientAsset.getPhoto().length() <= 0) {
                    String id = objClientAsset.getAoAsset_id();
                    final HttpEngine objHttpEngine = new HttpEngine();
                    final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
                    String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                    final URLMapping um = ConstantVal.loadPhoto(ctx);
                    ServerResponse sr = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(), new String[]{String.valueOf(tokenId), String.valueOf(id), String.valueOf(4), account_id}, um.getParamNames(), um.isNeedToSync());
                    strServerResponse = sr.getResponseCode();
                    parseCommonData objparseCommonData = new parseCommonData();
                    photo = objparseCommonData.parsePhoto(sr.getResponseString());
                    //update item_master table
                    if (strServerResponse.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                        DataBase db = new DataBase(ctx);
                        db.open();
                        ContentValues cv = new ContentValues();
                        cv.put("photo", photo);
                        db.update(DataBase.asset_table, DataBase.asset_int, "aoAsset_id='" + id + "'", cv);
                        db.close();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (objClientAsset.getPhoto() == null || objClientAsset.getPhoto().length() <= 0) {
                    objClientAsset.setIsImageLoaded(setPostExecutionPhotoToImageView(photo, img, strServerResponse, imgClick, noPic));
                }
            }
        }.execute();
    }


    public void loadAdminUserEmployeePhotoById(final CircleImageView img, final ClientAdminUserEmployee objClientAdminUserEmployee, final View.OnClickListener imgClick, final int noPic) {
        new AsyncTask() {
            String photo = "";
            String strServerResponse = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setPreExecutionPhotoToImageView(ctx, objClientAdminUserEmployee.getPhoto(), img, imgClick, noPic);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (objClientAdminUserEmployee.getPhoto() == null || objClientAdminUserEmployee.getPhoto().length() <= 0) {
                    String id = objClientAdminUserEmployee.getEmpId();
                    final HttpEngine objHttpEngine = new HttpEngine();
                    final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
                    String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                    final URLMapping um = ConstantVal.loadPhoto(ctx);
                    ServerResponse sr = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(), new String[]{String.valueOf(tokenId), String.valueOf(id), String.valueOf(3), account_id}, um.getParamNames(), um.isNeedToSync());
                    strServerResponse = sr.getResponseCode();
                    parseCommonData objparseCommonData = new parseCommonData();
                    photo = objparseCommonData.parsePhoto(sr.getResponseString());
                    //update item_master table
                    if (strServerResponse.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                        DataBase db = new DataBase(ctx);
                        db.open();
                        ContentValues cv = new ContentValues();
                        cv.put("photo", photo);
                        db.update(DataBase.adminuser_employee_table, DataBase.adminuser_employee_int, "empId='" + id + "'", cv);
                        db.close();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (objClientAdminUserEmployee.getPhoto() == null || objClientAdminUserEmployee.getPhoto().length() <= 0) {
                    objClientAdminUserEmployee.setIsImageLoaded(setPostExecutionPhotoToImageView(photo, img, strServerResponse, imgClick, noPic));
                }
            }
        }.execute();
    }

    public static void setPreExecutionPhotoToImageView(Context ctx, String strBase64, CircleImageView img, View.OnClickListener imgClick, int noPic) {
        try {
            int pixel = Helper.getPixelByPercentageOfScreenHeight(30, ctx);
            Bitmap bmpNoPic = Helper.getResizedBitmap(BitmapFactory.decodeResource(ctx.getResources(), noPic), pixel, pixel);
            RelativeLayout.LayoutParams rparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rparam.addRule(RelativeLayout.CENTER_IN_PARENT);
            img.setLayoutParams(rparam);
            if (ctx.getClass() == acHome.class) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, pixel / 2, 0, 0);
                acHome.lyUserDetail.setLayoutParams(params);
                acHome.lyUserDetail.setPadding(0, pixel / 2, 0, 0);
            } else if (ctx.getClass() == acAssetDetail.class) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, pixel / 2, 0, (int) ctx.getResources().getDimension(R.dimen.ViewMargin));
                acAssetDetail.lyAssetDetail.setLayoutParams(params);
                acAssetDetail.lyAssetDetail.setPadding((int) ctx.getResources().getDimension(R.dimen.ViewPadding), pixel / 2, (int) ctx.getResources().getDimension(R.dimen.ViewPadding), (int) ctx.getResources().getDimension(R.dimen.ViewPadding));
            } else if (ctx.getClass() == acItemDetail.class) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, pixel / 2, 0, (int) ctx.getResources().getDimension(R.dimen.ViewMargin));
                acItemDetail.lyItemDetail.setLayoutParams(params);
                acItemDetail.lyItemDetail.setPadding((int) ctx.getResources().getDimension(R.dimen.ViewPadding), pixel / 2, (int) ctx.getResources().getDimension(R.dimen.ViewPadding), (int) ctx.getResources().getDimension(R.dimen.ViewPadding));
            }
            if (strBase64 != null && strBase64.length() > 0) {
                strBase64 = strBase64.substring(strBase64.indexOf(",") + 1);
                Bitmap bmp = Helper.convertBase64ImageToBitmap(strBase64);
                if (bmp == null) {
                    img.setImageBitmap(bmpNoPic);
                    img.setBackgroundResource(0);
                } else {
                    Drawable color = new ColorDrawable(ctx.getResources().getColor(R.color.white));
                    Drawable image;
                    if (ctx.getClass() == acHome.class || ctx.getClass() == acAssetDetail.class || ctx.getClass() == acItemDetail.class) {
                        image = new BitmapDrawable(ctx.getResources(), Helper.getResizedBitmap(bmp, pixel, pixel));
                    } else {
                        image = new BitmapDrawable(ctx.getResources(), bmp);
                    }
                    try {
                        LayerDrawable ld = new LayerDrawable(new Drawable[]{color, image});
                        img.setImageDrawable(ld);
                        img.setScaleType(ImageView.ScaleType.FIT_START);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.writeToCrashlytics(e);
                    }
                    if (imgClick != null) {
                        img.setTag(bmp);
                        img.setOnClickListener(imgClick);
                    }
                }
            } else {
                img.setBackgroundResource(0);
                img.setImageBitmap(Helper.getResizedBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_awaiting), pixel, pixel));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        }
    }

    private boolean setPostExecutionPhotoToImageView(String strBase64, CircleImageView img, String serverResponse, View.OnClickListener imgClick, int noPic) {
        int pixel = Helper.getPixelByPercentageOfScreenHeight(30, ctx);
        Bitmap bmpNoPic = Helper.getResizedBitmap(BitmapFactory.decodeResource(ctx.getResources(), noPic), pixel, pixel);
        RelativeLayout.LayoutParams rparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rparam.addRule(RelativeLayout.CENTER_IN_PARENT);
        img.setLayoutParams(rparam);
        try {
            if (strBase64.length() > 0) {
                strBase64 = strBase64.substring(strBase64.indexOf(",") + 1);
            }
            Bitmap bmp = Helper.convertBase64ImageToBitmap(strBase64);
            if (ctx.getClass() == acHome.class) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, pixel / 2, 0, 0);
                acHome.lyUserDetail.setLayoutParams(params);
                acHome.lyUserDetail.setPadding(0, pixel / 2, 0, 0);
            } else if (ctx.getClass() == acAssetDetail.class) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, pixel / 2, 0, (int) ctx.getResources().getDimension(R.dimen.ViewMargin));
                acAssetDetail.lyAssetDetail.setLayoutParams(params);
                acAssetDetail.lyAssetDetail.setPadding((int) ctx.getResources().getDimension(R.dimen.ViewPadding), pixel / 2, (int) ctx.getResources().getDimension(R.dimen.ViewPadding), (int) ctx.getResources().getDimension(R.dimen.ViewPadding));
            } else if (ctx.getClass() == acItemDetail.class) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, pixel / 2, 0, (int) ctx.getResources().getDimension(R.dimen.ViewMargin));
                acItemDetail.lyItemDetail.setLayoutParams(params);
                acItemDetail.lyItemDetail.setPadding((int) ctx.getResources().getDimension(R.dimen.ViewPadding), pixel / 2, (int) ctx.getResources().getDimension(R.dimen.ViewPadding), (int) ctx.getResources().getDimension(R.dimen.ViewPadding));
            }

            if (strBase64.equals("") || bmp == null) {
                if (serverResponse.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                    img.setBackgroundResource(0);
                    img.setImageBitmap(bmpNoPic);
                }
            } else {
                Drawable color = new ColorDrawable(ctx.getResources().getColor(R.color.white));
                Drawable image;
                if (ctx.getClass() == acHome.class || ctx.getClass() == acAssetDetail.class || ctx.getClass() == acItemDetail.class) {
                    image = new BitmapDrawable(ctx.getResources(), Helper.getResizedBitmap(bmp, pixel, pixel));
                } else {
                    image = new BitmapDrawable(ctx.getResources(), bmp);
                }
                try {
                    LayerDrawable ld = new LayerDrawable(new Drawable[]{color, image});
                    img.setImageDrawable(ld);
                    img.setScaleType(ImageView.ScaleType.FIT_START);
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.writeToCrashlytics(e);
                }
                if (imgClick != null) {
                    img.setTag(bmp);
                    img.setOnClickListener(imgClick);
                }
            }
            return true;
        } catch (Exception e) {
            Logger.writeToCrashlytics(e);
            img.setBackgroundResource(0);
            img.setImageBitmap(bmpNoPic);
            return false;
        }
    }

    public static void setPreExecutionPhotoToImageView(Context ctx, String strBase64, ImageView img, View.OnClickListener imgClick) {
        RelativeLayout.LayoutParams rparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rparam.addRule(RelativeLayout.CENTER_IN_PARENT);
        img.setLayoutParams(rparam);
        if (strBase64 != null && strBase64.length() > 0) {
            strBase64 = strBase64.substring(strBase64.indexOf(",") + 1);
            Bitmap bmp = Helper.convertBase64ImageToBitmap(strBase64);
            if (bmp == null) {
                img.setImageResource(R.drawable.ic_nopic);
                img.setBackgroundResource(0);
            } else {
                img.setImageResource(0);
                img.setBackgroundDrawable(new BitmapDrawable(ctx.getResources(), bmp));
                try {
                    img.setScaleType(ImageView.ScaleType.FIT_START);
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.writeToCrashlytics(e);
                }
                if (imgClick != null) {
                    img.setTag(bmp);
                    img.setOnClickListener(imgClick);
                }
            }
        } else {
            img.setBackgroundResource(0);
            img.setImageResource(R.drawable.ic_awaiting);
        }
    }

    private boolean setPostExecutionPhotoToImageView(String strBase64, ImageView img, String serverResponse, View.OnClickListener imgClick) {
        RelativeLayout.LayoutParams rparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rparam.addRule(RelativeLayout.CENTER_IN_PARENT);
        img.setLayoutParams(rparam);
        try {
            if (strBase64.length() > 0) {
                strBase64 = strBase64.substring(strBase64.indexOf(",") + 1);
            }
            try {
                img.setScaleType(ImageView.ScaleType.FIT_START);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.writeToCrashlytics(e);
            }
            Bitmap bmp = Helper.convertBase64ImageToBitmap(strBase64);
            if (strBase64.equals("") || bmp == null) {
                if (serverResponse.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                    img.setBackgroundResource(0);
                    img.setImageResource(R.drawable.ic_nopic);
                }
            } else {
                img.setImageResource(0);
                img.setBackgroundDrawable(new BitmapDrawable(ctx.getResources(), bmp));
                if (imgClick != null) {
                    img.setTag(bmp);
                    img.setOnClickListener(imgClick);
                }
            }
            return true;
        } catch (Exception e) {
            Logger.writeToCrashlytics(e);
            img.setBackgroundResource(0);
            img.setImageResource(R.drawable.ic_nopic);
            return false;
        }
    }

    public void loadItemPhotoById(final ImageView img, final ClientItemMaster1 objClientItemMaster, final View.OnClickListener imgClick) {
        new AsyncTask() {
            String photo = "";
            String strServerResponse = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setPreExecutionPhotoToImageView(ctx, objClientItemMaster.getPhoto(), img, imgClick);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                String id = objClientItemMaster.getUuid();
                final HttpEngine objHttpEngine = new HttpEngine();
                final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
                String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                final URLMapping um = ConstantVal.loadPhoto(ctx);
                ServerResponse sr = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(), new String[]{String.valueOf(tokenId), String.valueOf(id), String.valueOf(0), account_id}, um.getParamNames(), um.isNeedToSync());
                strServerResponse = sr.getResponseCode();
                parseCommonData objparseCommonData = new parseCommonData();
                photo = objparseCommonData.parsePhoto(sr.getResponseString());
                //update item_master table
                if (strServerResponse.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                    DataBase db = new DataBase(ctx);
                    db.open();
                    ContentValues cv = new ContentValues();
                    cv.put("photo", photo);
                    db.update(DataBase.item_master_table, DataBase.item_master_int, "uuid='" + id + "'", cv);
                    db.close();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                objClientItemMaster.setIsImageLoaded(setPostExecutionPhotoToImageView(photo, img, strServerResponse, imgClick));
            }
        }.execute();
    }
}
