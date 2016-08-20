package com.appniche.stockhawk.service.StockHistoryData;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JayPrakash on 19-08-2016.
 */

public class StockHistoryData {

    private static final String LOG_TAG = StockHistoryData.class.getSimpleName();

    private final String BASE_URL = "http://chartapi.finance.yahoo.com/instrument/1.0/";
    private final String END_URL = "/chartdata;type=quote;range=1y/json";

    private StockHistoryModel stockHistoryModel;
    StockHistoryCallback stockHistoryCallback;
    private List<StockHistoryGraph> historyGraphs;
    private Context mContext;
    boolean status = false;

    public StockHistoryData(StockHistoryCallback stockHistoryCallback, Context mContext) {
        this.stockHistoryCallback = stockHistoryCallback;
        this.mContext = mContext;
        historyGraphs = new ArrayList<StockHistoryGraph>();
    }

    private static final String STOCK_NAME = "Company-Name";
    private static final String FIRST_TRADE = "first-trade";
    private static final String LAST_TRADE = "last-trade";
    private static final String CURRENCY = "currency";
    private static final String BID_PRICE = "previous_close_price";

    private static final String DATE = "Date";
    private static final String CLOSE = "close";

    public void getStockHistoryData(String symbol){
        final String url = BASE_URL+symbol+END_URL;
        Log.d(LOG_TAG, url);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.d("IN THE BACKGROUND", url);

                try {
                    String jsonData = fetchStockData(url);
                    Log.d(LOG_TAG, jsonData);
                    getStockHistoryDataFromJSON(jsonData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.d(LOG_TAG, "onpost execute ");
                if (status){
                    stockHistoryCallback.onSuccess(stockHistoryModel);
                }else{
                    stockHistoryCallback.onFailure();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getStockHistoryDataFromJSON(String jsonData) {
        jsonData = jsonData.substring(jsonData.indexOf("{"), jsonData.lastIndexOf("}") + 1);
        try {
            JSONObject object = new JSONObject(jsonData);
            JSONObject metaObject = object.getJSONObject("meta");
            String stockName = metaObject.getString(STOCK_NAME);
            String firstTrade = metaObject.getString(FIRST_TRADE);
            String lastTrade = metaObject.getString(LAST_TRADE);
            String currency = metaObject.getString(CURRENCY);
            String bidPrice = metaObject.getString(BID_PRICE);

            JSONArray seriesArray = object.getJSONArray("series");
            for (int i=0;i<seriesArray.length();i++){
                JSONObject seriesObject = seriesArray.getJSONObject(i);
                String date = seriesObject.getString(DATE);
                double close = seriesObject.getDouble(CLOSE);
                historyGraphs.add(new StockHistoryGraph(date, (float) close));
            }

            stockHistoryModel = new StockHistoryModel(stockName, firstTrade, lastTrade, currency, bidPrice, historyGraphs);

            if (stockHistoryCallback != null){
                status = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private String fetchStockData(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    /**
     * Interface to interact with the callee class to notify regarding success, or errors if any.
     */
    public interface StockHistoryCallback {
        void onSuccess(StockHistoryModel stockHistoryModel);

        void onFailure();
    }
}
