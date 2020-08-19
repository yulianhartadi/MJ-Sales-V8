package merak.jaya.abadi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static merak.jaya.abadi.MainActivity.setWindowFlag;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    EditText EdtNamaLengkap;
    EditText EdtUserName;
    EditText EdtNoHp;
    EditText EdtRegEmail;
    EditText EdtRegPassword;
    EditText EdtTryPassword;
    Button btn_create_new_account, btnback_login;
    Spinner SpnDepo;
    String DepoID;
    private String stringJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EdtNamaLengkap = findViewById(R.id.EdtNamaLengkap);
        EdtUserName = findViewById(R.id.EdtUserName);
        EdtNoHp = findViewById(R.id.EdtNoHp);
        EdtRegEmail = findViewById(R.id.EdtRegEmail);
        EdtRegPassword = findViewById(R.id.EdtRegPassword);
        EdtTryPassword = findViewById(R.id.EdtTryPassword);
        SpnDepo = findViewById(R.id.SpnDepo);
        btn_create_new_account = findViewById(R.id.btn_create_new_account);
        btnback_login = findViewById(R.id.btnback_login);
        btn_create_new_account.setOnClickListener(this);
        btnback_login.setOnClickListener(this);
        SpnDepo.setOnItemSelectedListener(this);
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        listdepo();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void listdepo() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://salestreck.nanoxy.co.id/api/ListDepo";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject jsonObject;
                ArrayList<HashMap<String, String>> arrayListDepo = new ArrayList<>();
                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        String stringJsonDepoId, stringJsonDepoName;
                        HashMap<String, String> hashMapDepo = new HashMap<>();
                        stringJsonDepoId = jo.getString("depoId");
                        stringJsonDepoName = jo.getString("depoName");
                        hashMapDepo.put("depoId", stringJsonDepoId);
                        hashMapDepo.put("depoName", stringJsonDepoName);
                        arrayListDepo.add(hashMapDepo);
                        ListAdapter listAdapterDepo;
                        listAdapterDepo = new SimpleAdapter(RegisterActivity.this, arrayListDepo, R.layout.item_depo, new String[]{"depoId", "depoName"}, new int[]{R.id.TVstoreId, R.id.TVstoreName});
                        SpnDepo.setAdapter((SpinnerAdapter) listAdapterDepo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbidden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}