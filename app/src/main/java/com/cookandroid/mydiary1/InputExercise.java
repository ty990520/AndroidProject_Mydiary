package com.cookandroid.mydiary1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class InputExercise extends AppCompatActivity {

    Button btn3, btn4;

    TextView tv19;

    EditText et7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_exercise);

        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);

        tv19 = findViewById(R.id.textView19);
        et7 = findViewById(R.id.editText7);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = (View) View.inflate(InputExercise.this, R.layout.activity_exercisestarttime_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(InputExercise.this);
                dlg.setView(dialogView);

                NumberPicker np1 = (NumberPicker) dialogView.findViewById(R.id.NumberPicker7);
                NumberPicker np2 = (NumberPicker) dialogView.findViewById(R.id.NumberPicker8);
                np1.setMinValue(00);
                np1.setMaxValue(23);
                np2.setMinValue(00);
                np2.setMaxValue(59);
                np1.setWrapSelectorWheel(false);
                np2.setWrapSelectorWheel(false);
                np1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                np2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                tv19.setText(newVal);
                            }
                        });
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

        et7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = (View) View.inflate(InputExercise.this, R.layout.activity_exercisetime_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(InputExercise.this);
                dlg.setView(dialogView);

                NumberPicker np1 = (NumberPicker) dialogView.findViewById(R.id.NumberPicker5);
                NumberPicker np2 = (NumberPicker) dialogView.findViewById(R.id.NumberPicker6);
                np1.setMinValue(0);
                np1.setMaxValue(12);
                np2.setMinValue(0);
                np2.setMaxValue(59);
                np1.setWrapSelectorWheel(false);
                np2.setWrapSelectorWheel(false);
                np1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                np2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                et7.setText(newVal);
                            }
                        });
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}