package com.cookandroid.mydiary1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     BottomSheetDialog.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class BottomSheetDialog extends BottomSheetDialogFragment  implements View.OnClickListener{

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";

    private LinearLayout msgLo;
    private LinearLayout emailLo;
    private LinearLayout cloudLo;
    private LinearLayout bluetoothLo;
    private ImageView finish_btn_modal;
    String memberId="";

    // TODO: Customize parameters
    public static BottomSheetDialog newInstance(BottomNavigationView.OnNavigationItemSelectedListener itemCount) {
        final BottomSheetDialog fragment = new BottomSheetDialog();
        final Bundle args = new Bundle();
        //args.putInt(ARG_ITEM_COUNT, itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
             memberId = bundle.getString("memberId");
        }

        //return inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog, container, false);
        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container,false);
        msgLo = (LinearLayout) view.findViewById(R.id.msgLo);
        bluetoothLo = (LinearLayout) view.findViewById(R.id.bluetoothLo);

        msgLo.setOnClickListener(this);
        bluetoothLo.setOnClickListener(this);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.msgLo:
                Intent intent1 = new Intent(getActivity(),InputBody.class);
                intent1.putExtra("memberId",memberId);
                startActivity(intent1);
                break;
            /*case R.id.emailLo:
                Intent intent2 = new Intent(getActivity(),InputExercise.class);
                intent2.putExtra("memberId",memberId);
                startActivity(intent2);
                break;*/
            case R.id.bluetoothLo:
                Intent intent2 = new Intent(getActivity(),InputDiet.class);
                intent2.putExtra("memberId",memberId);
                startActivity(intent2);
                break;
        }
        dismiss();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // TODO: Customize the item layout
            super(inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog_item, parent, false));
            text = itemView.findViewById(R.id.text);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final int mItemCount;

        ItemAdapter(int itemCount) {
            mItemCount = itemCount;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }

}