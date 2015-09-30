package barqsoft.footballscores;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class FootballWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;


            @Override
            public void onCreate() {
            }


            @Override
            public void onDataSetChanged() {
                final long identityToken = Binder.clearCallingIdentity();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));
                data = getContentResolver()
                        .query(DatabaseContract
                        .scores_table.buildScoreWithDate()
                        ,null, null, new String[]{date}, null);

                Binder.restoreCallingIdentity(identityToken);
            }


            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }

            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION||data == null||!data.moveToPosition(position))
                {return null;}

                RemoteViews remoteViews = new RemoteViews(getPackageName(),
                        R.layout.widget_scores_list_item);

                remoteViews.setTextViewText(R.id.home_name, data.getString(scoresAdapter.COL_HOME));
                remoteViews.setTextViewText(R.id.away_name, data.getString(scoresAdapter.COL_AWAY));
                remoteViews.setTextViewText(R.id.data_textview, data.getString(scoresAdapter.COL_MATCHTIME));
                remoteViews.setTextViewText(R.id.score_textview, Utilies.getScores(data.getInt(scoresAdapter.COL_HOME_GOALS),
                                                                 data.getInt(scoresAdapter.COL_AWAY_GOALS)));

                //Click intent
                final Intent intentFill = new Intent();
                remoteViews.setOnClickFillInIntent(R.id.widget_scores_list_item, intentFill);

                return remoteViews;

            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_scores_list_item);
            }


            @Override
            public int getViewTypeCount() {
                return 1;
            }


            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(scoresAdapter.COL_ID);
                return position;
            }


            @Override
            public boolean hasStableIds() {
                return true;
            }


        };
    }
}
