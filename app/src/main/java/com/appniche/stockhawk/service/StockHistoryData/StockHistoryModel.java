package com.appniche.stockhawk.service.StockHistoryData;

import java.util.List;

/**
 * Created by JayPrakash on 19-08-2016.
 */

public class StockHistoryModel {

    private String sName;
    private String sFirstTrade;
    private String sLastTrade;
    private String sCurrency;
    private String sBidPrice;
    private List<StockHistoryGraph> historyGraphs;

    public StockHistoryModel(String sName, String sFirstTrade, String sLastTrade, String sCurrency, String sBidPrice, List<StockHistoryGraph> historyGraphs) {
        this.sName = sName;
        this.sFirstTrade = sFirstTrade;
        this.sLastTrade = sLastTrade;
        this.sCurrency = sCurrency;
        this.sBidPrice = sBidPrice;
        this.historyGraphs = historyGraphs;
    }

    public String getsName() {
        return sName;
    }

    public String getsFirstTrade() {
        return sFirstTrade;
    }

    public String getsLastTrade() {
        return sLastTrade;
    }

    public String getsCurrency() {
        return sCurrency;
    }

    public String getsBidPrice() {
        return sBidPrice;
    }

    public List<StockHistoryGraph> getHistoryGraphs() {
        return historyGraphs;
    }
}
