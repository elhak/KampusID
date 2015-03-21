package wartech.kampusid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Andromeda on 21-Mar-15.
 */
public class splash extends Activity {
    //Set waktu lama splashscreen
    private static int splashInterval = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splas);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i = new Intent(splash.this, MainActivity.class);
                startActivity(i);


                //jeda selesai Splashscreen
                finish();
            }
        }, splashInterval);

    }
}
