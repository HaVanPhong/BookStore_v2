package com.example.bookstore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.Database.Account;
import com.example.bookstore.Database.SQLHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    TextView tvforget;
    Button btnLogin, btnRegister;
    TextInputEditText edtUsername;
    EditText edtPassword;
    CheckBox checkBoxSaveAcc;
    String ten, mk;
    TextInputLayout ip2;

    static  final  String SHARE_PRE_NAME="account";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AnhXa();
        String tvf="<u>forget your account</u>";
        tvforget.setText(Html.fromHtml(tvf));
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        ip2=findViewById(R.id.ip2);
        ip2.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);




        SQLHelper sqlHelper=new SQLHelper(Login.this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(Login.this);
                dialog.setTitle("ĐIỀN THÔNG TIN");
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_dialog_register);

                final EditText edtUsernameRe=dialog.findViewById(R.id.edtUsernameRe),
                        edtPasswordRe=dialog.findViewById(R.id.edtPasswordRe),
                        edtPasswordReAgain=dialog.findViewById(R.id.edtPasswordReAgain);
                Button btnHuy=(Button) dialog.findViewById(R.id.btnhuy);
                Button btnOk=(Button) dialog.findViewById(R.id.btnOk);

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtPasswordRe.getText().toString().compareTo(edtPasswordReAgain.getText().toString())!=0){
                            Toast.makeText(Login.this, "MK không trùng khớp", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            ten=edtUsernameRe.getText().toString();
                            mk=edtPasswordRe.getText().toString();
                            edtUsername.setText(ten);
                            edtPassword.setText(mk);
                            boolean cc= sqlHelper.insertAccount(new Account(ten, mk));
                            if(cc){
                                Toast.makeText(Login.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                            else
                                Toast.makeText(Login.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten=edtUsername.getText().toString();
                String mk=edtPassword.getText().toString();
                Account account=new Account(ten, mk);
                boolean cc= sqlHelper.checkLogin(account);
                if (cc){
                    if(checkBoxSaveAcc.isChecked()){
                        SaveAccount(edtUsername.getText().toString(), edtPassword.getText().toString());
                    }else {
                        SaveAccount("", "");
                    }
                    Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent1=new Intent(Login.this, MainActivity.class);
                    intent1.putExtra("account", (Parcelable) account);
                    startActivity(intent1);
                }
                else
                    Toast.makeText(Login.this, "Tài khoản hoặc mật khẩu chưa đúng", Toast.LENGTH_SHORT).show();

            }
        });

        tvforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Hệ thống quên mật khẩu đang bảo trì", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SaveAccount(String username, String password){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARE_PRE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();

    }
    private void AnhXa() {
        tvforget=findViewById(R.id.tvforget);
        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnRegister);
        edtPassword=findViewById(R.id.edtPassword);
        edtUsername=findViewById(R.id.edtUsername);
        checkBoxSaveAcc=findViewById(R.id.SaveAcc);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences=getSharedPreferences(SHARE_PRE_NAME, MODE_PRIVATE);
        String username=sharedPreferences.getString("username", "");
        String password=sharedPreferences.getString("password", "");
        edtUsername.setText(username);
        edtPassword.setText(password);
        checkBoxSaveAcc.setChecked(true);
    }
}