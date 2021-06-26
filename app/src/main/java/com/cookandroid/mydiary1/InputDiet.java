package com.cookandroid.mydiary1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Rating;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class InputDiet extends AppCompatActivity {

    private final int GET_GALLERY_IMAGE = 200;
    Button btn8;
    RatingBar rating;
    TextView tv18;
    ImageView img, btn7;
    Uri selectedImageUri;
    RadioGroup radioGroup, radioGroup2;
    EditText editText;
    String picturePath; //이미지 경로
    String memberId = "";
    int update_flag = 0;
    String dietId = "", dietScore = "", dietGroup = "", dietTime = "", dietIntake = "", dietMemo = "", dietImg="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_diet);
        rating = findViewById(R.id.ratingBar);
        btn7 = findViewById(R.id.button7);
        btn8 = findViewById(R.id.button8);
        tv18 = findViewById(R.id.textView18);
        img = findViewById(R.id.imageView);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup2 = findViewById(R.id.radioGroup2);
        editText = findViewById(R.id.editText);

        Intent intent = getIntent();
        memberId = intent.getStringExtra("memberId");
        dietId = intent.getStringExtra("dietId");
        dietScore = intent.getStringExtra("dietScore");
        dietIntake = intent.getStringExtra("dietIntake");
        dietGroup = intent.getStringExtra("dietGroup");
        dietTime = intent.getStringExtra("dietTime");
        dietMemo = intent.getStringExtra("dietMemo");
        dietImg = intent.getStringExtra("dietImg");

        if (dietId != null) {
            update_flag = 1;
            rating.setRating((float) Double.parseDouble(dietScore));
            /*분류(dietGroup)*/
            if(dietGroup.equals("아침")){
                radioGroup.check(R.id.radioButton);
            }else if(dietGroup.equals("아점")){
                radioGroup.check(R.id.radioButton2);
            }else if(dietGroup.equals("점심")){
                radioGroup.check(R.id.radioButton3);
            }else if(dietGroup.equals("점저")){
                radioGroup.check(R.id.radioButton4);
            }else if(dietGroup.equals("저녁")){
                radioGroup.check(R.id.radioButton5);
            }

            /*식사량(dietIntake)*/
            dietIntake = dietIntake.substring(5,dietIntake.length());
            if(dietIntake.equals("가볍게")){
                radioGroup2.check(R.id.radioButton6);
            }else if(dietIntake.equals("적당히")){
                radioGroup2.check(R.id.radioButton7);
            }else if(dietIntake.equals("배부르게")){
                radioGroup2.check(R.id.radioButton8);
            }

            tv18.setText(dietTime.substring(3,dietTime.length()));
            editText.setText(dietMemo);
            Toast.makeText(this, dietImg, Toast.LENGTH_SHORT).show();
            String url = "http://107.22.137.189:8080/mobile/images/" + dietImg;
            ImageLoadTask image_task = new ImageLoadTask(url, img);
            image_task.execute();
            img.setEnabled(false);  //이미지 수정x

        }
        ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},MODE_PRIVATE);

        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tv18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = (View) View.inflate(InputDiet.this, R.layout.activity_exercisestarttime_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(InputDiet.this);
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

                final int[] first = new int[1];
                final int[] second = new int[1];
                /*시*/
                np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        first[0] = newVal;
                    }
                });
                /*분*/
                np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        second[0] = newVal;
                    }
                });


                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv18.setText(first[0] + "시 " + second[0] + "분");
                    }
                });


                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "저장 중입니다....", Toast.LENGTH_LONG).show();
                //이미지 서버에 저장
                String imgFileName = "";
                if (picturePath != null) {
                    new Task2().execute(picturePath);
                    Path path = Paths.get(picturePath);
                    imgFileName = path.getFileName().toString();
                }

                //Toast.makeText(getApplicationContext(), picturePath + "이미지 전송 성공", Toast.LENGTH_LONG).show();

                Float rating1 = rating.getRating();
                String dietScore = rating1.toString();
                int rId = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = findViewById(rId);
                String dietGroup = rb.getText().toString();
                int rId2 = radioGroup2.getCheckedRadioButtonId();
                RadioButton rb2 = findViewById(rId2);
                String dietIntake = rb2.getText().toString();
                String dietTime = tv18.getText().toString();
                String dietMemo = editText.getText().toString();
                //String dietImg = img.toString();

                try {
                    String rst = "";
                    if (update_flag == 1) {
                        rst = new Task().execute(dietId, dietScore, dietGroup, dietIntake, dietTime, dietMemo, dietImg).get();
                        Toast.makeText(getApplicationContext(), "게시글이 수정되었습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        rst = new Task().execute(dietScore, dietGroup, dietIntake, dietTime, dietMemo, imgFileName, memberId).get();
                        Toast.makeText(getApplicationContext(), "성공적으로 저장했습니다!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            selectedImageUri = data.getData();
            img.setImageURI(selectedImageUri);
            img.setAlpha((float) 0.99);

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
            if (cursor == null || cursor.getCount() < 1) {
                return; // no cursor or no record. DO YOUR ERROR HANDLING
            }
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            if (columnIndex < 0) // no column index
                return; // DO YOUR ERROR HANDLING
            // 선택한 파일 경로
            picturePath = cursor.getString(columnIndex);
            cursor.close();
        }


    }

    public class Task extends AsyncTask<String, String, String> {
        String sendMsg, receiveMsg;
        String serverIp = "";

        @Override
        protected String doInBackground(String... strings) {    //string가 위에서 보낸 (bookName,author,price)을 받음
            if (update_flag == 1) {
                serverIp = "http://107.22.137.189:8080/mobile/diet/dietupdate.jsp";
            } else {
                serverIp = "http://107.22.137.189:8080/mobile/diet/dietInsert.jsp";
            }
            try {
                String str;
                URL url = new URL(serverIp);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                if (update_flag == 1) {//dietId, dietScore, dietGroup, dietIntake, dietTime, dietMemo, dietImg
                    sendMsg = "dietId=" + strings[0] + "&dietScore=" + strings[1] + "&dietGroup=" + strings[2] + "&dietIntake=" + strings[3] + "&dietTime=" + strings[4] + "&dietMemo=" + strings[5] + "&dietImg=" + strings[6];

                } else {
                    sendMsg = "dietScore=" + strings[0] + "&dietGroup=" + strings[1] + "&dietIntake=" + strings[2] + "&dietTime=" + strings[3] + "&dietMemo=" + strings[4] + "&dietImg=" + strings[5] + "&memberId=" + strings[6];

                }
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
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
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
            //Toast.makeText(getApplicationContext(), values[0], Toast.LENGTH_SHORT).show();
        }
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String urlStr;
        private ImageView imageView;
        private HashMap<String, Bitmap> hashMap = new HashMap<>();

        // 어떤 url 로 요청할 지, 응답을 받은 후 어떤 이미지뷰에 설정할 지 전달받음
        public ImageLoadTask(String urlStr, ImageView imageView) {
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
                if (hashMap.containsKey(urlStr)) {  // 요청 주소가 들어있다면 비트맵을 꺼냄
                    Bitmap oldBitmap = hashMap.remove(urlStr);
                    if (oldBitmap != null) {
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
    public class Task2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String urlString = "http://107.22.137.189:8080/mobile/saveImage.jsp";
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            String line = "";
            try {
                //File sourceFile = new File(strings[0]);
                //File sourceFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pictures"+strings[0]);
                DataOutputStream dos;
                // if (!sourceFile.isFile()) {
                //     Log.e("uploadFile", "Source File not exist :" + strings[0]);
                // } else {
                FileInputStream mFileInputStream = new FileInputStream(strings[0]);
                URL connectUrl = new URL(urlString);
                // open connection
                HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", strings[0]);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + strings[0] + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                Log.e("dos", dos.toString());
                int bytesAvailable = mFileInputStream.available();
                int maxBufferSize = 1024 * 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];
                int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = mFileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                mFileInputStream.close();
                dos.flush(); // finish upload...

                if (conn.getResponseCode() == 200) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer stringBuffer = new StringBuffer();

                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                }
                mFileInputStream.close();
                dos.close();
                //  }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return line;
        }
    }

}