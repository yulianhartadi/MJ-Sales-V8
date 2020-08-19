package merak.jaya.abadi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_reset_password, btn_back_login_from_reset;
    EditText edtResetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        btn_reset_password = findViewById(R.id.btn_reset_password);
        btn_back_login_from_reset = findViewById(R.id.btn_back_login_from_reset);
        edtResetEmail=findViewById(R.id.edtResetEmail);
        btn_reset_password.setOnClickListener(this);
        btn_back_login_from_reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset_password:
                forgoting(edtResetEmail.getText().toString());
                break;
            case R.id.btn_back_login_from_reset:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }
    void forgoting(String s){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://salestreck.nanoxy.co.id/api/forgot";
        HashMap<String, String> params = new HashMap<>();
        params.put("email", s);
        Log.e( "forgoting: ",s );
        RequestParams requestParams=new RequestParams(params);

        StringBuilder url2=new StringBuilder();
        url2.append(url);
        try {
            url2.append(URLEncoder.encode("?email","UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url2.append("=");
        try {
            url2.append(URLEncoder.encode(s,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e( "forgoting: ",url2.toString() );

        client.get(url2.toString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e( "onSuccess: ", new String(responseBody));

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
                Toast.makeText(ResetPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
