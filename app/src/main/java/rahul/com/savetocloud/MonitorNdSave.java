package rahul.com.savetocloud;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;


import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

public class MonitorNdSave extends Service {
    // service that monitors if images are saved to a folder from camera and saves it to cloud when
    // and image is saved to local

    private static final String TAG = "MonitorNdSave";

    // FILELOC pointed to the default location where camera saves pictures, change it to appropriate
    // folder
    private static String FILELOC = android.os.Environment.
            getExternalStorageDirectory().toString() + "/DCIM/Camera";


    private FileObserver observer;


    public MonitorNdSave() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Runnable th = new Runnable() {
            public void run() {

                observer = new FileObserver(FILELOC, FileObserver.CLOSE_WRITE) {
                    @Override
                    public void onEvent(int event, String file) {
                        if (event == FileObserver.CLOSE_WRITE) {
                            String extension = "";

                            int i = file.lastIndexOf('.');
                            if (i > 0) {
                                extension = file.substring(i + 1);
                            }
                            String imgFile;
                            imgFile = FILELOC + "/" + file;
                            Log.d(TAG, extension);
                            uploadImg(imgFile); // function to save image to cloud
                        }
                    }
                };
                observer.startWatching();
            }
        };

        Thread t = new Thread(th);
        t.start();
        return Service.START_STICKY;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, " Destroyed");

    }

    private void uploadImg(String imgFile) {

        // compress the image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        final Bitmap bitmap = BitmapFactory.decodeFile(imgFile, options);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();

        // save the image
        ParseFile file = new ParseFile("trackers.jpeg", image);
        file.saveInBackground();

        // Create a New Class called "BotPics" in Parse
        ParseObject imgupload = new ParseObject("BotPics");
        // Create a column named "ImageName" and set the string
        imgupload.put("ImageName", "Trackers");
        // Create a column named "ImageFile" and insert the image
        imgupload.put("ImageFile", file);
        // Create the class and the columns
        imgupload.saveInBackground();

    }
}