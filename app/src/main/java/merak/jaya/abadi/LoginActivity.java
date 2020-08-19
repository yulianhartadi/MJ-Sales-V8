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

import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtEmail , edtPassword;
    Button btn_email_sign_in , btn_to_register,btn_forgot_password ;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btn_email_sign_in = findViewById(R.id.btn_email_sign_in);
        btn_to_register = findViewById(R.id.btn_to_register);
        btn_forgot_password=findViewById(R.id.btn_forgot_password);
        btn_email_sign_in.setOnClickListener(this);
        btn_to_register.setOnClickListener(this);
        btn_forgot_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_email_sign_in:
                getLoginVerivication();
                break;
            case R.id.btn_forgot_password:
                intent = new Intent(this, ResetPasswordActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void getLoginVerivication() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://salestreck.nanoxy.co.id/api/CekSales";
        HashMap<String, String> params = new HashMap<>();
        params.put("UserName", edtEmail.getText().toString());
        params.put("Password", edtPassword.getText().toString());
        RequestParams requestParams=new RequestParams(params);

        client.post(url,requestParams, new AsyncHttpResponseHandler() {
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
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
