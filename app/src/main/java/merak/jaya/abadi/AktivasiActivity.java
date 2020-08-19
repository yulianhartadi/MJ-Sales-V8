package merak.jaya.abadi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;

public class AktivasiActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_device_aktif, btn_Aktivasi_Device, btn_help;
    EditText edtDeviceCode, edtAktivasiDevice;
    String number;

    private static final String Section = "mypref";
    private static final String DivideID = "DivideID";
    private static final String DivideStatus = "DivideStatus";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktivasi);
        number = getMyPhoneNO();
        Log.e("DiviceId", number);
        btn_device_aktif = findViewById(R.id.btn_device_aktif);
        btn_Aktivasi_Device = findViewById(R.id.btn_Aktivasi_Device);
        btn_help = findViewById(R.id.btn_help);
        edtDeviceCode = findViewById(R.id.edtDeviceCode);
        edtAktivasiDevice = findViewById(R.id.edtAktivasiDevice);
        btn_device_aktif.setOnClickListener(this);
        btn_Aktivasi_Device.setOnClickListener(this);
        btn_help.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(Section, MODE_PRIVATE);
        if (sharedPreferences.contains(DivideID)) {
            edtDeviceCode.setText(sharedPreferences.getString(DivideID, ""));
            edtDeviceCode.setEnabled(false);
        }
    }
    @SuppressLint("HardwareIds")
    private String getMyPhoneNO() {
        return Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    private void help() {
        String Sdialog = "Sebelum menggunakan Aplikasi ini, anda cukup ambil device code. Kemudian Masukkan Kode Aktivasi dengan menunjukan Device Code ke operator. Lalu Aktifkan";
        final DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                    case DialogInterface.BUTTON_NEUTRAL:
                        dialogInterface.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }

            }
        };
        AlertDialog.Builder Builder = new AlertDialog.Builder(this);
        Builder.setMessage(Sdialog).setPositiveButton("Mengerti", dialog).show();
//        super.onBackPressed();
    }
    private String codedevice(String s){
        String S1,S2,S3;
        S1=s.substring(0,5);
        S2=s.substring(6,10);
        S3=s.substring(11);
        Log.e( "codedevice: ", S1+"-"+S2+"-"+S3);
        String s1=new BigInteger(S1,16).toString(36);
        String s2=new BigInteger(S2,16).toString(36);
        String s3=new BigInteger(S3,16).toString(36);
        Log.e( "codedevice: ",  s1+"-"+s2+"-"+s3);
        return s1+"-"+s2+"-"+s3;
    }
    private String codeactivation(String s){
        String S1,S2,S3;
        S1=s.substring(0,5);
        S2=s.substring(6,10);
        S3=s.substring(11);
        String s1=new BigInteger(S1,16).toString(36);
        String s2=new BigInteger(S2,16).toString(36);
        String s3=new BigInteger(S3,16).toString(36);
        int i1=Integer.parseInt(s1,36)-1111;
        int i2=Integer.parseInt(s2,36)+3333;
        int i3=Integer.parseInt(s3,36)-5555;
        String p1=i1+""+i3;
        String p2=i2+"";
        String P1=new BigInteger(p1,10).toString(30);
        String P2=new BigInteger(p2,10).toString(30);
        Log.e("codeactivation: ", P1+"-"+P2);
        return P1+"-"+P2;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_device_aktif:
                edtDeviceCode.setText(codedevice(number));
                edtDeviceCode.setEnabled(false);
                ClipboardManager clipboard =(ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                assert clipboard != null;
                clipboard.setText(codedevice(number));
                editor = sharedPreferences.edit();
                editor.putString(DivideID, codedevice(number));
                editor.apply();
                Toast.makeText(AktivasiActivity.this, "Device Code Telah Dicopy",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_Aktivasi_Device:
                if (edtDeviceCode.getText().toString().equals("")) {
                    Toast.makeText(this, "Ambil Device Code", Toast.LENGTH_SHORT).show();
                } else if (edtAktivasiDevice.getText().toString().equals("")) {
                    Toast.makeText(this, "Masukkan Kode Aktivasi", Toast.LENGTH_SHORT).show();
                } else {
                    //cekaktifasi();
                    String saktif=edtAktivasiDevice.getText().toString();
                    if (saktif.equals(codeactivation(number))){
                        editor = sharedPreferences.edit();
                        editor.putString(DivideStatus, "Aktif");
                        editor.apply();
                        Toast.makeText(this, "Aktivasi Berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AktivasiActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(this, "Kode Aktivasi Anda Salah \n Silahkan hubungi Admin", Toast.LENGTH_SHORT).show();
                    }

                }

                break;
            case R.id.btn_help:
                help();
                break;
        }
    }
}
