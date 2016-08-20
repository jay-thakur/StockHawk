package com.appniche.stockhawk.service.StockHistoryData;

/**
 * Created by JayPrakash on 20-08-2016.
 */

public class StockHistoryGraph {

    private String sDate;
    private float sClose;

    public StockHistoryGraph(String sDate, float sClose) {
        this.sDate = sDate;
        this.sClose = sClose;
    }

    public String getsDate() {
        return sDate;
    }

    public float getsClose() {
        return sClose;
    }

}
