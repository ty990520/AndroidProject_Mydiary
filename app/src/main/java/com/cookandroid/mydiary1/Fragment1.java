package com.cookandroid.mydiary1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HorizontalCalendar horizontalCalendar;
    int Year = 0;
    int Month = 0;
    int Day = 0;
    String memberId = "";

    Calendar date = Calendar.getInstance();


    public Fragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

            date.set(Year, Month-1, Day);
            //format1 = String.format("Year", cal.get(Calendar.DAY_OF_MONTH));
            //format2 = String.format("m", cal.getMon);
            //DateFormat.format("E",cal);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);
        //materialCalendarView = (MaterialCalendarView) vg.findViewById(R.id.calendarView);



        //처음 childfragment 지정
        Bundle bundle = new Bundle();
        Fragment1_child1 frag = new Fragment1_child1();
        bundle.putInt("Year", Year);
        bundle.putInt("Month", Month);
        bundle.putInt("Day", Day);
        bundle.putString("memberId", memberId);
        frag.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.child_fragment, frag).commit();

        //하위버튼
        LinearLayout subButton1 = (LinearLayout) v.findViewById(R.id.subButton1);
        LinearLayout subButton2 = (LinearLayout) v.findViewById(R.id.subButton2);


        //클릭 이벤트
        subButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment1_child1 frag1 = new Fragment1_child1();
                frag1.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.child_fragment, frag1).commit();

                //Toast.makeText(getContext(), String.valueOf(Year) + String.valueOf(Month) + String.valueOf(Day) + "", Toast.LENGTH_LONG).show();
                //getFragmentManager().beginTransaction().replace(R.id.child_fragment, frag1).commit();
            }
        });
        subButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment1_child2 frag2 = new Fragment1_child2();
                frag2.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.child_fragment, frag2).commit();

            }
        });


        /* start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* end after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);


        //if (Year == 0) {
        //    DateFormat.format("EEE, MMM d, yyyy", Month Day Year, );
        //} else {
        horizontalCalendar = new HorizontalCalendar.Builder(v, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .configure()
                .formatTopText("EEE")
                .formatMiddleText("dd")
                //.formatBottomText("EEE")
                .textSize(10f, 16f, 0f)
                .showTopText(false)
                .showBottomText(true)
                .textColor(Color.LTGRAY, Color.parseColor("#148ACA"))
                .colorTextMiddle(Color.LTGRAY, Color.parseColor("#148ACA"))
                .end()
                .defaultSelectedDate(date)
                .build();
        //}
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                Day = Integer.parseInt((String) DateFormat.format("dd", date));
                Month = Integer.parseInt(((String) DateFormat.format("MMM", date)).substring(0, ((String) DateFormat.format("MMM", date)).length() - 1));
                Year = Integer.parseInt((String) DateFormat.format("yyyy", date));


                Fragment1_child1 frag3 = new Fragment1_child1();
                Bundle bundle3 = new Bundle();
                bundle3.putInt("Year", Year);
                bundle3.putInt("Month", Month);
                bundle3.putInt("Day", Day);
                bundle3.putString("memberId", memberId);
                frag3.setArguments(bundle3);
                getFragmentManager().beginTransaction().replace(R.id.child_fragment, frag3).commit();

                //Toast.makeText(getContext(), DateFormat.format("yyyy년 MMM dd일 EEE요일", date) + "의 식단이에요", Toast.LENGTH_SHORT).show();
            }

        });
        return v;
    }


}
