package com.cookandroid.mydiary1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class FragmentDialog extends DialogFragment {


    private Fragment fragment;
    ImageView finish_btn;

    public FragmentDialog() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calendar, container, false);

        Bundle args = getArguments();
        String value = args.getString("key");


        // 먼저 부모 프래그먼트를 받아옵니다.
        //findFragmentByTag안의 문자열 값은 Fragment1.java에서 있던 문자열과 같아야합니다.
        //dialog.show(getActivity().getSupportFragmentManager(),"tag");
        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
        finish_btn = view.findViewById(R.id.ok_btn);
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment != null) {
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                }
            }
        });

        return view;
    }
}
