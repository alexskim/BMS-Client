package cc.asjks.bms.client.fragment;

 
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cc.asjks.bms.client.LoginActivity;
import cc.asjks.bms.client.R;
import cc.asjks.bms.client.activity.MainActivity;

import cc.asjks.bms.client.model.UserInfo;
import cc.asjks.bms.client.util.Config;
import cc.asjks.bms.client.util.HttpUtil;
import cc.asjks.bms.client.util.UtilDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MineFragment extends Fragment {
    private String title[] = {"个人资料","交易明细"};
    private int img[] = {R.mipmap.user_info,R.mipmap.user_mxi};
    private ListView mine_lv;
    private List<Map<String, Object>> data = new ArrayList<>();
    private TextView mine_name;
    private Button mine_logout,mine_delete;
    private ImageView mine_img;
    private HttpUtil httpUtil;
    Context context;
    UserInfo userInfo;
    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mine,null);
        userInfo= Config.userInfo;
        context=getContext();
        httpUtil=new HttpUtil(context);
        getData();
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        mine_lv = view.findViewById(R.id.mine_lv);
        mine_name =  view.findViewById(R.id.mine_name);
        mine_img = view.findViewById(R.id.mine_img);
        mine_logout =view.findViewById(R.id.mine_logout);
        mine_delete =view.findViewById(R.id.mine_delete);
        mine_name=view.findViewById(R.id.mine_name);
        mine_name.setText(userInfo.getData().getUsername());
    }

    private void getData() {
        data.clear();
        for (int i = 0; i < title.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", title[i]);
            map.put("img", img[i]);
            data.add(map);
        }
    }
    private void initEvent(){
        //列表菜单
        mine_lv.setAdapter(new SimpleAdapter(context, data, R.layout.item_mine_list, new String[]{"title", "img"}, new int[]{
                R.id.mine_list_title, R.id.mine_list_img}));

        //   mine_name.setText(Config.userInfo.getData().getUsername() + "");


        mine_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(new Intent(context, LoginActivity.class));
            }
        });
        mine_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad=new AlertDialog.Builder(context);
                ad.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser();
                    }
                });
                ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.setMessage("你确定要删除账号吗?");
                ad.setTitle("提示");
                ad.show();
            }
        });

        mine_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:{
                        getActivity().getFragmentManager().beginTransaction().replace(R.id.maincontent, new PersonalFragment()).commit();
                        break;
                    }
                    case 1:{
                        getFragmentManager().beginTransaction().replace(R.id.maincontent, new DetailsFragment()).commit();
                        break;
                    }
                }
            }
        });
    }

    private void deleteUser() {
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
                httpUtil.httpPost("deleteUser", jsonObject, handler, null);
                Log.i("http",""+jsonObject.toString());
            }
        }, 1000);
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Toast.makeText(context,"注销成功",Toast.LENGTH_LONG).show();
                getActivity().finish();
                startActivity(new Intent(context, LoginActivity.class));
            } else if (msg.what == 1) {
                Toast.makeText(context, ""+msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
