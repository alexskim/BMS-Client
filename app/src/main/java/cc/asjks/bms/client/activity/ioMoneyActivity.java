package cc.asjks.bms.client.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cc.asjks.bms.client.R;
import cc.asjks.bms.client.model.UserInfo;
import cc.asjks.bms.client.util.Config;
import cc.asjks.bms.client.util.HttpUtil;
import cc.asjks.bms.client.util.UtilDialog;

public class ioMoneyActivity extends AppCompatActivity {

    private EditText editTextIOMoneyAmount;
    private Spinner spinnerIOMoneyCurrency;
    private Button buttonIOMoneyIn,buttonIOMoneyOut;
    private ArrayAdapter arrayAdapter;
    private HttpUtil httpUtil;
    private String currency;
    private String type;
    Context context;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_io_money);

        editTextIOMoneyAmount=findViewById(R.id.editTextIOMoneyAmount);
        spinnerIOMoneyCurrency=findViewById(R.id.spinnerIOMoneyCurrency);
        buttonIOMoneyIn=findViewById(R.id.buttonIOMoneyIn);
        buttonIOMoneyOut=findViewById(R.id.buttonIOMoneyOut);

        context=ioMoneyActivity.this;
        userInfo= Config.userInfo;
        httpUtil=new HttpUtil(context);

        arrayAdapter=ArrayAdapter.createFromResource(this, R.array.plantes, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIOMoneyCurrency.setAdapter(arrayAdapter);
        spinnerIOMoneyCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context,""+arrayAdapter.getItem(position),Toast.LENGTH_LONG).show();
                currency=(String)arrayAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currency=null;
            }
        });

        buttonIOMoneyIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextIOMoneyAmount.getText().toString().equals("")){
                    Toast.makeText(context, "请输入完整", Toast.LENGTH_SHORT).show();
                }else{
                    type="1";
                    ioMoney();
                }

            }
        });

        buttonIOMoneyOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextIOMoneyAmount.getText().toString().equals("")){
                    Toast.makeText(context, "请输入完整", Toast.LENGTH_SHORT).show();
                }else{
                    type="2";
                    ioMoney();
                }

            }
        });
    }

    private void ioMoney() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token",userInfo.getToken());
            jsonObject.put("currency", currency);
            jsonObject.put("amount", editTextIOMoneyAmount.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UtilDialog.showDialog(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UtilDialog.disDialog();
                switch (type){
                    case "1":{
                        httpUtil.httpPost("deposit", jsonObject, handler, null);
                        break;
                    }
                    case "2":{
                        httpUtil.httpPost("withdraw", jsonObject, handler, null);
                        break;
                    }
                }
                Log.i("http",""+jsonObject.toString());
            }
        }, 1000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Toast.makeText(context, "处理成功", Toast.LENGTH_SHORT).show();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String Time=sdf.format(new Date());
                Bundle b=new Bundle();
                b.putString("amount",editTextIOMoneyAmount.getText().toString().trim());
                b.putString("currency",currency);
                b.putString("type",type);
                b.putString("time",Time);
                Intent intent=new Intent();
                intent.setClass(context,SuccessActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            } else if (msg.what == 1) {
                Toast.makeText(context, ""+msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
