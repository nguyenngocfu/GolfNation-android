package com.golf.golfnation.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Base64;
import android.util.Log;

import com.golf.golfnation.common.volley.MultipartEntity;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ImageUtils {
    public static Bitmap getCircularBitmapImage(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        squaredBitmap.recycle();
        return bitmap;
    }

    public static String getBase64BinaryData(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        Log.e("Compressed Img", Base64.encodeToString(byteArray, Base64.DEFAULT));
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap resizeBitmap(Bitmap bmp, int targetW, int targetH) {
        double xFactor = 0;
        double width = Double.valueOf(bmp.getWidth());
        double height = Double.valueOf(bmp.getHeight());
        xFactor = Math.min(targetW / width, targetH / height);
        int Nheight = (int) ((xFactor * height));
        int NWidth = (int) (xFactor * width);
        return Bitmap.createScaledBitmap(bmp, NWidth, Nheight, true);
    }

/*    public static StringRequest uploadImage(final String mode, final String id, final Bitmap bmp, final Golfnation app) {
        StringRequest sr = new StringRequest(Request.Method.POST, Constants.API.UPLOAD_IMAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Uploading", response);
                if ("user".equals(mode)) {
                    Gson gson = new Gson();
                    UploadPhotoResponse uploadPhotoResponse = gson.fromJson(response, UploadPhotoResponse.class);
                    app..setPhotoName(uploadPhotoResponse.getDetails().getPhotoName());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Uploading", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY.UPLOAD_IMAGE_MODE, mode);
                params.put(Constants.KEY.UPLOAD_IMAGE_ID, id);
                params.put(Constants.KEY.UPLOAD_IMAGE_DATA, getBase64BinaryData(bmp));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        return sr;
    }*/

    public static String uploadUserPhoto(Bitmap bmp, String userID, String name) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream); // convert Bitmap to ByteArrayOutputStream
        InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream

        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            String url = String.format(Constants.UPLOAD_USER_PROFILE__URL);
            HttpPost httppost = new HttpPost(url); // server

            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("user_id", userID);
            reqEntity.addPart("uploaded_file",
                    System.currentTimeMillis() + ".png", in);
            httppost.setEntity(reqEntity);

            Log.i("UploadImage", "request " + httppost.getRequestLine());
            HttpResponse response = null;
            try {
                response = httpclient.execute(httppost);
                return getStringFromInputStream(response.getEntity().getContent());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (response != null)
                    Log.i("UploadImage", "response " + response.getStatusLine().toString());
            } finally {

            }
        } finally {

        }

        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Bitmap base64StringToBitmap(String strBase64) {
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
    public static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static String sendPostRequest(String requestURL,
                                  HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = br.readLine();
            } else {
                response = "Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public static File persistImage(Context context, Bitmap bitmap, String name) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (Exception e) {
            Log.e("ImageUtils", "Error writing bitmap", e);
        }
        return null;
    }
}