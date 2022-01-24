package net.prolancer.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int INITIAL_TIP_PERCENTAGE = 15;
    private static final int INITIAL_SPLIT_NUMBER = 1;

    private EditText baseAmount;
    private SeekBar seekBarTip, seekbarSplit;
    private TextView tipPercentage, tipDescription, tipAmount, totalAmount, splitNumber, splitAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baseAmount = findViewById(R.id.baseAmount);
        seekBarTip = findViewById(R.id.seekBarTip);
        tipPercentage = findViewById(R.id.tipPercentage);
        tipDescription = findViewById(R.id.tipDescription);
        tipAmount = findViewById(R.id.tipAmount);
        totalAmount = findViewById(R.id.totalAmount);
        seekbarSplit = findViewById(R.id.seekBarSplit);
        splitNumber = findViewById(R.id.splitNumber);
        splitAmount = findViewById(R.id.splitAmount);

        tipPercentage.setText(INITIAL_TIP_PERCENTAGE + "%");
        seekBarTip.setProgress(INITIAL_TIP_PERCENTAGE);
        updateTipDescription(INITIAL_TIP_PERCENTAGE);
        seekBarTip.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Log.d(TAG, "onProgressChanged : " + progress);
                tipPercentage.setText(progress + "%");
                computeTipAndTotal();
                updateTipDescription(progress);
                computeSplitAmount();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        baseAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG,"afterTextChanged : " + editable);
                computeTipAndTotal();
                computeSplitAmount();
            }
        });

        splitNumber.setText(String.valueOf(INITIAL_SPLIT_NUMBER));
        seekbarSplit.setProgress(INITIAL_SPLIT_NUMBER);
        seekbarSplit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Log.d(TAG, "onProgressChanged : " + progress);
                splitNumber.setText(String.valueOf(progress));
                computeSplitAmount();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    /**
     * Compute Tip & Total Amount
     */
    private void computeTipAndTotal() {
        // 1. Get the value of the base and tip percent
        double baseAmt = baseAmount.getText().length() > 0 ? Double.valueOf(baseAmount.getText().toString()) : 0.0;
        int tipPercent = seekBarTip.getProgress();
        // 2. Compute the tip and total
        double tipAmt = baseAmt * tipPercent / 100;
        double totalAmt = baseAmt + tipAmt;
        // 3. Update the UI
        tipAmount.setText(String.format("%.2f", tipAmt));
        totalAmount.setText(String.format("%.2f", totalAmt));
    }

    /**
     * update tip description
     * @param tipPercent
     */
    private void updateTipDescription(int tipPercent) {
        String tipDesc = "";

        if (tipPercent <= 9) {
            tipDesc = "Poor";
        } else if (tipPercent >= 10 && tipPercent <= 14) {
            tipDesc = "Acceptable";
        } else if (tipPercent >= 15 && tipPercent <= 19) {
            tipDesc = "Good";
        } else if (tipPercent >= 20 && tipPercent <= 24) {
            tipDesc = "Great";
        } else {
            tipDesc = "Amazing";
        }

        tipDescription.setText(tipDesc);

        // Update the color based on the tipPercent
        int color = (int) new ArgbEvaluator().evaluate(
            Float.valueOf(tipPercent) / seekBarTip.getMax(),
                ContextCompat.getColor(this, R.color.color_worst_tip),
                ContextCompat.getColor(this, R.color.color_best_tip)
        );
        tipDescription.setTextColor(color);
    }

    /**
     * Compute Amount per person
     */
    private void computeSplitAmount() {
        double totalAmt = totalAmount.getText().length() > 0 ? Double.valueOf(totalAmount.getText().toString()) : 0.0;
        int splitNum = seekbarSplit.getProgress();

        double splitAmt = totalAmt / splitNum;
        splitAmount.setText(String.format("%.2f", splitAmt));
    }
}