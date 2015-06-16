package com.htfs.utilpackage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created for AssetTracking with search on 2015.
 */
public class DialogBoxDisplay {

    /**
     * AlertDialog with message "Open Location service to enable GPS?"
     *
     * @param context
     */
    public static void showGps(final Context context) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("GPS Location Needed");
        builder.setMessage("Open Location service to enable GPS?")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * AlertDialog with message "Username/Password is wrong"
     *
     * @param context
     */
    public static void wrongUser(final Context context) {
        msgBox(context, "Username/Password is wrong");
    }

    /**
     * AlertDialog with message "Check your internet Connection"
     * or
     * AlertDialog with message "There is problem with connection,Try Again Later!!!"
     *
     * @param context
     */
    public static void noInte(final Context context) {

        String msg;

        if (!CheckInternetConnection.checkConnection(context))
            msg = "Check your internet Connection";
        else
            msg = "There is problem with connection,Try Again Later!!!";

        msgBox(context, msg);
    }

    /**
     * AlertDialog with message "Check your internet Connection"
     * or
     * AlertDialog with message "Connection error. Please check the configuration setting"
     *
     * @param context
     */
    public static void checkConnect(final Context context) {

        String msg;

        if (!CheckInternetConnection.checkConnection(context))
            msg = "Check your internet Connection";
        else
            msg = "Connection error. Please check the configuration setting";

        msgBox(context, msg);
    }

    /**
     * General alert dialog box
     * with positive button
     *
     * @param context call activity's context
     * @param msg     the message to be displayed
     */
    public static void msgBox(final Context context, String msg) {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
