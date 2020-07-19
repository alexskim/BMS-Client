package cc.asjks.bms.client.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import cc.asjks.bms.client.R;



public class UtilDialog {
    public static Dialog dialog;
    public static void showDialog(Context context){
        dialog=new Dialog(context,R.style.dialog);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    public static void disDialog(){
        dialog.dismiss();
    }
}
