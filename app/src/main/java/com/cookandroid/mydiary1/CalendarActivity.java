package com.cookandroid.mydiary1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CalendarActivity extends AppCompatActivity {
    String time, kcal, menu;
    ImageView finish_btn, ok_btn;
    TextView textView_calendar;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;
    MaterialCalendarView materialCalendarView;

    int Year = 0, Month = 0, Day = 0;
    String memberId = "";
    ArrayList<ListItem> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Intent inIntent = getIntent();
        Year = inIntent.getIntExtra("year",0);
        Month = inIntent.getIntExtra("month",0);
        Day = inIntent.getIntExtra("day",0);
        memberId = inIntent.getStringExtra("memberId");

        textView_calendar = findViewById(R.id.textView_calendar);
        String shot_Day = Year + "년 " + Month + "월 " + Day + "일";
        textView_calendar.setText(shot_Day);
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.hide();*/

        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();


        materialCalendarView.addDecorators(
                //new SundayDecorator(),
                //new SaturdayDecorator(),
                oneDayDecorator); //오늘날짜에 표시

        materialCalendarView.addDecorators(oneDayDecorator);
        materialCalendarView.setPadding(10, 5, 10, 5);


        dates = new ArrayList<>();
        ArrayList<String> result = new ArrayList<String>();
        //String[] result = new String[100];
        //String[] cal_date = new String[result.length];
        //String[] result = {"2021-05-01", "2021-05-05", "2021-05-10", "2021-05-27"};
        try {

            String rst = new Task().execute(/*memberId*/memberId).get();
            JSONObject json = new JSONObject(rst);
            JSONArray jArr = json.getJSONArray("List");
            String msg1 = "";
            String msg2 = "";

            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                msg1 = json.getString("dietDate");
                msg2 = json.getString("dietId");

                if (i == 0) {
                    result.add(msg1.substring(0, 10));
                } else {
                    int add_flag=1;
                    for (int j = 0; j < result.size(); j++) {
                        add_flag=1;
                        if (result.get(j).equals(msg1.substring(0, 10))) {
                            add_flag=0;
                            break;
                        }
                    }
                    if(add_flag==1)
                        result.add(msg1.substring(0, 10));
                }
                // String[] result = {"2021,05,1", "2021,05,5", "2021,05,10", "2021,05,27"};
            }
            //String[] result = {"2021-05-01", "2021-05-05", "2021-05-10", "2021-05-27"};
            String[] cal_date = new String[result.size()];
            for(int i=0; i<result.size(); i++){
                cal_date[i] = result.get(i);

            }
            new ApiSimulator(cal_date).execute().get();
            //new ApiSimulator(cal_date).executeOnExecutor(Executors.newSingleThreadExecutor());
        } catch (Exception e) {
            e.printStackTrace();
        }



        //녹색 테두리


        //new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        //new ApiSimulator(result).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                Year = date.getYear();
                Month = date.getMonth() + 1;
                Day = date.getDay();



                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Year);
                cal.set(Calendar.MONTH, Month);
                cal.set(Calendar.DATE, Year);


                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = Year + "년 " + Month + "월 " + Day + "일";

                Log.i("shot_Day test", shot_Day + "");

                //materialCalendarView.clearSelection();
                materialCalendarView.setSelectionColor(Color.LTGRAY);


                textView_calendar.setText(shot_Day);
                textView_calendar.setTextSize(20);
                //Toast.makeText(getApplicationContext(), shot_Day, Toast.LENGTH_SHORT).show();

            }
        });

        finish_btn = findViewById(R.id.finish_btn);
        ok_btn = findViewById(R.id.ok_btn);

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                outIntent.putExtra("Year", Year);
                outIntent.putExtra("Month", Month);
                outIntent.putExtra("Day", Day);
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
    }


    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result) {
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for (int i = 0; i < Time_Result.length; i++) {
                //CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);
                //day.from(year,month,dayy);
                dates.add(new CalendarDay(year, month-1,dayy));
                calendar.set(year, month - 1, dayy);
            }
            materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, dates, CalendarActivity.this));

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }
            //날짜 밑에 도트찍기
            //materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays, CalendarActivity.this));
            //materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, MainActivity.this));
        }
    }

    public class Task extends AsyncTask<String, Void, String> {
        //public  String ip ="220.149.119.161:8080"; //자신의 IP번호
        String sendMsg, receiveMsg;
        String serverIp = "http://107.22.137.189:8080/mobile/diet/dietDateList.jsp"; // 연결할 jsp주소

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(serverIp);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "memberId="+strings[0];

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