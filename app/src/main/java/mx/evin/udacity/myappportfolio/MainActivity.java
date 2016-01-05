package mx.evin.udacity.myappportfolio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MyAppPortfolioTAG";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
//    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[][] apps = new String[][]{
                {"non_started", "movies"},
                {"non_started", "football"},
                {"non_started", "library"},
                {"non_started", "build"},
                {"non_started", "reader"},
                {"non_started", "capstone"}
        };
        showAppNames(apps);

        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    mInformationTextView.setText("Message ok");
                } else {
                    mInformationTextView.setText("Message reweqweqweqwqqweqweqw");
                }
            }
        };
        mInformationTextView = (TextView) findViewById(R.id.informationTextView);

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        String message = "This will launch the ";
        TextView textViewStatus = (TextView) v.findViewById(R.id.card_button_name_txt);
        message += textViewStatus.getText() + "!";
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showAppNames(String[][] appNames){
        for (String[] app:appNames) {
            try{
                CardView cardViewHolder = (CardView) retrieveViewByString("card_" + app[1]);
                TextView textViewStatus = (TextView) cardViewHolder.findViewById(R.id.card_button_status_txt);
                TextView textViewName = (TextView) cardViewHolder.findViewById(R.id.card_button_name_txt);

                textViewStatus.setText(retrieveStringByName("status_" + app[0]));
                textViewName.setText(retrieveStringByName("project_name_" + app[1]));
                cardViewHolder.setOnClickListener(this);
            }catch (NullPointerException np){
                Log.d(TAG, "Error: " + np.toString());
            }catch (Resources.NotFoundException nf){
                Log.d(TAG, "Resource not found: " + nf.toString());
            }
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private View retrieveViewByString(String viewIdString) {
        return findViewById(getResources().getIdentifier(viewIdString, "id", getPackageName()));
    }

    private String retrieveStringByName(String stringName){
        return getString(getResources().getIdentifier(stringName, "string", getPackageName()));
    }


}
