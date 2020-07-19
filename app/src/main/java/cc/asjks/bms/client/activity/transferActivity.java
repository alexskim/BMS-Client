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
import cc.asjks.bms.client.model.Money;
import cc.asjks.bms.client.model.UserInfo;
import cc.asjks.bms.client.util.Config;
import cc.asjks.bms.client.util.HttpUtil;
import cc.asjks.bms.client.util.UtilDialog;

import static cc.asjks.bms.client.util.Config.money;

public class transferActivity extends AppCompatActivity {

    private EditText editTextToUid,editTextAmount;
    private Spinner spinnerCurrency;
    private Button buttonTransferSubmit;
    private ArrayAdapter arrayAdapter;
    private HttpUtil httpUtil;
    private String currency;
    Context context;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        editTextToUid=findViewById(R.id.editTextToUid);
        editTextAmount=findViewById(R.id.editTextAmount);
        spinnerCurrency=findViewById(R.id.spinnerCurrency);
        buttonTransferSubmit=findViewById(R.id.buttonTransferSubmit);

        context=transferActivity.this;
        userInfo= Config.userInfo;
        httpUtil=new HttpUtil(context);

        arrayAdapter=ArrayAdapter.createFromResource(this, R.array.plantes, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(arrayAdapter);
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(transferActivity.this,""+arrayAdapter.getItem(position),Toast.LENGTH_LONG).show();
                currency=(String)arrayAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currency=null;
            }
        });

        buttonTransferSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextToUid.getText().toString().equals("")){
                    Toast.makeText(context, "请输入完整", Toast.LENGTH_SHORT).show();
                }else if(editTextAmount.getText().toString().equals("")){
                    Toast.makeText(context, "请输入完整", Toast.LENGTH_SHORT).show();
                }else{
                    transfer();
                }

            }
        });
    }

    private void transfer() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token",userInfo.getToken());
            jsonObject.put("currency", currency);
            jsonObject.put("to", editTextToUid.getText().toString());
            jsonObject.put("amount", editTextAmount.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UtilDialog.showDialog(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UtilDialog.disDialog();
                httpUtil.httpPost("transferAccounts", jsonObject, handler, null);
                Log.i("http",""+jsonObject.toString());
            }
        }, 1000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Toast.makeText(context, "转账成功", Toast.LENGTH_SHORT).show();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String Time=sdf.format(new Date());
                Bundle b=new Bundle();
                b.putString("amount",editTextAmount.getText().toString().trim());
                b.putString("currency",currency);
                b.putString("type","0");
                b.putString("to",editTextToUid.getText().toString());
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
