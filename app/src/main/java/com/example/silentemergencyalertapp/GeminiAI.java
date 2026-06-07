package com.example.silentemergencyalertapp;

import okhttp3.*;
import org.json.*;

import java.io.IOException;

public class GeminiAI {

    private static final String API_KEY = "YOUR_GEMINI_API_KEY";

    private static final String URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    public interface Callback {
        void onSuccess(String text);
        void onError(String error);
    }

    public static void generateSOSMessage(String input, Callback callback) {

        OkHttpClient client = new OkHttpClient();

        try {

            // ✅ Correct Gemini format
            JSONObject textPart = new JSONObject();
            textPart.put("text",
                    "Convert this situation into a short emergency SMS. " +
                            "Keep it urgent, clear, and direct. No extra explanation.\n\n" +
                            "Situation: " + input
            );

            JSONObject part = new JSONObject();
            part.put("parts", new JSONArray().put(textPart));

            JSONObject bodyJson = new JSONObject();
            bodyJson.put("contents", new JSONArray().put(part));

            RequestBody body = RequestBody.create(
                    bodyJson.toString(),
                    MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError("Network error: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if (response.body() == null) {
                        callback.onError("Empty response");
                        return;
                    }

                    try {

                        String res = response.body().string();

                        JSONObject json = new JSONObject(res);

                        String output =
                                json.getJSONArray("candidates")
                                        .getJSONObject(0)
                                        .getJSONObject("content")
                                        .getJSONArray("parts")
                                        .getJSONObject(0)
                                        .getString("text");

                        callback.onSuccess(output);

                    } catch (Exception e) {
                        callback.onError("Parse error: " + e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            callback.onError("Request error: " + e.getMessage());
        }
    }
}