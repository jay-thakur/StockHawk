package com.appniche.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.appniche.stockhawk.R;
import com.appniche.stockhawk.service.StockTaskService;
import com.appniche.stockhawk.ui.MyStocksActivity;
import com.appniche.stockhawk.ui.StockDetailActivity;

public class StockWidgetProvider extends AppWidgetProvider {

    /*private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

    }*/

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_widget);

            Intent adapterIntent = new Intent(context, StockWidgetService.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, adapterIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            views.setRemoteAdapter(R.id.stock_list, adapterIntent);

            Intent clickIntent = new Intent(context, StockDetailActivity.class);
            PendingIntent clickPendingIntent = TaskStackBuilder.create(context)
                                                                .addNextIntentWithParentStack(clickIntent)
                                                                .getPendingIntent(0, pendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.stock_list, clickPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stock_list);
            //Toast.makeText(context, "widget added", Toast.LENGTH_LONG).show();
        }
    }

    /*@Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] appwidgetIds = widgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        widgetManager.notifyAppWidgetViewDataChanged(appwidgetIds, R.id.stock_list);
    }*/
}
