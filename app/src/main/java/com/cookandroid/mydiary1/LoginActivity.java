package com.cookandroid.mydiary1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText et_id, et_pass;
    Button btn_login, btn_register;
    CheckBox checkBox;
    boolean loginData;
    private SharedPreferences data;
    String id, pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        //et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        checkBox=findViewById(R.id.checkBox);
        data = getSharedPreferences("data", MODE_PRIVATE);
        loginData = data.getBoolean("SAVE_LOGIN_DATA", false);
        id = data.getString("id", "");
        pw = data.getString("pw", "");

        if (loginData) {
            et_id.setText(id);
            et_pass.setText(pw);
            checkBox.setChecked(loginData);
        }

        btn_register = findViewById(R.id.btn_logout);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = data.edit();
                editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
                editor.putString("id", et_id.getText().toString().trim());
                editor.putString("pw", et_pass.getText().toString().trim());
                editor.apply();

                String id = et_id.getText().toString().trim();
                String pw = et_pass.getText().toString().trim();

                try {
                    String rst = new Task().execute(id, pw).get();
                    JSONObject json = new JSONObject(rst);
                    JSONArray jArr = json.getJSONArray("List");

                    int check=0;
                    for (int i = 0; i < jArr.length(); i++) {
                        json = jArr.getJSONObject(i);
                        check = json.getInt("check");
                    }

                    if(check==1) {
                        Toast.makeText(getApplicationContext(), "로그인 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(check==2) {
                        Toast.makeText(getApplicationContext(), "로그인이 되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("memberId",id);
                        intent.putExtra("password",pw);
                        startActivity(intent);
                    }
                    else if(check==3){
                        Toast.makeText(getApplicationContext(), "다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class Task extends AsyncTask<String, Void, String> {
        //public  String ip ="220.149.119.161:8080"; //자신의 IP번호
        String sendMsg, receiveMsg;
        //        String serverIp = "http://54.227.89.25:8080/mobile/member/selectByMemberId.jsp"; // 연결할 jsp주소
        String serverIp = "http://54.227.89.25:8080/mobile/member/login.jsp"; // 연결할 jsp주소

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(serverIp);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "memberId="+strings[0]+"&password="+strings[1];
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