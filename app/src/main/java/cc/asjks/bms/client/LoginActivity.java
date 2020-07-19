package cc.asjks.bms.client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cc.asjks.bms.client.activity.MainActivity;
import cc.asjks.bms.client.activity.RegisterActivity;
import cc.asjks.bms.client.model.UserInfo;
import cc.asjks.bms.client.util.Config;
import cc.asjks.bms.client.util.HttpUtil;
import cc.asjks.bms.client.util.UtilDialog;

import org.json.JSONException;
import org.json.JSONObject;

import cc.asjks.bms.client.activity.MainActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText login_user;
    private EditText login_pwd;
    private CheckBox login_check;
    private Button login_ok;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private HttpUtil httpUtil;
    private String user, pwd;
    private TextView login_register,ip_set,port_set;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context=LoginActivity.this;
        sp = getSharedPreferences("login", MODE_PRIVATE);
        httpUtil = new HttpUtil(this);
        initView();
    }

    private void initView() {
        login_user =findViewById(R.id.login_user);
        login_register = findViewById(R.id.login_register);
        login_pwd =  findViewById(R.id.login_pwd);
        login_check =findViewById(R.id.login_check);
        login_ok = findViewById(R.id.login_ok);
        ip_set=findViewById(R.id.ip_set);
        port_set=findViewById(R.id.port_set);

        if (sp.getBoolean("check", false)) {
            login_check.setChecked(true);
        }
        login_user.setText(sp.getString("user", ""));
        login_pwd.setText(sp.getString("pwd", ""));
        login_ok.setOnClickListener(this);
        login_register.setOnClickListener(this);
        ip_set.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_ok:
                ip_config();
                submit();
                break;
            case R.id.login_register:
                ip_config();
                startActivity(new Intent(context, RegisterActivity.class));
                break;
        }
    }

    private  void ip_config(){
        Config.IP=ip_set.getText().toString();
        Config.PORT=port_set.getText().toString();
    }
    private void submit() {
        // validate
        user = login_user.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(this, "请输入登录账号", Toast.LENGTH_SHORT).show();
            return;
        }
        pwd = login_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入登录密码", Toast.LENGTH_SHORT).show();
            return;
        }

        checkLogin();
    }

    private void checkLogin() {
        editor = sp.edit();
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", user);
            jsonObject.put("password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UtilDialog.showDialog(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UtilDialog.disDialog();
                httpUtil.httpPost("login", jsonObject, handler, UserInfo.class);
                Log.i("http",""+jsonObject.toString());
            }
        }, 1000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Config.userInfo = (UserInfo) msg.obj;
                if (login_check.isChecked()) {
                    editor.putBoolean("check", true);
                    editor.putString("user", user);
                    editor.putString("pwd", pwd);
                    editor.commit();
                } else {
                    editor.putBoolean("check", false);
                    editor.putString("pwd", "");
                    editor.commit();
                }

                if (Config.userInfo.getData().getRole().equals("1")) {
                    startActivity(new Intent(context, MainActivity.class));
                    finish();

                } else {
                    startActivity(new Intent(context, MainActivity.class));
                    finish();
                }
            } else if (msg.what == 1) {
                Toast.makeText(context, ""+msg.obj, Toast.LENGTH_SHORT).show();
                login_pwd.setText("");
            }
        }
    };
}
