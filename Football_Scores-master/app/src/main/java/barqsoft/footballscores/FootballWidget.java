package barqsoft.footballscores;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import barqsoft.footballscores.service.myFetchService;

public class FootballWidget extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.football_widget);
            setRemoteAdapter(context, remoteViews);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(intent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setPendingIntentTemplate(R.id.widget_scores_list, pendingIntent);
            remoteViews.setEmptyView(R.id.widget_scores_list, R.id.widget_blank);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);

        if(AppWidgetManager.ACTION_APPWIDGET_UPDATE	.equals(intent.getAction())){
            Intent intentSer = new Intent(context, myFetchService.class);
            context.startService(intentSer);
        }

        else if (myFetchService.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_scores_list);
        }
    }

    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_scores_list, new Intent(context,
                FootballWidgetRemoteViewsService.class));
    }

}