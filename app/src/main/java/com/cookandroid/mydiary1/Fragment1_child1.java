package com.cookandroid.mydiary1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1_child1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1_child1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String[] array_dietId = new String[100];
    ArrayList<ListItem> diet;
    ArrayList<ListItem> menu;
    ListView customListView;
    private static CustomAdapter customAdapter;
    int Year = 0, Month = 0, Day = 0;
    String memberId = "";
    TextView msg;

    public Fragment1_child1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1_child1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment1_child1 newInstance(String param1, String param2) {
        Fragment1_child1 fragment = new Fragment1_child1();
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
            Year = bundle.getInt("Year");
            Month = bundle.getInt("Month");
            Day = bundle.getInt("Day");
            memberId = bundle.getString("memberId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fragment1_child1, container, false);
        msg = rootView.findViewById(R.id.msg);
        //msg = rootView.findViewById(R.id.msg);
        //Toast.makeText(getContext(),memberId, Toast.LENGTH_LONG).show();
        diet = new ArrayList<>();
        menu = new ArrayList<>();
        try {
            String rst = new Task().execute(/*memberId*/memberId, String.valueOf(Year), String.valueOf(Month), String.valueOf(Day)).get();
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
            String url = "";

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

                url = "http://107.22.137.189:8080/mobile/images/" + msg8;
                if (msg1 == "") {
                    msg.setVisibility(View.VISIBLE);
                } else
                    msg.setVisibility(View.INVISIBLE);

                //total += (msg1+":"+msg2+":"+msg3+":"+msg4+":"+msg5+":"+msg6+":"+msg7+":"+msg8+"\n");
                array_dietId[i] = msg4;
                menu.add(new ListItem(msg6, url, msg5 + "\n" + msg7));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        customListView = (ListView) rootView.findViewById(R.id.listView_custom);
        customAdapter = new CustomAdapter(getContext(), menu);
        customListView.setAdapter(customAdapter);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                //각 아이템을 분간 할 수 있는 position과 뷰

                Intent intent = new Intent(getActivity(), GridviewEdit.class);
                //intent.putExtra("test",1);
                String id_detail = array_dietId[position];
                intent.putExtra("DietId", id_detail);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class Task extends AsyncTask<String, Void, String> {
        //public  String ip ="220.149.119.161:8080"; //자신의 IP번호
        String sendMsg, receiveMsg;
        String serverIp = "http://107.22.137.189:8080/mobile/diet/selectDietListByDate.jsp"; // 연결할 jsp주소

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL(serverIp);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "memberId=" + strings[0] + "&year=" + strings[1] + "&month=" + strings[2] + "&day=" + strings[3];

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
    }
}

//data class
class ListItem {
    private String name;
    private String summary;
    private String thumb_url;

    public ListItem(String name, String thumb_url, String summary) {
        this.name = name;
        this.summary = summary;
        this.thumb_url = thumb_url;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getThumb_url() {
        return thumb_url;
    }
}