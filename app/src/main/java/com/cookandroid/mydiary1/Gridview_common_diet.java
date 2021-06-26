package com.cookandroid.mydiary1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Gridview_common_diet extends AppCompatActivity {
    TextView memId, date, dietGroup,dietTime,dietIntake, dietMemo;
    RatingBar dietScore;
    ImageView iv;
    String dietId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview_common_diet);

        Intent intent = getIntent();
        dietId = intent.getStringExtra("dietId");

        memId = findViewById(R.id.memId);
        date = findViewById(R.id.textView25);
        dietGroup = findViewById(R.id.textView26);
        dietScore = findViewById(R.id.rating);
        dietTime = findViewById(R.id.textView27);
        dietIntake = findViewById(R.id.textView28);
        dietMemo = findViewById(R.id.textView29);
        iv = findViewById(R.id.imageView5);


        //iv = findViewById(R.id.imageView5);

        try {
            String rst = new Task().execute(/*bodyId*/dietId).get();
            JSONObject json = new JSONObject(rst);
            JSONArray jArr = json.getJSONArray("List");
            String msg1 = "";
            String msg2 = "";
            String msg3 = "";
            String msg4 = "";
            String msg5 = "";
            String msg6 = "";
            String msg7 = "";
            String msg8 = "";
            String msg9 = "";
            String total = "";
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                json = jArr.getJSONObject(i);
                msg1 = json.getString("dietScore");
                msg2 = json.getString("dietIntake");
                msg3 = json.getString("dietDate");
                msg4 = json.getString("dietId");
                msg5 = json.getString("dietTime");
                msg6 = json.getString("dietGroup");
                msg7 = json.getString("dietMemo");
                msg8 = json.getString("dietImg");
                msg9 = json.getString("memberId");

                String url = "http://107.22.137.189:8080/mobile/images/"+msg8;
                ImageLoadTask task = new ImageLoadTask(url, iv);
                task.execute();

                //total += (msg1 + ":" + msg2 + ":" + msg3 + ":" + msg4 + ":" + msg5 + ":" + msg6 + ":" + msg7 + ":" + msg8 + "\n");
                date.setText(msg3.substring(0,10));
                memId.setText(msg9+"님의 글");
                dietGroup.setText(msg6);
                dietScore.setRating(Float.parseFloat(msg1));
                dietTime.setText("시간 "+msg5);
                dietIntake.setText("|    "+msg2);
                dietMemo.setText(msg7);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String urlStr;
        private ImageView imageView;
        private HashMap<String, Bitmap> hashMap = new HashMap<>();

        // 어떤 url 로 요청할 지, 응답을 받은 후 어떤 이미지뷰에 설정할 지 전달받음
        public ImageLoadTask(String urlStr, ImageView imageView){
            this.urlStr = urlStr;
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            // 웹서버의 이미지 데이터를 받아 비트맵 객체로 만든다.
            Bitmap bitmap = null;
            try {
                // 메모리에 만들어진 후 해제되지 않으면 메모리에 계속 남아있는다.
                // 여러 이미지를 로딩하게 되면 메모리가 부족해지는 문제가 발생할 수 있으므로
                // 사용하지 않는 비트맵 객체는 recycle() 메소드를 이용해 즉시 해제시킨다.
                if(hashMap.containsKey(urlStr)){  // 요청 주소가 들어있다면 비트맵을 꺼냄
                    Bitmap oldBitmap = hashMap.remove(urlStr);
                    if(oldBitmap != null){
                        oldBitmap.recycle();    // 들어왔던 비트맵을 메모리에 제거
                        oldBitmap = null;
                    }
                }

                URL url = new URL(urlStr);
                // 주소로 접속하여 이미지 스트림을 받고, decodeStream 을 통해 비트맵으로 바꿈
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                hashMap.put(urlStr, bitmap); // 새 비트맵을 넣음
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);   // 비트맵을 이미지뷰에 설정
            imageView.invalidate(); // 이미지를 다시 그림
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.context_menu, menu);
    }

    public class Task extends AsyncTask<String, Void, String> {    //리턴타입	//doInBackground의 배열
        //public  String ip ="220.149.119.161:8080"; //자신의 IP번호
        String sendMsg, receiveMsg;
        String select_serverIp = "http://107.22.137.189:8080/mobile/diet/selectByDietId.jsp"; // 연결할 jsp주소


        @Override
        protected String doInBackground(String... strings) {    //핵심 코드
            try {
                URL url = new URL(select_serverIp);
                String str;
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");    //커넥션 생성
                conn.setRequestMethod("POST");    //웹 방식과 다름
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "dietId=" + strings[0];    //get방식과 유사해보이지만 post방식임 (get방식으로 넘겨주게 되면 한글 처리까지 고려해야해서 대부분 post방식으로 사용할 것)

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
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;    //string타입의 json 내용을 리턴
        }
    }
}