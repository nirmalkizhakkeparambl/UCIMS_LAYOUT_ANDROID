package com.gisfy.unauthorizedlayouts;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gisfy.unauthorizedlayouts.Util.VideoCapture;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class Main2Activity extends AppCompatActivity {
    ProgressDialog pDialog;
    VideoCapture videoCapture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        videoCapture=findViewById(R.id.video_view);
    }

    public void submit(View view) {
        new VideoTask().execute("kishore","/storage/emulated/0/DCIM/Camera/VID_20200518_181241.mp4");
    }
    private class VideoTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Main2Activity.this);
            pDialog.setMessage("Please wait...It is downloading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            okhttp3.Response response = null;
            String url= strings[0];
            String file= strings[1];
            try {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("",file,
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        new File(file)))
                        .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://148.72.208.177/ImageUpload/api/ManipulateVideo/UploadVideo?VideoName="+url)
                        .method("POST", body)
                        .build();
                response = client.newCall(request).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return String.valueOf(response);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result!=null) {
                pDialog.hide();
                Toast.makeText(Main2Activity.this, ""+result, Toast.LENGTH_SHORT).show();
                Log.i("reponse",result);
            }else {
                pDialog.show();
            }
        }
    }
//    private void showCustomDialog() {
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//        View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_video_recorder, viewGroup, false);
//        videoname = UUID.randomUUID().toString();
//        recorder = new MediaRecorder();
//        initRecorder();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DialogTheme);
//        builder.setView(dialogView);
//        alertDialog = builder.create();
//        Button submit=dialogView.findViewById(R.id.button);
//        SurfaceView cameraView=dialogView.findViewById(R.id.camera_view);
//
//        holder = cameraView.getHolder();
//        holder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                prepareRecorder();
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//            }
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                if (recording) {
//                    recorder.stop();
//                    recording = false;
//                }
//                recorder.release();
//                alertDialog.dismiss();
//            }
//        });
//
//        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        //Now we need an AlertDialog.Builder object
//
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (recording) {
//                    recorder.stop();
//                    recording = false;
//                    alertDialog.dismiss();
//                } else {
//                    recording = true;
//                    recorder.start();
//                }
//            }
//        });
//
//        alertDialog.show();
//    }
//    private void initRecorder() {
//        recorder = new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//        CamcorderProfile cpHigh = CamcorderProfile
//                .get(CamcorderProfile.QUALITY_LOW);
//        recorder.setProfile(cpHigh);
//        File path = new File("/storage/emulated/0/Android/data/com.gisfy.unauthorizedlayouts/files/", "UCIMSUL" + File.separator + "Videos");
//        if(!path.exists()){
//            path.mkdirs();
//        }
//        Videopath="/storage/emulated/0/Android/data/com.gisfy.unauthorizedlayouts/files/UCIMSUL/Videos/"+videoname+".MP4";
//        recorder.setOutputFile(Videopath);
//        recorder.setMaxDuration(30000); // 30 seconds
//    }
//
//    private void prepareRecorder() {
//        recorder.setPreviewDisplay(holder.getSurface());
//        try {
//            recorder.prepare();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//            alertDialog. dismiss();
//        } catch (IOException e) {
//            e.printStackTrace();
//            alertDialog.dismiss();
//        }
//    }
}
