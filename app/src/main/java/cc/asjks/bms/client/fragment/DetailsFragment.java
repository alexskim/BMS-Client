package cc.asjks.bms.client.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cc.asjks.bms.client.R;
import cc.asjks.bms.client.activity.SuccessActivity;
import cc.asjks.bms.client.model.Details;
import cc.asjks.bms.client.model.UserInfo;
import cc.asjks.bms.client.util.Config;
import cc.asjks.bms.client.util.HttpUtil;
import cc.asjks.bms.client.util.UtilDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cc.asjks.bms.client.util.Config.details;

public class DetailsFragment extends Fragment{
    private List<Map<String,Object>> dataList=new ArrayList<>();
    private BaseAdapter myBaseAdapter;
    private ListView lvDetails;
    private HttpUtil httpUtil;
    UserInfo userInfo;
    static Details detailsInfo;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_details,null);
        lvDetails = view.findViewById(R.id.lvDetails);
        userInfo=Config.userInfo;
        detailsInfo=details;
        context=getActivity();
        httpUtil=new HttpUtil(context);
        getDate();
        return view;
    }

    public void init(){
        dataList.clear();
        initData();
        initView();
    }
    public void initData(){
        Map<String,Object> map;
        for(int i=0;i<(detailsInfo.getData().size());i++){
            map=new HashMap<>();
            map.put("id",detailsInfo.getData().get(i).getId());
            map.put("uid",detailsInfo.getData().get(i).getUid());
            map.put("date",detailsInfo.getData().get(i).getDate());
            map.put("toUid",detailsInfo.getData().get(i).getToUid());
            map.put("amount",detailsInfo.getData().get(i).getAmount());
            map.put("currency",detailsInfo.getData().get(i).getCurrency());
            map.put("type",detailsInfo.getData().get(i).getType());
            dataList.add(map);
        }
    }

    public void initView(){
        myBaseAdapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return dataList.size();
            }

            @Override
            public Object getItem(int position) {
                return dataList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if(convertView==null){
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_details_list,null);
                }
                TextView details_list_type=convertView.findViewById(R.id.details_list_type);
                TextView details_list_amount=convertView.findViewById(R.id.details_list_amount);
                TextView details_list_currency=convertView.findViewById(R.id.details_list_currency);

                final Map<String,Object> map=dataList.get(position);
                final String details_id=String.valueOf(map.get("id"));
                final String details_uid=String.valueOf(map.get("uid"));
                final String details_date=(String)map.get("date");
                final String details_toUid=String.valueOf(map.get("toUid"));
                final Double details_amount=(Double)map.get("amount");
                final String details_currency=(String)map.get("currency");
                final String details_type=(String)map.get("type");

                switch (details_type){
                    case "0":{
                        details_list_type.setText("转账");
                        break;
                    }
                    case "1":{
                        details_list_type.setText("存款");
                        break;
                    }
                    case "2":{
                        details_list_type.setText("取款");
                        break;
                    }
                    case "3":{
                        details_list_type.setText("后台修改");
                    }
                }
                details_list_amount.setText(String.valueOf(details_amount));
                details_list_currency.setText(details_currency);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle b=new Bundle();
                        b.putString("amount",String.valueOf(details_amount));
                        b.putString("currency",details_currency);
                        b.putString("type",details_type);
                        b.putString("to",details_toUid);
                        b.putString("time",details_date);
                        Intent intent=new Intent();
                        intent.setClass(context, SuccessActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
                return convertView;
            }
        };
        lvDetails.setAdapter(myBaseAdapter);
    }
    private void getDate() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token",userInfo.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UtilDialog.showDialog(context);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UtilDialog.disDialog();
                httpUtil.httpPost("queryDetails", jsonObject, handler, Details.class);
                Log.i("http",""+jsonObject.toString());
            }
        }, 1000);
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                detailsInfo = (Details) msg.obj;
                init();
            } else if (msg.what == 1) {
                Toast.makeText(context, ""+msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

}
