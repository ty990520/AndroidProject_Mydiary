package com.cookandroid.mydiary1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class InputWater extends AppCompatActivity {

    Button btn5, btn6, btn9, btn10;
    EditText editText9, editText10;
    int et10 = 0;
    int insert_flag = 1, update_flag = 0, selectById_flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_water);

        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);

        btn9 = findViewById(R.id.button9);
        btn10 = findViewById(R.id.button10);

        editText9 = findViewById(R.id.editText9);
        editText10 = findViewById(R.id.editText10);

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //if()
        //editText10.setText();

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //et10 = Integer.parseInt(editText10.toString());
                et10 = Integer.parseInt(editText10.getText().toString());
                if (et10 > 0) {
                    et10 -= 100;
                }
                editText10.setText(et10 + "");
            }
        });

        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et10 = Integer.parseInt(editText10.getText().toString());
                et10 += 100;
                editText10.setText(et10 + "");
            }
        });

        //저장하기
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String waterTarget = editText9.getText().toString();
                String waterIntake = editText10.getText().toString();
                String memberId = "leehw";  /*아이디 변경*/
                try {
                    String rst = new Task().execute(waterTarget, waterIntake, memberId).get();
                    //Toast.makeText(getApplicationContext(), "성공적으로 저장했습니다!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), rst, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

    }

    public class Task extends AsyncTask<String, String, String> {
        String sendMsg, receiveMsg;
        //String serverIp = "http://107.22.137.189:8080/mobile/water/waterInsert.jsp";

        @Override
        protected String doInBackground(String... strings) {    //string가 위에서 보낸 (bookName,author,price)을 받음
            try {
                if (insert_flag == 1) {
                    String serverIp = "http://107.22.137.189:8080/mobile/water/waterInsert.jsp";
                    String str;
                    URL url = new URL(serverIp);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestMethod("POST");
                    OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                    sendMsg = "waterTarget=" + strings[0] + "&waterIntake=" + strings[1] + "&memberId=" + strings[2];
                    osw.write(sendMsg);
                    osw.flush();
                    if (conn.getResponseCode() == conn.HTTP_OK) {
                        InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        BufferedReader reader = new BufferedReader(tmp);
                        StringBuffer buffer = new StringBuffer();
                        while ((str = reader.readLine()) != null) {
                            buffer.append(str);
                        }
                        receiveMsg = buffer.toString();
                        publishProgress(receiveMsg);
                        insert_flag = 0;
                    } else {
                        Log.i("통신 결과", conn.getResponseCode() + "에러");
                    }
                } else if (update_flag == 1) {
                    String serverIp = "http://107.22.137.189:8080/mobile/water/waterUpdate.jsp";
                    String str;
                    URL url = new URL(serverIp);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestMethod("POST");
                    OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                    sendMsg = "waterId=" + strings[0] +"waterTarget=" + strings[1] + "&waterIntake=" + strings[2] + "&memberId=" + strings[3];
                    osw.write(sendMsg);
                    osw.flush();
                    if (conn.getResponseCode() == conn.HTTP_OK) {
                        InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        BufferedReader reader = new BufferedReader(tmp);
                        StringBuffer buffer = new StringBuffer();
                        while ((str = reader.readLine()) != null) {
                            buffer.append(str);
                        }
                        receiveMsg = buffer.toString();
                        publishProgress(receiveMsg);
                        insert_flag = 0;
                    } else {
                        Log.i("통신 결과", conn.getResponseCode() + "에러");
                    }
                    update_flag = 0;
                } else if (selectById_flag == 1) {
                    String serverIp = "http://107.22.137.189:8080/mobile/water/selectByWaterId.jsp";
                    String str;
                    URL url = new URL(serverIp);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");    //커넥션 생성
                    conn.setRequestMethod("POST");    //웹 방식과 다름
                    OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                    sendMsg = "waterId=" + strings[0];    //get방식과 유사해보이지만 post방식임 (get방식으로 넘겨주게 되면 한글 처리까지 고려해야해서 대부분 post방식으로 사용할 것)

                    osw.write(sendMsg);
                    osw.flush();
                    if (conn.getResponseCode() == conn.HTTP_OK) {    //결과 리턴
                        InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");    //utf8로 데이터 읽어오기
                        BufferedReader reader = new BufferedReader(tmp);
                        StringBuffer buffer = new StringBuffer();
                        while ((str = reader.readLine()) != null) {
                            buffer.append(str);    //내용 버퍼에 저장
                        }
                        receiveMsg = buffer.toString();
                    } else {
                        Log.i("통신 결과", conn.getResponseCode() + "에러");
                    }
                    selectById_flag = 0;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getApplicationContext(), values[0], Toast.LENGTH_SHORT).show();
        }
    }
}