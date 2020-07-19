package cc.asjks.bms.client.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.asjks.bms.client.R;

public class SuccessActivity extends AppCompatActivity {

    private TextView tvSuccessAmount,tvSuccessCurrency,tvSuccessType,tvSuccessToUid,tvSuccessTime;
    private LinearLayout llType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        tvSuccessAmount=findViewById(R.id.tvSuccessAmount);
        tvSuccessCurrency=findViewById(R.id.tvSuccessCurrency);
        tvSuccessType=findViewById(R.id.tvSuccessType);
        tvSuccessToUid=findViewById(R.id.tvSuccessToUid);
        tvSuccessTime=findViewById(R.id.tvSuccessTime);
        llType=findViewById(R.id.llType);

        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        Double amount=Double.valueOf(b.getString("amount"));
        String currency=b.getString("currency");
        String type=b.getString("type");
        String to=b.getString("to");
        String time=b.getString("time");

        if(!type.equals("0")){
            llType.setVisibility(View.GONE);
        }else{
            tvSuccessToUid.setText(to);
        }

        tvSuccessCurrency.setText(currency);
        switch (type){
            case "0":{
                tvSuccessType.setText("转账");
                tvSuccessAmount.setText("- "+amount);
                break;
            }
            case "1":{
                tvSuccessType.setText("存款");
                tvSuccessAmount.setText("+ "+amount);
                break;
            }
            case "2":{
                tvSuccessType.setText("取款");
                tvSuccessAmount.setText("- "+amount);
                break;
            }
            case "3":{
                tvSuccessType.setText("后台修改");
                tvSuccessAmount.setText(""+amount);
            }
        }

        tvSuccessTime.setText(time);
    }
}
