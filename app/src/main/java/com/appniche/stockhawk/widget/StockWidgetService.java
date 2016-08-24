package com.appniche.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.appniche.stockhawk.R;
import com.appniche.stockhawk.data.QuoteColumns;
import com.appniche.stockhawk.data.QuoteProvider;

/**
 * Created by JayPrakash on 21-08-2016.
 */

public class StockWidgetService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockWidgetFactory(getApplicationContext(),intent);
    }

    private class StockWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context context;
        private Cursor cursor;
        private int widgetId;

        public StockWidgetFactory(Context context, Intent intent) {
            this.context = context;
            this.widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            cursor = context.getContentResolver().query(
                    QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{
                            QuoteColumns._ID,
                            QuoteColumns.SYMBOL,
                            QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE,
                            QuoteColumns.CHANGE,
                            QuoteColumns.ISUP
                    },
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null

            );
        }

        @Override
        public void onDataSetChanged() {
            if (cursor != null){
                cursor.close();
            }

            cursor = context.getContentResolver().query(
                    QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{
                            QuoteColumns._ID,
                            QuoteColumns.SYMBOL,
                            QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE,
                            QuoteColumns.CHANGE,
                            QuoteColumns.ISUP
                    },
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null
            );

        }

        @Override
        public void onDestroy() {
            if (cursor != null)
                cursor.close();
        }

        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_item_quote);
            if (cursor.moveToPosition(position)){
                String symbol = cursor.getString(1);
                String bidPrice = cursor.getString(2);
                views.setTextViewText(R.id.stock_symbol, symbol);
                views.setTextViewText(R.id.bid_price, bidPrice);
                views.setTextViewText(R.id.change, cursor.getString(3));
                if (cursor.getInt(4) == 1) {
                    views.setInt(R.id.change, "setBackgroundResource",
                            R.drawable.percent_change_pill_green);
                } else {
                    views.setInt(R.id.change, "setBackgroundResource",
                            R.drawable.percent_change_pill_red);
                }

                Intent fillInIntent = new Intent();
                fillInIntent.putExtra("symbol", symbol);
                fillInIntent.putExtra("bidPrice", bidPrice);

                views.setOnClickFillInIntent(R.id.list_item_layout, fillInIntent);
            }
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
