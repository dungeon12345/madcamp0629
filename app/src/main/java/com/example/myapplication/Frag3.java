package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Frag3 extends Fragment {

    private static final String TAG = "Frag2";
    private static final int REQUEST_CODE = 99;
    private static final int PICK_FROM_ALBUM = 1;
    private File tempFile;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public int inSampleSize = 1;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    private static String basePath;

    public float imageViewRotation = 90;

    private  Button buttonleft ;
    private  Button buttonright ;
    private ImageView resultView;
    private TextView imgPath;
    private Gallery customGallery;
    private String[] imgs;
    private ImageView imageView;
    private ImageView blackBackground;
    private ImageView falseButton;
    private ArrayList<String> images;
    private GridView gallery;

    private View v;
    private File f1;

    public Frag3() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    GridView mGridView ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //setup();

        v = inflater.inflate(R.layout.fragment_frag3, container, false);
        Button button = v.findViewById(R.id.button);
        Button button2 = v.findViewById(R.id.button2);
        Button button3 = v.findViewById(R.id.button3);
        buttonleft = v.findViewById(R.id.buttonleft);
        buttonright = v.findViewById(R.id.buttonright);

        imageView = v.findViewById(R.id.ImageView);
        blackBackground = v.findViewById(R.id.BlackBackground);
        falseButton = v.findViewById(R.id.button4);

        buttonleft.setEnabled(false);
        buttonright.setEnabled(false);
        blackBackground.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        falseButton.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyGalleryApp");

                if (! mediaStorageDir.exists()){
                    if (! mediaStorageDir.mkdirs()){
                        Log.d("MyGalleryApp", "failed to create directory");
                    }
                }


                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                try{
                        File f1 = createImageFile();
                        mCurrentPhotoPath = f1.getAbsolutePath();
                        // 이미지가 저장될 파일은 카메라 앱이 구동되기 전에 세팅해서 넘겨준다.
                        Uri uri = FileProvider.getUriForFile(getActivity(), getContext().getPackageName() + ".fileprovider", f1);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri) ;
                        startActivityForResult(takePictureIntent, REQUEST_CODE);
                        if(f1.length() ==0 ){
                            f1.delete();
                        }
                }catch( IOException e){
                        e.printStackTrace();
                    }


            }



        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_FROM_ALBUM);

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });


        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.GONE);
                blackBackground.setVisibility(View.GONE);
                falseButton.setVisibility(View.GONE);
                gallery.setEnabled(true);
                buttonleft.setEnabled(false);
                buttonright.setEnabled(false);
            }
        });

        buttonleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri selectedImg_uri = Uri.parse(images.get(current_position - 1));
                    imageView.setImageURI(selectedImg_uri);
                    imageView.setRotation(90);
                    blackBackground.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.bringToFront();
                    falseButton.setVisibility(View.VISIBLE);
                    falseButton.bringToFront();
                    gallery.setEnabled(false);
                    current_position--;
                }catch(IndexOutOfBoundsException e){

                    Toast.makeText(getContext(),"This is the First Picture in the Gallery",Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String uri_string = images.get(current_position + 1);
                    Uri selectedImg_uri = Uri.parse(uri_string);
                    imageView.setImageURI(selectedImg_uri);
                    imageView.setRotation(90);
                    blackBackground.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.bringToFront();
                    falseButton.setVisibility(View.VISIBLE);
                    falseButton.bringToFront();
                    gallery.setEnabled(false);
                    current_position++;
                }catch(IndexOutOfBoundsException e){

                    Toast.makeText(getContext(),"There is no more Picture in the Gallery",Toast.LENGTH_SHORT).show();
                }

            }
        });

        gridView();

        return v;
    }


    public void refresh(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
        Toast.makeText(getContext(),"Gallery has been refreshed",Toast.LENGTH_SHORT).show();

    }


    private int current_position;
    public void gridView(){

        gallery = (GridView) v.findViewById(R.id.galleryGridView);
        ImageAdapter imageadapter = new ImageAdapter((Activity) getContext());
        gallery.setAdapter(imageadapter);
        gallery.setEnabled(true);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (null != images && !images.isEmpty()){
                    Uri selectedImg_uri = Uri.parse(images.get(position));
                    current_position = position;
                    imageView.setImageURI(selectedImg_uri);
                    imageView.setRotation(90);
                    blackBackground.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.bringToFront();
                    falseButton.setVisibility(View.VISIBLE);

                    gallery.setEnabled(false);
                    buttonleft.setEnabled(true);
                    buttonright.setEnabled(true);
                    falseButton.bringToFront();
                }



            }

//            @Override
//            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
//                Toast.makeText(getContext(), "길게 눌렀구나 " + arrayListOfStudent.get(position).getStudentName(), Toast.LENGTH_SHORT).show();
//                return true;
//            }
//


        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_ALBUM) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                Uri photoUri = data.getData();     // get data는 intent 가 작업하고 있는 데이타를 가져옴 이를 통해 갤러리에서 선택한 이미지의 uri 를 받아옴
                Cursor cursor = null;
                try {
                    /*
                     *  Uri 스키마를
                     *  content:/// 에서 file:/// 로  변경한다.
                     */
                    String[] proj = {MediaStore.Images.Media.DATA};
                    assert photoUri != null;
                    cursor = getActivity().getContentResolver().query(photoUri, proj, null, null, null);

                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    tempFile = new File(cursor.getString(column_index));  // 이미지를 임시적으로 저장할 파일

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }


//                ImageView imageView = getActivity().findViewById(R.id.imageView);
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options); //tempfile을 bitmap 파일로 변경함
//
//                imageView.setImageBitmap(originalBm);  //imageview 에 사진을 추가 현재 originalBm에 사진 정보가 있음

            }
        }
        else if(requestCode == REQUEST_CODE){

            MoveToGallery();
//            imageView.setDrawingCacheEnabled(true);
//            Bitmap b = imageView.getDrawingCache();
//            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),b, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DESCRIPTION);
        }





    }
    final static String JPEG_FILE_PREFIX = "IMG_";

    final static String JPEG_FILE_SUFFIX = ".jpg";

    public  String mCurrentPhotoPath;
    
    private File createImageFile() throws IOException{

        String timeStamp = new SimpleDateFormat( "yyyyMMdd_HHmmss").format( new Date());

        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";

        File image = File.createTempFile(
                imageFileName,			// prefix

                JPEG_FILE_SUFFIX,		// suffix

                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyGalleryApp") //directory
        );

         mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }




    public void MoveToGallery(){

        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        File f = new File( mCurrentPhotoPath);

        Uri contentUri = Uri.fromFile( f);

        mediaScanIntent.setData( contentUri);

        getActivity().sendBroadcast( mediaScanIntent);

    }




    private class ImageAdapter extends BaseAdapter {

        /** The context. */
        private Activity context;

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext
         *            the local context
         */
        public ImageAdapter(Activity localContext) {
            context = localContext;
            images = getAllShownImagesPath(context);
        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(270, 270));

            } else {
                picturesView = (ImageView) convertView;
            }

            Glide.with(context).load(images.get(position))
                    .placeholder(R.drawable.ic_launcher_background).centerCrop()
                    .into(picturesView);

            return picturesView;
        }

        /**
         * Getting All Images Path.
         *
         * @param activity
         *            the activity
         * @return ArrayList with images Path
         */
        private ArrayList<String> getAllShownImagesPath(Activity activity) {
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, null);

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                listOfAllImages.add(absolutePathOfImage);
            }
            return listOfAllImages;
        }
    }



}