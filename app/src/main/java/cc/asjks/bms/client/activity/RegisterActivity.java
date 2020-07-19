package cc.asjks.bms.client.activity;

import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cc.asjks.bms.client.R;
import cc.asjks.bms.client.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView register_img;
    private EditText register_name;
    private EditText register_pswd1;
    private EditText register_pswd2;
    private Button register_ok;
    private HttpUtil httpUtil;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(RegisterActivity.this, ""+msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        httpUtil=new HttpUtil(this);

        initView();
    }

    private void initView() {
        register_img = findViewById(R.id.register_img);
        register_name = findViewById(R.id.register_name);
        register_pswd1 =findViewById(R.id.register_pswd1);
        register_pswd2 =  findViewById(R.id.register_pswd2);
        register_ok = findViewById(R.id.register_ok);

        register_ok.setOnClickListener(this);
        register_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_ok:
                submit();
                break;
            case R.id.register_img:
                finish();
                break;
        }
    }

    private void submit() {
        // validate
        String name = register_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        String pswd1 = register_pswd1.getText().toString().trim();
        if (TextUtils.isEmpty(pswd1)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String pswd2 = register_pswd2.getText().toString().trim();
        if (TextUtils.isEmpty(pswd2)) {
            Toast.makeText(this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!pswd1.equals(pswd2)){
            Toast.makeText(this, "两次密码不一样", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO validate success, do something
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("username",name);
            jsonObject.put("password",pswd1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpUtil.httpPost("register",jsonObject,handler,null);
    }
}
