package rahul.com.savetocloud;

/**
 * Created by rahul on 5/5/15.
 */
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import android.app.Application;

public class ParseApplication extends Application {

    private static final String APP_ID     = "fLW4mwYjyq7JjHEZ0gn4qBe6G7mPPpVQpw4NVy2h";
    private static final String CLIENT_KEY = "rlQvY4PWKhNH48GOju3oEGgyxURM2QdmNsEbRcvm";

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, APP_ID, CLIENT_KEY);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

}
