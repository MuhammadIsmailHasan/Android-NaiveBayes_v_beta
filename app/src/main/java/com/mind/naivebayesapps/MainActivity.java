package com.mind.naivebayesapps;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    OkHttpClient client = new OkHttpClient();

    EditText fitur1, fitur2, fitur3;
    Button btnSub;
    Button btnKelompok;

    public String url = "http://54.165.2.188:8001/api/naive";

    String fitur_1, fitur_2, fitur_3;

    private static final String TAG = "prediksi";

    String mMessage;
    String hasilPredic;
    //    TextView hasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fitur1 = (EditText) findViewById(R.id.fitur_1);
        fitur2 = (EditText) findViewById(R.id.fitur_2);
        fitur3 = (EditText) findViewById(R.id.fitur_3);

        btnSub= (Button) findViewById(R.id.btnSubmit);
        btnKelompok= (Button) findViewById(R.id.btnKelompok);

        fitur_1 = fitur1.getText().toString();
        fitur_2 = fitur2.getText().toString();
        fitur_3 = fitur3.getText().toString();

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    postRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnKelompok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogKelompok();
            }
        });
    }

    private void postRequest() throws IOException {

        MediaType MEDIA_TYPE = MediaType.parse("application/json");

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("fitur_1", fitur_1);
            postdata.put("fitur_2", fitur_2);
            postdata.put("fitur_3", fitur_3);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                hasilPredic = mMessage.toString();
                alertDialog();
                Log.w("failure Response", mMessage);
//                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                mMessage = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(mMessage);
                            hasilPredic = json.getString("prediksi");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        alertDialog();
                    }
                });
                Log.e(TAG, mMessage);
            }
        });
    }

    private void alertDialog() {
        AlertDialog.Builder Peringatan = new AlertDialog.Builder(this);
        Peringatan.setTitle("Hasil Prediksi");
        Peringatan
                .setMessage(hasilPredic)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface Peringatan, int id) {
                        Peringatan.dismiss();
                    }
                }).show();
    }

    private void alertDialogKelompok() {
        AlertDialog.Builder Peringatan = new AlertDialog.Builder(this);
        Peringatan.setTitle("Daftar Nama Kelompok");
        Peringatan
                .setMessage("1. Muhammad Ismail Hasan \n2. Afwan Ghofur \n3. Mickael Alexander")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface Peringatan, int id) {
                        Peringatan.dismiss();
                    }
                }).show();
    }

}
