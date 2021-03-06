package asyncmanager;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lab360io.jobio.inventoryapp.R;
import com.lab360io.jobio.inventoryapp.acHome;

import java.util.Date;

import entity.BusinessAccountdbDetail;
import entity.ClientAdminUserEmployee;
import parser.parseCommonData;
import utility.CircleImageView;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 4/25/2016.
 */
public class asyncLoadCommonData {
    Context ctx;
    Date dtCurrentTime;

    public asyncLoadCommonData(Context ctx, Date dtCurrentTime) {
        this.ctx = ctx;
        this.dtCurrentTime = dtCurrentTime;
    }

    public asyncLoadCommonData(Context ctx) {
        this.ctx = ctx;
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
                    final int tokenId = Helper.getIntPreference(ctx, ConstantVal.TOKEN_ID, 0);
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
            }
            if (strBase64 != null && strBase64.length() > 0) {
                strBase64 = strBase64.substring(strBase64.indexOf(",") + 1);
                Bitmap bmp = Helper.convertBase64ImageToBitmap(strBase64);
                if (bmp == null) {
                    img.setImageBitmap(bmpNoPic);
                    img.setBackgroundResource(0);
                } else {
                    if (ctx.getClass() == acHome.class) {
                        img.setImageBitmap(Helper.getResizedBitmap(bmp, pixel, pixel));
                    } else {
                        img.setImageBitmap(bmp);
                    }
                    try {
                        img.setScaleType(ImageView.ScaleType.FIT_START);
                    } catch (Exception e) {

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
            }
            if (strBase64.equals("") || bmp == null) {
                if (serverResponse.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                    img.setBackgroundResource(0);
                    img.setImageBitmap(bmpNoPic);
                }
            } else {
                if (ctx.getClass() == acHome.class) {
                    img.setImageBitmap(Helper.getResizedBitmap(bmp, pixel, pixel));
                } else {
                    img.setImageBitmap(bmp);
                }
                try {
                    img.setScaleType(ImageView.ScaleType.FIT_START);
                } catch (Exception e) {

                }
                if (imgClick != null) {
                    img.setTag(bmp);
                    img.setOnClickListener(imgClick);
                }
            }
            return true;
        } catch (Exception e) {
            img.setBackgroundResource(0);
            img.setImageBitmap(bmpNoPic);
            return false;
        }
    }
}
