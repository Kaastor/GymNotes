package pl.edu.wat.gymnotes.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.wat.gymnotes.activities.DiaryDetailsActivity;


public class DiaryCalendarDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private Logger logger = Logger.getLogger(DiaryCalendarDialog.class.toString());

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        logger.log(Level.INFO, "onCreateDialog");

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        month++;
        String data;
        if(month < 10)
            data = day + "-0" + month + "-" + year;
        else
            data = day + "-" + month + "-" + year;
        Intent intent = new Intent(getActivity(), DiaryDetailsActivity.class)
                .putExtra("data", data);
        startActivity(intent);
        logger.log(Level.INFO, "Send intetnt to DiaryDetailsActivity. Chosen date: " + data);

    }

}
