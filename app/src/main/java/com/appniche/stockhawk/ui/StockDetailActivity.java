package com.appniche.stockhawk.ui;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appniche.stockhawk.R;
import com.appniche.stockhawk.rest.Utils;
import com.appniche.stockhawk.service.StockHistoryData.StockHistoryData;
import com.appniche.stockhawk.service.StockHistoryData.StockHistoryGraph;
import com.appniche.stockhawk.service.StockHistoryData.StockHistoryModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockDetailActivity extends AppCompatActivity implements StockHistoryData.StockHistoryCallback{

    private final String LOG_TAG = StockDetailActivity.class.getSimpleName();

    LineChart lineChart;
    String symbol;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        emptyView = (TextView) findViewById(R.id.empty_view);

        symbol = getIntent().getStringExtra("STOCK_SYMBOL");
        Log.d(LOG_TAG, symbol);

        lineChart = (LineChart) findViewById(R.id.line_graph);

        StockHistoryData stockHistoryData = new StockHistoryData(this, this);
        stockHistoryData.getStockHistoryData(symbol);

    }

    @Override
    public void onSuccess(StockHistoryModel stockHistoryModel) {

        List<StockHistoryGraph> stockHistoryGraphs = stockHistoryModel.getHistoryGraphs();

        ArrayList<String> xAxisvalues = new ArrayList<>();
        ArrayList<Entry> yAxisValues = new ArrayList<>();

        for (int i=0; i<stockHistoryGraphs.size();i++){

            double close = stockHistoryGraphs.get(i).getsClose();
            xAxisvalues.add(Utils.convertDate(stockHistoryGraphs.get(i).getsDate()));
            yAxisValues.add(new Entry((float) close, i));

        }

        LineDataSet dataSet = new LineDataSet(yAxisValues, symbol);

        // set the line to be drawn like this "- - - - - -"
        dataSet.setColor(Color.WHITE);
        dataSet.setDrawCircles(false);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setDrawFilled(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xAxisvalues,dataSets);
        data.setValueTextColor(Color.WHITE);
        data.setDrawValues(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setEnabled(true);
        yAxis.setLabelCount(5, true);
        yAxis.setTextColor(Color.WHITE);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setTextSize(12f);
        lineChart.setDescriptionColor(Color.WHITE);

        // set data
        lineChart.setData(data);
        lineChart.setDescription("1 Year Stock History");

        lineChart.setNoDataText("Fetching Data...");
        lineChart.setContentDescription("Line Chart showing historical data for "+symbol);

        callInvalidate();

        TextView stockSymbol = (TextView) findViewById(R.id.stock_symbol);
        stockSymbol.setText(symbol);
        TextView stockName = (TextView) findViewById(R.id.stock_name);
        stockName.setText(stockHistoryModel.getsName());
        TextView firstTrade = (TextView) findViewById(R.id.first_trade);
        firstTrade.setText(Utils.convertDate(stockHistoryModel.getsFirstTrade()));
        TextView lastTrade = (TextView) findViewById(R.id.last_trade);
        lastTrade.setText(Utils.convertDate(stockHistoryModel.getsLastTrade()));
        TextView currency = (TextView) findViewById(R.id.currency);
        currency.setText(stockHistoryModel.getsCurrency());
        TextView bidPrice = (TextView) findViewById(R.id.bid_price);
        bidPrice.setText(stockHistoryModel.getsBidPrice());
    }

    private void callInvalidate(){
        Handler mainThread = new Handler(Looper.getMainLooper());
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                lineChart.invalidate();
                lineChart.animateX(2000);
            }
        });
    }

    @Override
    public void onFailure() {
        emptyView.setVisibility(View.VISIBLE);
        Toast.makeText(this,"Can't show data, there is some problem", Toast.LENGTH_LONG).show();
    }
}
