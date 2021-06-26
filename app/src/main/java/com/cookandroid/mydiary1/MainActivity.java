package com.cookandroid.mydiary1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main_Activity";
    private BottomNavigationView mBottomNavigationView;

    FragmentManager manager;
    FragmentTransaction transaction;
    Toast toast;
    int Year = 0, Month = 0, Day = 0;
    String memberId="", password="";
    private long backKeyPressedTime = 0;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this,"이용해 주셔서 감사합니다.",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        Intent login_intent = getIntent();
        memberId = login_intent.getStringExtra("memberId");
        password = login_intent.getStringExtra("password");

        //첫 화면 띄우기

        Bundle bundle = new Bundle();

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("MM");
        SimpleDateFormat format3 = new SimpleDateFormat("dd");


        if (Year == 0 && Month == 0 && Day == 0) {
            Year = Integer.parseInt(format1.format(System.currentTimeMillis()));
            Month = Integer.parseInt(format2.format(System.currentTimeMillis()));
            Day = Integer.parseInt(format3.format(System.currentTimeMillis()));

        }//Toast.makeText(this, String.valueOf(Year) + String.valueOf(Month) + String.valueOf(Day) + "", Toast.LENGTH_LONG).show();

        Fragment1 frag = new Fragment1();
        bundle.putInt("Year", Year);
        bundle.putInt("Month", Month);
        bundle.putInt("Day", Day);
        bundle.putString("memberId", memberId);
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, frag).commit();

        //case 함수를 통해 클릭 받을 때마다 화면 변경하기
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_calendar:
                        Fragment1 frag1 = new Fragment1();
                        if (Year == 0 && Month == 0 && Day == 0) {
                            Year = Integer.parseInt(format1.format(System.currentTimeMillis()));
                            Month = Integer.parseInt(format2.format(System.currentTimeMillis()));
                            Day = Integer.parseInt(format3.format(System.currentTimeMillis()));
                        }
                        bundle.putInt("Year", Year);
                        bundle.putInt("Month", Month);
                        bundle.putInt("Day", Day);
                        bundle.putString("memberId", memberId);
                        frag1.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, frag1).commit();
                        break;
                    case R.id.nav_menu:

                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Fragment2()).commit();
                        break;
                    case R.id.nav_graph:
                        Fragment3 frag3 = new Fragment3();
                        bundle.putInt("Year", Year);
                        bundle.putInt("Month", Month);
                        bundle.putString("memberId", memberId);
                        frag3.setArguments(bundle);

//                        StatsFragment frag = new StatsFragment();
//                        bundle.putInt("Year", Year);
//                        bundle.putInt("Month", Month);
//                        bundle.putString("memberId", memberId);
//                        frag.setArguments(bundle);

                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,frag3).commit();
                        break;
                    case R.id.nav_add:  //modal bottom sheet
                        //getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new Fragment4()).commit();
                        BottomSheetDialog bottomSheet = new BottomSheetDialog();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("memberId", memberId);
                        bottomSheet.setArguments(bundle2);
                        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                        break;
                }
                return true;
            }
        });


    }


    /*
        public void replaceFragment(Fragment fragment){
            manager = getSupportFragmentManager();
            transaction = manager.beginTransaction();

            transaction.replace(R.id.frame_container, fragment).commit();
        }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_btn1:
                Intent intent1 = new Intent(getApplicationContext(), CalendarActivity.class);
                intent1.putExtra("year", Year);
                intent1.putExtra("month", Month);
                intent1.putExtra("day", Day);
                intent1.putExtra("memberId", memberId);
                startActivityForResult(intent1, 0);
                return true;
            case R.id.action_btn2:
                Intent intent2 = new Intent(getApplicationContext(), Map.class);

                startActivityForResult(intent2, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Year = data.getIntExtra("Year", 0);
            Month = data.getIntExtra("Month", 0);
            Day = data.getIntExtra("Day", 0);


            Toast.makeText(getApplicationContext(), Year + "년 " + Month + "월 " + Day + "일 ", Toast.LENGTH_SHORT).show();
            Bundle bundle2 = new Bundle();
            Fragment1 frag2 = new Fragment1();
            bundle2.putInt("Year", Year);
            bundle2.putInt("Month", Month);
            bundle2.putInt("Day", Day);
            bundle2.putString("memberId", memberId);
            frag2.setArguments(bundle2);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, frag2).commit();
            //getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,frag1).commit();

        }
    }

//    class MyPagerAdapter extends FragmentStatePagerAdapter {
//        ArrayList<Fragment> items = new ArrayList<Fragment>();
//
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//        private FragmentManager mFM;
//        public MyPagerAdapter(FragmentManager fm) {
//            super(fm);
//            mFM = fm;
//        }
//        public void addItem(Fragment item, String title) {
//            items.add(item);
//            mFragmentTitleList.add(title);
//
//        }
//        @Override
//        public Fragment getItem(int position) {
//            return items.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return items.size();
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {return mFragmentTitleList.get(position);}
//        public FragmentManager getFM() {
//            return mFM;
//        }
//    }
}