package rahul.com.savetocloud;


import android.app.Activity;

import java.io.ByteArrayOutputStream;

import com.parse.ParseFile;
import com.parse.ParseObject;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by rahul on 5/5/15.
 */
public class UploadImageActivity extends Activity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the view from main.xml
        setContentView(R.layout.upload_image);

        // start the service to monitor image folder and save data
        startService(new Intent(this, MonitorNdSave.class));

        // Locate the button in main.xml
        button = (Button) findViewById(R.id.uploadbtn);

        // Capture button clicks
        button.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Locate the image in res > drawable-hdpi


                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.rip);
                // Convert it to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile
                ParseFile file = new ParseFile("trackers.png", image);
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
                Toast.makeText(UploadImageActivity.this, "Image Uploaded",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upload_menu, menu);
        return true;
    }

}

