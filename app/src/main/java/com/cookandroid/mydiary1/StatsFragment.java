package com.cookandroid.mydiary1;

import android.app.FragmentManager;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

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
    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment frag3 = new Fragment3();
        Bundle bundle = new Bundle();
        bundle.putInt("Year", Year);
        bundle.putInt("Month", Month);
        bundle.putString("memberId", id);
        frag3.setArguments(bundle);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment3, frag3).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        btn_prev=rootView.findViewById(R.id.button13);
        btn_next =rootView.findViewById(R.id.button14);
        tvDate=rootView.findViewById(R.id.textView30);
        tvDate.setText(Year+"년 "+Month+"월");

        Fragment3 fragment3 = new Fragment3();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.detach(this).attach(this).commit();

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Month==1){
                    Year--;
                    Month=12;
                }
                else {
                    Month--;
                }

                tvDate.setText(Year+"년 "+Month+"월");
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Month==12){
                    Year++;
                    Month=1;
                }
                else {
                    Month++;
                }
                tvDate.setText(Year+"년 "+Month+"월");
                //ft.detach(fragment3).attach(fragment3).commit();

            }
        });

        return rootView;
    }
}