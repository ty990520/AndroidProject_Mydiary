package com.cookandroid.mydiary1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class GridviewEdit extends AppCompatActivity {
    TextView date, dietGroup,dietTime,dietIntake, dietMemo;
    ImageView iv;
    ImageView context_menu, cancle;
    int delete_flag=0;
    String dietid;
    RatingBar dietScore;
    String imageName="";
    String rate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview_edit);

        Intent intent = getIntent();
        dietid = intent.getStringExtra("DietId");

        date = findViewById(R.id.textView25);
        dietGroup = findViewById(R.id.textView26);
        dietScore = findViewById(R.id.rating);
        dietTime = findViewById(R.id.textView27);
        dietIntake = findViewById(R.id.textView28);
        dietMemo = findViewById(R.id.textView29);
        cancle = findViewById(R.id.button11);
        context_menu = findViewById(R.id.button12);
        iv = findViewById(R.id.imageView5);

        registerForContextMenu(context_menu);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            String rst = new Task().execute(/*bodyId*/dietid).get();
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
                msg1 = json.getString("dietScore");
                msg2 = json.getString("dietIntake");
                msg3 = json.getString("dietDate");
                msg4 = json.getString("dietId");
                msg5 = json.getString("dietTime");
                msg6 = json.getString("dietGroup");
                msg7 = json.getString("dietMemo");
                msg8 = json.getString("dietImg");
                msg9 = json.getString("memberId");
                imageName = msg8;
                rate=msg1;

                String url = "http://107.22.137.189:8080/mobile/images/"+msg8;
                ImageLoadTask task = new ImageLoadTask(url, iv);
                task.execute();

                //total += (msg1 + ":" + msg2 + ":" + msg3 + ":" + msg4 + ":" + msg5 + ":" + msg6 + ":" + msg7 + ":" + msg8 + "\n");
                date.setText(msg3.substring(0,10));
                dietGroup.setText(msg6);
                dietScore.setRating(Float.parseFloat(msg1));
                dietTime.setText("?????? "+msg5);
                dietIntake.setText("|    "+msg2);
                dietMemo.setText(msg7);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class Task extends AsyncTask<String, Void, String> {    //????????????	//doInBackground??? ??????
        String sendMsg, receiveMsg;
        String select_serverIp = "http://107.22.137.189:8080/mobile/diet/selectByDietId.jsp"; // ????????? jsp??????
        String delete_serverIp = "http://107.22.137.189:8080/mobile/diet/dietdelete.jsp"; // ????????? jsp??????

        @Override
        protected String doInBackground(String... strings) {    //?????? ??????
            try {
                URL url = null;
                if(delete_flag==1){
                    url = new URL(delete_serverIp);
                    delete_flag=0;
                }
                else{
                    url = new URL(select_serverIp);
                }
                String str;
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");    //????????? ??????
                conn.setRequestMethod("POST");    //??? ????????? ??????
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "dietId=" + strings[0];    //get????????? ????????????????????? post????????? (get???????????? ???????????? ?????? ?????? ???????????? ?????????????????? ????????? post???????????? ????????? ???)

                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {    //?????? ??????
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");    //utf8??? ????????? ????????????
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);    //?????? ????????? ??????
                    }
                    receiveMsg = buffer.toString();
                } else {
                    Log.i("?????? ??????", conn.getResponseCode() + "??????");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;    //string????????? json ????????? ??????
        }
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String urlStr;
        private ImageView imageView;
        private HashMap<String, Bitmap> hashMap = new HashMap<>();

        // ?????? url ??? ????????? ???, ????????? ?????? ??? ?????? ??????????????? ????????? ??? ????????????
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
            // ???????????? ????????? ???????????? ?????? ????????? ????????? ?????????.
            Bitmap bitmap = null;
            try {
                // ???????????? ???????????? ??? ???????????? ????????? ???????????? ?????? ???????????????.
                // ?????? ???????????? ???????????? ?????? ???????????? ??????????????? ????????? ????????? ??? ????????????
                // ???????????? ?????? ????????? ????????? recycle() ???????????? ????????? ?????? ???????????????.
                if(hashMap.containsKey(urlStr)){  // ?????? ????????? ??????????????? ???????????? ??????
                    Bitmap oldBitmap = hashMap.remove(urlStr);
                    if(oldBitmap != null){
                        oldBitmap.recycle();    // ???????????? ???????????? ???????????? ??????
                        oldBitmap = null;
                    }
                }

                URL url = new URL(urlStr);
                // ????????? ???????????? ????????? ???????????? ??????, decodeStream ??? ?????? ??????????????? ??????
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                hashMap.put(urlStr, bitmap); // ??? ???????????? ??????
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
            imageView.setImageBitmap(bitmap);   // ???????????? ??????????????? ??????
            imageView.invalidate(); // ???????????? ?????? ??????
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.updateBtn:
                Intent intent2 = new Intent(getApplicationContext(), InputDiet.class);
                intent2.putExtra("dietId",dietid);
                intent2.putExtra("dietScore",rate);
                intent2.putExtra("dietGroup",dietGroup.getText().toString());
                intent2.putExtra("dietIntake",dietIntake.getText().toString());
                intent2.putExtra("dietTime",dietTime.getText().toString());
                intent2.putExtra("dietMemo",dietMemo.getText().toString());
                intent2.putExtra("dietImg",imageName);
                startActivityForResult(intent2,0);
                return true;
            case R.id.deleteBtn:
                //
                try {
                    delete_flag = 1;
                    String rst = new Task().execute(/*bodyId*/dietid).get();
                    Toast.makeText(getApplicationContext(),"????????? ?????????????????????.",Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(),rst,Toast.LENGTH_LONG).show();
                    finish();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
        }
        return super.onContextItemSelected(item);
    }
}