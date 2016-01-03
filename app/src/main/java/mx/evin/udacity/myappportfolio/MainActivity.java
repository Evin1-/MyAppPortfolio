package mx.evin.udacity.myappportfolio;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "MyAppPortfolioTAG";

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

    private View retrieveViewByString(String viewIdString) {
        return findViewById(getResources().getIdentifier(viewIdString, "id", getPackageName()));
    }

    private String retrieveStringByName(String stringName){
        return getString(getResources().getIdentifier(stringName, "string", getPackageName()));
    }


}
