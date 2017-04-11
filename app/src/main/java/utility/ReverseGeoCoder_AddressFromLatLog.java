package utility;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ReverseGeoCoder_AddressFromLatLog {
    public static String[] getAddress(Context ctx, double dblLat, double dblLong) {
        List<Address> addresses = null;
        String address = "";
        String knownName = "";
        String city = "";
        String state = "";
        String country = "";
        String postalCode = "";
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(dblLat, dblLong, 1);
        } catch (NumberFormatException e1) {
            e1.printStackTrace();
            Logger.writeToCrashlytics(e1);
        } catch (IOException e1) {
            e1.printStackTrace();
            Logger.writeToCrashlytics(e1);
        }

        boolean isGeoCodeWorking = false;
        if (geocoder.isPresent()) {
            if (addresses != null) {
                if (addresses.size() > 0) {
                    address = addresses.get(0).getAddressLine(0);
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    country = addresses.get(0).getCountryName();
                    postalCode = addresses.get(0).getPostalCode();
                    knownName = addresses.get(0).getFeatureName();
                    Logger.debug("geocode working" + address + " " + city + " " + state + " " + country + " " + postalCode + " " + knownName);
                    if (postalCode != null) {
                        isGeoCodeWorking = true;
                    }
                }
            }
        }
        if (isGeoCodeWorking == false) {
            String[] geoResult = ReverseGeoCoder_AddressFromLatLog.getAddressFormURL(ctx, String.valueOf(dblLat), String.valueOf(dblLong));
            address = geoResult[0];
            city = geoResult[1];
            state = geoResult[2];
            country = geoResult[3];
            postalCode = geoResult[4];
            knownName = geoResult[5];
            Logger.debug("geocode not working working" + address + " " + city + " " + state + " " + country + " " + postalCode + " " + knownName);
        }
        return new String[]{address, city, state, country, postalCode, knownName};
    }

    public static String[] getAddressFormURL(Context ctx, String strLat, String strLong) {
        String address1 = "";
        String address2 = "";
        String city = "";
        String state = "";
        String country = "";
        String county = "";
        String PIN = "";

        try {

            JSONObject jsonObj = getJSONfromURL(ctx, "http://maps.googleapis.com/maps/api/geocode/json?latlng="
                    + strLat + "," + strLong + "&sensor=true");
            String Status = jsonObj.getString("status");
            if (Status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero
                        .getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);

                    if (TextUtils.isEmpty(long_name) == false
                            || !long_name.equals(null)
                            || long_name.length() > 0 || long_name != "") {
                        if (Type.equalsIgnoreCase("street_number")) {
                            address1 = long_name + " ";
                        } else if (Type.equalsIgnoreCase("route")) {
                            address1 = address1 + long_name;
                        } else if (Type.equalsIgnoreCase("sublocality")) {
                            address2 = long_name;
                        } else if (Type.equalsIgnoreCase("locality")) {
                            // Address2 = Address2 + long_name + ", ";
                            city = long_name;
                        } else if (Type
                                .equalsIgnoreCase("administrative_area_level_2")) {
                            county = long_name;
                        } else if (Type
                                .equalsIgnoreCase("administrative_area_level_1")) {
                            state = long_name;
                        } else if (Type.equalsIgnoreCase("country")) {
                            country = long_name;
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            PIN = long_name;
                        }

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        }

        String[] strAddress = new String[6];
        strAddress[0] = address1;
        strAddress[1] = city;
        strAddress[2] = state;
        strAddress[3] = country;
        strAddress[4] = PIN;
        strAddress[5] = address2;

        return strAddress;

    }

    public static JSONObject getJSONfromURL(Context ctx, String url) {
        HttpEngine objHttpEngine = new HttpEngine();
        String result = objHttpEngine.getDataFromWebAPI(ctx, url, new String[]{}, new String[]{}, false).getResponseString();
        JSONObject jObject = null;
        /*// initialize
        InputStream is = null;
        String result = "";
        JSONObject jObject = null;

        // http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        // convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }*/

        // try parse the string to a JSON object
        try {
            jObject = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
            Logger.writeToCrashlytics(e);
        }

        return jObject;
    }
}
