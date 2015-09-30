package it.jaschke.alexandria;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class DisplayToast implements Runnable {
    private final Context mContext;
    String mText;

    public DisplayToast(Context mContext, String text){
        this.mContext = mContext;
        mText = text;
    }

    public void run(){
        Toast toast= Toast.makeText(mContext, mText, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

    }
}