package com.example.tariqi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class DialogFragment extends androidx.fragment.app.DialogFragment {
    private static final String TITLE = "title";
    private static final String ICON = "icon";
    private static final String MESSAGE = "message";

    private String title;
    private String message;
    private int icon;
    private PositiveClickListener positiveClickListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof PositiveClickListener){
            positiveClickListener = (PositiveClickListener) context;
        }else
            throw new RuntimeException("please implement listener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        positiveClickListener = null;
    }

//    public static DialogFragment newInstance(){
//        Bundle bundle = new Bundle();
//        bundle.putString(TITLE,title);
//        bundle.putString(MESSAGE,message);
//        bundle.putInt(ICON,icon);
//        DialogFragment fragment = new DialogFragment();
//        fragment.setArguments(bundle);
//        return fragment;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            title = args.getString(TITLE);
            message = args.getString(MESSAGE);
            icon = args.getInt(ICON);
        }
    }

    public DialogFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button save = view.findViewById(R.id.save);
        EditText note = view.findViewById(R.id.edt_note);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positiveClickListener.onPositiveButtonCliced(note.getText().toString());
                dismiss();
            }
        });
    }
    public interface PositiveClickListener{
        void onPositiveButtonCliced(String note);
    }
}
