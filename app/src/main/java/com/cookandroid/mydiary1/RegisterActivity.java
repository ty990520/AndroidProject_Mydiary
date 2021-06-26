package com.cookandroid.mydiary1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    EditText et_id, et_pass, et_passCheck;
    TextView tv_age, tv;
    Button btn_register;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toast.makeText(getApplicationContext(), "회원가입", Toast.LENGTH_SHORT).show();

        et_id = findViewById(R.id.et_id2);
        et_pass = findViewById(R.id.et_pass2);
        et_passCheck = findViewById(R.id.et_passCheck);
        //.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //et_passCheck.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        tv_age = findViewById(R.id.tv_age);
        radioGroup = findViewById(R.id.radioGroup);

        tv_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = (View) View.inflate(RegisterActivity.this, R.layout.fragment_age, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(RegisterActivity.this);
                dlg.setView(dialogView);

                NumberPicker np1 = (NumberPicker) dialogView.findViewById(R.id.numberPicker9);
                NumberPicker np2 = (NumberPicker) dialogView.findViewById(R.id.numberPicker10);
                np1.setMinValue(0);
                np1.setMaxValue(9);
                np2.setMinValue(0);
                np2.setMaxValue(9);
                np1.setWrapSelectorWheel(false);
                np2.setWrapSelectorWheel(false);
                np1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                np2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


                final int[] first = new int[1];
                final int[] second = new int[1];
                np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        first[0] = newVal;
                    }
                });
                np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        second[0] = newVal;
                    }
                });


                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_age.setText(first[0] * 10 + second[0]+ "");
                    }
                });

                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

        btn_register = findViewById(R.id.btn_register2);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = et_id.getText().toString().trim();
                String pw = et_pass.getText().toString().trim();
                String pwCh = et_passCheck.getText().toString().trim();
                String age = tv_age.getText().toString().trim();
                String gender=String.valueOf(radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId())));
                //Toast.makeText(getApplicationContext(), age+":"+gender, Toast.LENGTH_SHORT).show();
                //tv.append(age+":"+gender);
                try {
                    String rst = new Task().execute(id, pw, pwCh, age, gender).get();
                    JSONObject json = new JSONObject(rst);
                    JSONArray jArr = json.getJSONArray("List");

                    int check=0;
                    for (int i = 0; i < jArr.length(); i++) {
                        json = jArr.getJSONObject(i);
                        check = json.getInt("check");
                    }
                    if(check==1) {
                        Toast.makeText(getApplicationContext(), "회원가입 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(check==2) {
                        Toast.makeText(getApplicationContext(), "5글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(check==3) {
                        Toast.makeText(getApplicationContext(), "영문 대문자, 소문자, 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(check==4) {
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(check==5){
                        Toast.makeText(getApplicationContext(), "중복된 아이디가 있습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(check==6){
                        Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //    public class Task extends AsyncTask<String, String, String> {
//        String sendMsg, receiveMsg;
//        String serverIp = "http://54.227.89.25:8080/mobile/member/join.jsp";
//
//        @Override
//        protected String doInBackground(String... strings) { // string 3개 받음
//            try {
//                String str;
//                URL url = new URL(serverIp);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                conn.setRequestMethod("POST");
//                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
//
//                sendMsg = "memberId="+strings[0]+"&password="+strings[1];
//                osw.write(sendMsg);
//                osw.flush();
//                if(conn.getResponseCode() == conn.HTTP_OK) {
//                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
//                    BufferedReader reader = new BufferedReader(tmp);
//                    StringBuffer buffer = new StringBuffer();
//                    while ((str = reader.readLine()) != null) {
//                        buffer.append(str);
//                    }
//                    receiveMsg = buffer.toString();
//                    publishProgress(receiveMsg);
//                } else {
//                    Log.i("통신 결과", conn.getResponseCode()+"에러");
//                }
//
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return receiveMsg;
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values); // 프로그레스 되는 게 있으면 values로 보내줌
//            //tv.setText(values[0]);
//        }
//    }
    public class Task extends AsyncTask<String, Void, String> {
        //public  String ip ="220.149.119.161:8080"; //자신의 IP번호
        String sendMsg, receiveMsg;
        //        String serverIp = "http://54.227.89.25:8080/mobile/member/selectByMemberId.jsp"; // 연결할 jsp주소
        String serverIp = "http://54.227.89.25:8080/mobile/member/signup.jsp"; // 연결할 jsp주소

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(serverIp);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "memberId="+strings[0]+"&password="+strings[1]+"&passwordCheck="+strings[2]+"&age="+strings[3]+"&gender="+strings[4];
//                sendMsg = "memberId="+strings[0];

                osw.write(sendMsg);
                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }
}