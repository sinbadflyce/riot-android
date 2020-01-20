/*
 * Copyright 2016 OpenMarket Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.matrix.androidsdk.rest.client;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

/**
 * UrlPostTask triggers a POST with no param.
 */
public class UrlPostTask extends AsyncTask<String, Void, String> {

    private static final String LOG_TAG = "UrlPostTask";
    // the post listener
    private IPostTaskListener mListener;
    // the proxy
    @NonNull
    private Proxy mProxy;

    public UrlPostTask(@Nullable Proxy proxy) {
        if (proxy == null) {
            mProxy = Proxy.NO_PROXY;
        } else {
            mProxy = proxy;
        }
    }

    /**
     * Convert a stream to a string
     *
     * @param is the input stream to convert
     * @return the string
     */
    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "convertStreamToString " + e.getMessage(), e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                Log.e(LOG_TAG, "convertStreamToString finally failed " + e.getMessage(), e);
            }
        }
        return sb.toString();
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";

        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(mProxy);
            if (RestClient.getUserAgent() != null) {
                conn.setRequestProperty("User-Agent", RestClient.getUserAgent());
            }
            conn.setRequestMethod("POST");

            InputStream is = new BufferedInputStream(conn.getInputStream());

            if (is != null) {
                result = convertStreamToString(is);
            }
        } catch (Exception e) {
            // Do something about exceptions
            result = e.getMessage();
        }
        return result;
    }

    /**
     * Set the post listener
     *
     * @param listener the listener
     */
    public void setListener(IPostTaskListener listener) {
        mListener = listener;
    }

    protected void onPostExecute(String result) {
        JsonObject object = null;

        Log.d(LOG_TAG, "onPostExecute " + result);

        try {
            JsonParser parser = new JsonParser();
            object = parser.parse(result).getAsJsonObject();
        } catch (Exception e) {
            Log.e(LOG_TAG, "## onPostExecute() failed" + e.getMessage(), e);
        }

        if (null != mListener) {
            if (null != object) {
                mListener.onSucceed(object);
            } else {
                mListener.onError(result);
            }
        }
    }

    public interface IPostTaskListener {
        /**
         * The post succeeds.
         *
         * @param object the object retrieves in the response.
         */
        void onSucceed(JsonObject object);

        /**
         * The post failed
         *
         * @param errorMessage thr error message
         */
        void onError(String errorMessage);
    }
}