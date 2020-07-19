package cc.asjks.bms.client.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cc.asjks.bms.client.model.Money;
import cc.asjks.bms.client.model.UserInfo;
import cc.asjks.bms.client.util.Config;
import cc.asjks.bms.client.util.HttpUtil;

import cc.asjks.bms.client.R;
import cc.asjks.bms.client.util.UtilDialog;

import static cc.asjks.bms.client.util.Config.userInfo;
import static cc.asjks.bms.client.util.Config.money;

public class BalanceActivity extends AppCompatActivity {

    private TextView textViewCNY,textViewUSD;
    private Button buttonRefreshCNY,buttonRefreshUSD;
    private HttpUtil httpUtil;
    Context context;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        textViewCNY=findViewById(R.id.textViewCNY);
        textViewUSD=findViewById(R.id.textViewUSD);

        buttonRefreshCNY=findViewById(R.id.buttonRefreshCNY);
        buttonRefreshUSD=findViewById(R.id.buttonRefreshUSD);

        context=BalanceActivity.this;

        userInfo=Config.userInfo;
        httpUtil=new HttpUtil(context);

        buttonRefreshCNY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textViewCNY.setText("100￥");
                refresh("CNY");
            }
        });

        buttonRefreshUSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textViewUSD.setText("100$");
                refresh("USD");
            }
        });
    }

    private void refresh(String currency) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token",userInfo.getToken());
            jsonObject.put("currency", currency);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UtilDialog.showDialog(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UtilDialog.disDialog();
                httpUtil.httpPost("queryBalance", jsonObject, handler, Money.class);
                Log.i("http",""+jsonObject.toString());
            }
        }, 1000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                money = (Money) msg.obj;
                switch (money.getCurrency()){
                    case "CNY":{
                        textViewCNY.setText(money.getAmount().toString()+"￥");
                        break;
                    }
                    case "USD":{
                        textViewUSD.setText(money.getAmount().toString()+"$");
                        break;
                    }
                    default:{
                        break;
                    }
                }

            } else if (msg.what == 1) {
                Toast.makeText(context, ""+msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}