package cc.asjks.bms.client.fragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cc.asjks.bms.client.R;
import cc.asjks.bms.client.activity.RegisterActivity;
import cc.asjks.bms.client.model.UserInfo;
import cc.asjks.bms.client.util.Config;
import cc.asjks.bms.client.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment implements View.OnClickListener{
    private TextView tv_name,tv_truename,tv_regTime,tv_phone,tv_role;
    private ImageView img_back;
    UserInfo userInfo;
    HttpUtil httpUtil;
    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_personal,null);
       userInfo=Config.userInfo;
       httpUtil=new HttpUtil(getActivity());
       initView(view);
       initData();
       return view;
    }
    private void initView(View view){

        tv_name=view.findViewById(R.id.user_info_uname);
        tv_truename=view.findViewById(R.id.user_info_tuser);
        tv_phone=view.findViewById(R.id.user_info_tel);

        tv_regTime=view.findViewById(R.id.user_info_date);
        tv_role=view.findViewById(R.id.user_info_role);

        img_back=view.findViewById(R.id.user_info_imgback);

    }
    private  void initData(){
        Log.i("test",userInfo.getData().getUsername());
        String uname=userInfo.getData().getUsername();
        if (TextUtils.isEmpty(userInfo.getData().getUsername())) {
            tv_name.setText("未填");
        } else {
            tv_name.setText(userInfo.getData().getUsername());
        }
        String utel=userInfo.getData().getTel();
        if (TextUtils.isEmpty(utel)) {
            tv_phone.setText("未填");
        } else {
            tv_phone.setText(userInfo.getData().getTel());
        }

         tv_role.setText(userInfo.getData().getRole());
         tv_regTime.setText(userInfo.getData().getRegTime());
         tv_truename.setText(userInfo.getData().getTrueName());

        tv_truename.setOnClickListener(this);

        img_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_tuser:
                View view = View.inflate(getActivity(), R.layout.dialog_name, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final TextView tname = view.findViewById(R.id.dialog_edt_tname);
                final TextView ttel = view.findViewById(R.id.dialog_edt_tel);
                builder.setView(view);
                final AlertDialog dialog = builder.create();

                view.findViewById(R.id.dialog_btn_tname_update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name=tname.getText().toString();
                        String tel=ttel.getText().toString();
                        if(TextUtils.isEmpty(name)){
                            Toast.makeText(getActivity(),"请输入",Toast.LENGTH_LONG).show();
                        }
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("token",userInfo.getToken());
                            jsonObject.put("uid",userInfo.getData().getUid());
                            jsonObject.put("trueName",name);
                            jsonObject.put("tel",tel);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        httpUtil.httpPost("updateInfo",jsonObject,handler,null);
                        tv_truename.setText(name);
                        tv_phone.setText(tel);
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.dialog_btn_tname_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
            case R.id.user_info_imgback:
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null) //将当前fragment加入到返回栈中
                        .replace(R.id.maincontent, new MineFragment()).commit();

                break;
        }
    }
   private Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           if (msg.what==0){

               Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();

           }else {
               Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
           }
       }
   };
}
