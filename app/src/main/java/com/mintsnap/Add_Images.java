package com.mintsnap;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Images extends Fragment {
    String val;
    Bundle bundle;
   // Document document;
    File photofile;
    String mCurrentPhotoPath;
    TextView selectedFileName;
    String fileName;
    String fullPath;
    Image image;
    ImageView add_pdf,share_pdf,read_pdf,upload_pdf;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public Add_Images() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add__images, container, false);

        add_pdf = (ImageView) view.findViewById(R.id.add_pdf);
        share_pdf = (ImageView) view.findViewById(R.id.share_pdf);
        read_pdf = (ImageView) view.findViewById(R.id.read_pdf);
       // upload_pdf = (ImageView) view.findViewById(R.id.upload_pdf);


     /*  upload_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadit();
            }
        });*/

        selectedFileName = (TextView)view.findViewById(R.id.selectedFileName);

        try {
            bundle=getArguments();
            val = bundle.getString("filename");

            fileName = URLUtil.guessFileName(val, null, null);
            selectedFileName.setText(fileName);

        } catch (Exception e) {
            selectedFileName.setText("No File Selected");
            e.printStackTrace();
        }




        add_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filepath = val;
                if(fileName == null){
                    Toast.makeText(getActivity(),"No File Selected",Toast.LENGTH_SHORT).show();
                }else {


                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
               /* Intent intent = new Intent(
                        MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                startActivity(intent);*/
            }
        });
        share_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("application/pdf");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(val));
                    startActivity(Intent.createChooser(shareIntent, "Share image using"));
                } catch (Exception e) {
                    Toast.makeText(getActivity(),"No File Selected" ,Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        read_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                   /* Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(val), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);

*/

                    String result = fileName.substring(0, fileName.lastIndexOf("."));
                    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mintsnap/"+result);
                    String[] fileCount = path.list();

                    Document document = new Document();
                    String file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mintsnap/"+fileName;
                    PdfWriter.getInstance(document,new FileOutputStream(file));
                    document.open();

                    for(int i = 0; i < fileCount.length; i++)
                    {
                        fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mintsnap/"+result+"/"+fileCount[i];
                       image = Image.getInstance(fullPath);
                       image.setAlignment(Image.MIDDLE);
                        image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                        document.add(image);
                    }
                    document.close();


                    Intent intent = new Intent(getActivity(), Read_pdf.class);
                    intent.putExtra("FILE_PATH", val);
                    startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(getActivity(),"Please Add Photos/File to read." ,Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

    try{

    saveImageToExternalStorage(imageBitmap);
    }            catch (Exception e) {
                Toast.makeText(getActivity(),"Unexpexted Error." ,Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

           /* mImageView.setImageBitmap(imageBitmap);*/
        }
    }


    public boolean saveImageToExternalStorage(Bitmap image) {

        String result = fileName.substring(0, fileName.lastIndexOf("."));
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mintsnap/"+result;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        try {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            OutputStream fOut = null;
            File file = new File(fullPath, imageFileName+".jpg");
            file.createNewFile();
            fOut = new FileOutputStream(file);
// 100 means no compression, the lower you go, the stronger the compression
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            Toast.makeText(getActivity(),"ADDED" ,Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
            return false;
        }
    }

    public void uploadit(){
        if(fileName == null){
            Toast.makeText(getActivity(),"No File Selected",Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(getContext(), feed_upload.class);
            i.putExtra("FILE_NAME", val);
            startActivity(i);
        }

    }


}