package ftn.booking_app_team_2.bookie.dialogs;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import ftn.booking_app_team_2.bookie.R;

public class CreateReservationDialog extends Dialog{

    private Pair<Long, Long> selectedDateRange;
    private Context context;
    public CreateReservationDialog(Context context){
        super(context);
        this.context=context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_reservation);
        Button okButton = findViewById(R.id.okButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button dateButton=findViewById(R.id.iconButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void showDateRangePicker() {

        MaterialDatePicker<Pair<Long, Long>> dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select a Date Range")
                        .build();

        dateRangePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        selectedDateRange = selection;
                        displaySelectedDateRange();
                    }
                });

        dateRangePicker.show(dateRangePicker.getParentFragmentManager(), dateRangePicker.toString());

    }

    private void displaySelectedDateRange() {
        if (selectedDateRange != null) {
            Date startDate = new Date(selectedDateRange.first);
            Date endDate = new Date(selectedDateRange.second);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String formattedStartDate = dateFormat.format(startDate);
            String formattedEndDate = dateFormat.format(endDate);
            String dateRangeText = formattedStartDate + " - " + formattedEndDate;
            TextInputEditText text=findViewById(R.id.DateRangeSearchField);
            text.setText(dateRangeText);


        }
    }
}
