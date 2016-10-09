package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;

/**
 * Created by bruna on 08/10/2016.
 */

public class RecoverPassActivity extends Activity{

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private TextView inputEmail;
    private TextView btnSend;
    private ProgressDialog pDialog;

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

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Pega os valores nos campos
                String email = inputEmail.getText().toString().trim();
                //Se o campo estiver preenchido chamo a função de POST
                if (!email.isEmpty()) {
                    recoverPassword(email);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Por favor preencha o email!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void recoverPassword(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Enviando email ...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_RECOVER, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Resposta do Registro no Servidor: " + response.toString());
                showDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // Enviou o email!
                        //Traduz o JSON em objetos

                        JSONObject answer = jObj.getJSONObject("response");
                        String status = answer.getString("status");
                        String email = answer.getString("email");

                        Toast.makeText(getApplicationContext(), status + " para " + email, Toast.LENGTH_SHORT).show();
                    } else {

                        // Não existe usuário no banco
                        // Ou parêmetro de POST faltando
                        if (jObj.getString("error_msg") != ""){
                            Toast.makeText(getApplicationContext(),"Um erro ocorreu enviar novamente", Toast.LENGTH_LONG).show();
                        }else{
                            JSONObject answer = jObj.getJSONObject("response");
                            String status = answer.getString("status");
                            String email = answer.getString("email");

                            Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Send Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
