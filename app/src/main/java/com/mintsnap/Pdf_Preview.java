package com.mintsnap;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class Pdf_Preview extends Fragment {
    private static final int FILE_SELECT_CODE = 1;
ImageView activeornot;
    String FilePath;
    TextView selectedFileName;
    public Pdf_Preview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdf__preview, container, false);
        // Inflate the layout for this fragment
        Button openFile = (Button)view.findViewById(R.id.openFile);
        activeornot = (ImageView)view.findViewById(R.id.activeornot);
        selectedFileName = (TextView) view.findViewById(R.id.selectedFileName);
        activeornot.setImageResource(R.drawable.nores_button);
        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("*/*");      //all files
                intent.setType("application/pdf");   //XML file only
                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File to View"), FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(getActivity(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case FILE_SELECT_CODE:
                if(resultCode== Activity.RESULT_OK){
                    FilePath = data.getData().getPath();
                    activeornot.setImageResource(R.drawable.res_button);
                    selectedFileName.setText(URLUtil.guessFileName(FilePath, null, null));
                    activeornot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), Read_pdf.class);
                            intent.putExtra("FILE_PATH", FilePath);
                            startActivity(intent);
                        }
                    });
                }
                break;
        }
    }
}
