package com.appniche.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;
import com.appniche.stockhawk.R;
import com.appniche.stockhawk.ui.StockDetailActivity;

public class StockWidgetProvider extends AppWidgetProvider {

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

        }
    }

}
