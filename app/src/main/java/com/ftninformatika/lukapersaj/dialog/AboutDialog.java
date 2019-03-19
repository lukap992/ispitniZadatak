package com.ftninformatika.lukapersaj.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.ftninformatika.lukapersaj.R;

public class AboutDialog extends AlertDialog.Builder {
    public AboutDialog(Context context) {
        super(context);

        setTitle("About");
        setMessage("this is the last aplication, do you like it?");
        setCancelable(false);

        setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


    }




    public AlertDialog prepareDialog(){
        AlertDialog dialog = create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
