package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.loginandregistration.R;

/**
 * Created by bruna on 08/10/2016.
 */

public class RecoverPassActivity extends Activity{

    TextView inputEmail;
    TextView btnSend;

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordrecover);

        inputEmail = (TextView) findViewById(R.id.recoveremail);
        btnSend = (TextView) findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Pega os valores nos campos
                String email = inputEmail.getText().toString().trim();

                Toast.makeText(getApplicationContext(),
                            email, Toast.LENGTH_LONG)
                            .show();
                }

        });



    }
}
