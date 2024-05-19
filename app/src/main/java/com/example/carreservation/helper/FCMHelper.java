package com.example.carreservation.helper;

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class FCMHelper {

    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAAZU_yI24:APA91bGcSMxFm_ysrdLVqfv5nh3WIiSKK4sbdI_lqqELM7_GKjyNPwRmtzeykF6r8hBzfD4ENBy_konBVGZqYTiJjUlcziipsRx5BFBIJ_AE2Qqmstk9913RA0hmuBqvhO0ej3gd90t5";

    public static void sendNotification(String token, String title, String message) {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        JSONObject dataJson = new JSONObject();

        try {
            dataJson.put("title", title);
            dataJson.put("body", message);
            json.put("to", token);
            json.put("notification", dataJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(FCM_URL)
                .post(body)
                .addHeader("Authorization", "key=" + SERVER_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                // Response handling
                System.out.println(response.body().string());
            }
        });
    }
}
