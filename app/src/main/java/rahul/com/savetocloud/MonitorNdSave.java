package rahul.com.savetocloud;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

public class MonitorNdSave extends Service {

    private FileObserver observer;
    // FILELOC pointed to the default location where camera saves pictures
    private static String FILELOC = android.os.Environment.
            getExternalStorageDirectory().toString() + "/DCIM/Camera";
    private static final String TAG = "MonitorNdSave";


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

                observer = new FileObserver(FILELOC) {

                    @Override
                    public void onEvent(int event, String file) {
                        String imgFile;
                        imgFile = FILELOC + "/" + file;
                        Log.d(TAG, "File created [" + FILELOC + imgFile + "]");
                        uploadImg (imgFile); // function to save image to cloud
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

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        final Bitmap bitmap = BitmapFactory.decodeFile(imgFile, options);
        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file = new ParseFile("trackers.jpeg", image);
        // Upload the image into Parse Cloud
        file.saveInBackground();

        // Create a New Class called "ImageUpload" in Parse
        ParseObject imgupload = new ParseObject("ImageUpload");

        // Create a column named "ImageName" and set the string
        imgupload.put("ImageName", "Trackers");

        // Create a column named "ImageFile" and insert the image
        imgupload.put("ImageFile", file);

        // Create the class and the columns
        imgupload.saveInBackground();

        // Show a simple toast message
        Toast.makeText(MonitorNdSave.this, "Image Uploaded",
                Toast.LENGTH_SHORT).show();


    }
}