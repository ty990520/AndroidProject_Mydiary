package com.cookandroid.mydiary1;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> bottomTextList = new ArrayList<String>();
    private int bottomTextDescent;
    private Paint bottomTextPaint = new Paint();
    private int bottomTextHeight = 0;
    String year, month, id;
    int Year, Month;
    TextView textViewDietCnt, textViewBodyCnt, textVieWeightCnt, textViewExerciseHours, tvDate;
    Button btn_prev, btn_next;

    public Fragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
//            year = String.valueOf(bundle.getInt("Year"));
//            month = String.valueOf(bundle.getInt("Month"));

            Year = bundle.getInt("Year");
            Month = bundle.getInt("Month");
            id = bundle.getString("memberId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_3, container, false);

//        for (Record record : records) { //values에 데이터를 담는 과정
//            long dateTime = record.getDateTime();
//            float weight = (float) record.getWeight();
//            values.add(new Entry(dateTime, weight));
//        }

        Fragment3 fragment3 = new Fragment3();
        btn_prev=rootView.findViewById(R.id.button13);
        btn_next =rootView.findViewById(R.id.button14);
        TextView tvDiet[]=new TextView[5];
        TextView tvCnt[]=new TextView[5];
        Integer tvDietId[]={R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5};
        Integer tvCntId[]={R.id.textView6, R.id.textView8, R.id.textView10, R.id.textView12, R.id.textView14};
        textViewDietCnt=rootView.findViewById(R.id.textViewDietCnt);
        textViewBodyCnt=rootView.findViewById(R.id.textViewBodyCnt);
        textVieWeightCnt=rootView.findViewById(R.id.textVieWeightCnt);
        textViewExerciseHours=rootView.findViewById(R.id.textViewExerciseHours);
        //tvDate=rootView.findViewById(R.id.textView30);
        //tvDate.setText(Year+"년 "+Month+"월");
        year=String.valueOf(Year);
        month=String.valueOf(Month);

        ArrayList<String> dietGroup = new ArrayList<>();
        ArrayList<String> dietCnt = new ArrayList<>();
        String check;
        int sum1=0;
        try {
            String rst = new Task3().execute(id, year, month).get();
            JSONObject json = new JSONObject(rst);
            JSONArray jArr = json.getJSONArray("List");

            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                dietGroup.add(json.getString("dietGroup"));
                dietCnt.add(json.getString("dietCnt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i=0;i<5;i++) {
            tvDiet[i]=rootView.findViewById(tvDietId[i]);
            tvCnt[i]=rootView.findViewById(tvCntId[i]);
            check= (String) tvDiet[i].getText().toString();
            for(int j=0;j<dietGroup.size();j++) {
                if(check.equals(dietGroup.get(j)))
                    tvCnt[i].setText(dietCnt.get(j));
            }
            sum1+=Integer.valueOf(tvCnt[i].getText().toString());
        }

        textViewDietCnt.setText(sum1+"개");


        int sum2=0;
        try {
            String rst = new Task4().execute(id, year, month).get();
            JSONObject json = new JSONObject(rst);
            JSONArray jArr = json.getJSONArray("List");

            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                sum2=json.getInt("sum");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        textViewBodyCnt.setText(sum2+"개");

        ArrayList<Float> weight = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        String cut;
        try {
            String rst = new Task().execute(id, year, month).get();
            JSONObject json = new JSONObject(rst);
            JSONArray jArr = json.getJSONArray("List");

            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                weight.add(Float.valueOf(json.getString("weight")));
                cut = json.getString("bodyDate");
                date.add(cut.substring(8, 10));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        double change=(weight.get(weight.size()-1)-weight.get(0));
        if (change > 0)
            textVieWeightCnt.setText("+"+new DecimalFormat("#.#").format(change));
        else
            textVieWeightCnt.setText(new DecimalFormat("#.#").format(change));


        MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();

        if(weight.size()>0) {
            LineChart lineChart = rootView.findViewById(R.id.lineChart);
            ArrayList entries = new ArrayList<>();

            for (int i = 0; i < weight.size(); i++) {
                entries.add(new Entry(i, weight.get(i)));
            }

            LineDataSet lineDataSet = new LineDataSet(entries, "");
            lineChart.getLegend().setEnabled(false); // label 없애기

            lineDataSet.setLineWidth(2);
            lineDataSet.setCircleRadius(3);
            lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
            //lineDataSet.setCircleColorHole(Color.BLUE);
            lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
            lineDataSet.setDrawCircleHole(true);
            lineDataSet.setDrawCircles(true);
            lineDataSet.setDrawHorizontalHighlightIndicator(false);
            lineDataSet.setDrawHighlightIndicators(false);
            lineDataSet.setDrawValues(true);
            lineDataSet.setValueTextSize(9f);

            LineData lineData = new LineData(lineDataSet);
            lineData.setValueFormatter(formatter);
            lineChart.setData(lineData);

            XAxis xAxis = lineChart.getXAxis(); // x축

            //final String[] labels = {"11", "12", "13", "14", "15", "16", "17", "20", "23", "25"}; // Your List / array with String Values For X-axis Labels
            xAxis.setValueFormatter(new IndexAxisValueFormatter(date));

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // // x축 레이블 아래로
            xAxis.setTextColor(Color.BLACK);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(false);
            xAxis.setLabelCount(date.size(), true); // x축 레이블 표시 개수
//        xAxis.enableGridDashedLine(8, 24, 0);

            YAxis yLAxis = lineChart.getAxisLeft(); // y축 왼쪽
            yLAxis.setTextColor(Color.BLACK);

            YAxis yRAxis = lineChart.getAxisRight(); // y축 오른쪽
            yRAxis.setEnabled(false);

            lineChart.animateY(2000, Easing.EaseInCubic);
            //MyMarkerView marker = new MyMarkerView(getActivity().getApplicationContext(), R.layout.markerviewtext);
            //marker.setChartView(lineChart);
            //lineChart.setMarker(marker);

            Description description = new Description();
            description.setText("");
            lineChart.setDoubleTapToZoomEnabled(false);
            lineChart.setDrawGridBackground(false);
            lineChart.setDescription(description);
            //lineChart.animateXY(2000, 2000); //애니메이션 기능 활성화
            //lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
            lineChart.invalidate();
        }

        BarChart barChart = rootView.findViewById(R.id.barChart);

        ArrayList barEntries = new ArrayList<>();
        ArrayList<String> avg_age=new ArrayList<>();
        ArrayList<Float> avg_weight=new ArrayList<>();
        int gender=0;
        try {
            String rst = new Task2().execute(id, year, month).get();
            JSONObject json = new JSONObject(rst);
            JSONArray jArr = json.getJSONArray("List");

            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                cut = json.getString("age");
                avg_age.add(cut.substring(0, 1)+"0");

                cut = json.getString("average");
                avg_weight.add(Float.valueOf(cut.substring(0, 2)));

                gender=json.getInt("gender");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(gender==0)
            textViewExerciseHours.append(" (남성)");
        else
            textViewExerciseHours.append(" (여성)");

        for (int i = 0; i < avg_weight.size(); i++) {
            barEntries.add(new BarEntry(i, avg_weight.get(i)));
        }

//        barEntries.add(new BarEntry(0, 18));
//        barEntries.add(new BarEntry(1, 16));
//        barEntries.add(new BarEntry(2, 20));
//        barEntries.add(new BarEntry(3, 10));
//        barEntries.add(new BarEntry(4, 25));
//        barEntries.add(new BarEntry(5, 18));
//        barEntries.add(new BarEntry(6, 16));
//        barEntries.add(new BarEntry(7, 20));
//        barEntries.add(new BarEntry(8, 30));
//        barEntries.add(new BarEntry(9, 25));

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setValueTextSize(9f);
        barChart.getLegend().setEnabled(false); // label 없애기

        BarData bardata = new BarData(barDataSet); //데이터 객체에  dataSet 객체 넣어주기
        bardata.setValueFormatter(formatter);
        barChart.setData(bardata); //차트 위젯에 data 객체 넣기

        XAxis barxAxis = barChart.getXAxis(); // x축

        //final String[] barlabels = {"10", "20", "30", "40", "50", "60"}; // Your List / array with String Values For X-axis Labels
        barxAxis.setValueFormatter(new IndexAxisValueFormatter(avg_age));

        barxAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // // x축 레이블 아래로
        barxAxis.setTextColor(Color.BLACK);
        barxAxis.setDrawAxisLine(true);
        barxAxis.setDrawGridLines(false);
        barxAxis.setLabelCount(avg_age.size());
        barxAxis.enableGridDashedLine(8, 24, 0);

        YAxis baryLAxis = barChart.getAxisLeft(); // y축 왼쪽
        baryLAxis.setTextColor(Color.BLACK);

        YAxis baryRAxis = barChart.getAxisRight(); // y축 오른쪽
        baryRAxis.setEnabled(false);

        bardata.setBarWidth(0.5f); //bar 크기 지정
        //barDataSet.setColors(Color.parseColor("#FFA1B4DC"));
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        Description bardescription = new Description();
        bardescription.setText("");
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDescription(bardescription);
        barChart.animateY(2000, Easing.EaseInCubic);
        barChart.invalidate();
        return rootView;
    }

    public class Task extends AsyncTask<String, Void, String> {
        //public  String ip ="220.149.119.161:8080"; //자신의 IP번호
        String sendMsg, receiveMsg;
        //        String serverIp = "http://54.227.89.25:8080/mobile/member/selectByMemberId.jsp"; // 연결할 jsp주소
        String serverIp = "http://54.227.89.25:8080/mobile/member/weightChange.jsp"; // 연결할 jsp주소

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(serverIp);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "memberId="+strings[0]+"&year="+strings[1]+"&month="+strings[2];

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

    public class Task2 extends AsyncTask<String, Void, String> {
        //public  String ip ="220.149.119.161:8080"; //자신의 IP번호
        String sendMsg, receiveMsg;
        //        String serverIp = "http://54.227.89.25:8080/mobile/member/selectByMemberId.jsp"; // 연결할 jsp주소
        String serverIp = "http://54.227.89.25:8080/mobile/member/averageWeight.jsp"; // 연결할 jsp주소

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(serverIp);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "memberId="+strings[0]+"&year="+strings[1]+"&month="+strings[2];
                //sendMsg="";

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

    public class Task3 extends AsyncTask<String, Void, String> {
        //public  String ip ="220.149.119.161:8080"; //자신의 IP번호
        String sendMsg, receiveMsg;
        //        String serverIp = "http://54.227.89.25:8080/mobile/member/selectByMemberId.jsp"; // 연결할 jsp주소
        String serverIp = "http://54.227.89.25:8080/mobile/member/dietCount.jsp"; // 연결할 jsp주소

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(serverIp);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "memberId="+strings[0]+"&year="+strings[1]+"&month="+strings[2];; //+"&gender="+strings[2];
                //sendMsg="";

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

    public class Task4 extends AsyncTask<String, Void, String> {
        //public  String ip ="220.149.119.161:8080"; //자신의 IP번호
        String sendMsg, receiveMsg;
        //        String serverIp = "http://54.227.89.25:8080/mobile/member/selectByMemberId.jsp"; // 연결할 jsp주소
        String serverIp = "http://54.227.89.25:8080/mobile/member/bodyCount.jsp"; // 연결할 jsp주소

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(serverIp);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "memberId="+strings[0]+"&year="+strings[1]+"&month="+strings[2];; //+"&gender="+strings[2];
                //sendMsg="";

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