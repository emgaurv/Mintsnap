package com.mintsnap;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    File path;
    ListView list;
    EditText fileName;
    String newFileName;
    static ArrayList<String> pdf_paths=new ArrayList<String>();
    static ArrayList<String> pdf_names=new ArrayList<String>();

    public Home() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment


        list=(ListView)view.findViewById(R.id.listView1);
        pdf_paths.clear();
        pdf_names.clear();




        path = new File(Environment.getExternalStorageDirectory() + "/Mintsnap");
        searchFolderRecursive1(path);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.pdf_listview_item, R.id.rowTextView, pdf_names);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String path = pdf_paths.get(i);
                    File file = new File(path);
                    Uri path2 = Uri.fromFile(file);
                    Add_Images fragment = new Add_Images();
                    Bundle bundle=new Bundle();

                    bundle.putString("filename",(path2).toString());

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.content, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });

        fileName = (EditText) view.findViewById(R.id.fileName);
        ImageView createFile = (ImageView) view.findViewById(R.id.createFile);
        createFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


              newFileName = fileName.getText().toString();


                if(TextUtils.isEmpty(newFileName) || newFileName == " "){

                    int errorColor;
                    final int version = Build.VERSION.SDK_INT;
                    if (version >= 23) {

                        errorColor = ContextCompat.getColor(getActivity(), R.color.errorColor);
                    } else {
                        errorColor = getResources().getColor(R.color.errorColor);
                    }


                    String errorString = "Please Write File Name";  // your error message
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
                    spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
                    fileName.setError(spannableStringBuilder);


                    /*fileName.setError(Html.fromHtml("<font color='white'>Please Write File Name</font>"));*/
                    return;
                }else{
                    createPdf();
                    String targetPdf = Environment.getExternalStorageDirectory() + "/Mintsnap/" + newFileName +".pdf";


       /*     PdfDocument document = new PdfDocument();
               PdfDocument.PageInfo pageInfo =
                        new PdfDocument.PageInfo.Builder(100, 100, 1).create();
                PdfDocument.Page page = document.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                canvas.drawCircle(50, 50, 30, paint);
                // finish the page
                document.finishPage(page);

                // write the document content
                String targetPdf = Environment.getExternalStorageDirectory() + "/Mintsnap/" + newFileName +".pdf";
                File filePath = new File(targetPdf);

                try {
                    document.writeTo(new FileOutputStream(filePath));
                    Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Something wrong: " + e.toString(),
                            Toast.LENGTH_LONG).show();
                }
                document.close();
*/
                //Opening New Fragement Activity
                Add_Images fragment = new Add_Images();
                Bundle bundle=new Bundle();
                bundle.putString("filename",targetPdf);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                }

            }
        });





        return view;

    }


    private static void searchFolderRecursive1(File folder)
    {
        if (folder != null)
        {
            if (folder.listFiles() != null)
            {
                for (File file : folder.listFiles())
                {
                    if (file.isFile())
                    {
                        //.pdf files
                        if(file.getName().contains(".pdf"))
                        {
                            file.getPath();
                            pdf_names.add(file.getName());
                            pdf_paths.add(file.getPath());
                        }
                    }
                    else
                    {
                        searchFolderRecursive1(file);
                    }
                }
            }
        }
    }


    private void createPdf() {
        // TODO Auto-generated method stub
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mintsnap";
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();
            Log.d("PDFCreator", "PDF Path: " + path);
            File file = new File(dir, newFileName+".pdf");

            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(document, fOut);

            //open the document
            document.open();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getBaseContext().getResources(), R.drawable.cover);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.MIDDLE);
            document.add(myImg);
            document.close();

            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/Mintsnap/"+newFileName);

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("App", "failed to create directory");
                }
            }


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }


    }




}
