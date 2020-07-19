package cc.asjks.bms.client.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONException;
import org.json.JSONObject;

import cc.asjks.bms.client.R;
import cc.asjks.bms.client.activity.BalanceActivity;
import cc.asjks.bms.client.activity.RegisterActivity;
import cc.asjks.bms.client.activity.ioMoneyActivity;
import cc.asjks.bms.client.activity.transferActivity;
import cc.asjks.bms.client.adapter.Menu_HomeAdpager;
import cc.asjks.bms.client.model.Ad;
import cc.asjks.bms.client.model.MenuBean;
import cc.asjks.bms.client.model.UserInfo;
import cc.asjks.bms.client.util.Config;
import cc.asjks.bms.client.util.GlideImageLoader;
import cc.asjks.bms.client.util.HttpUtil;
import cc.asjks.bms.client.util.UtilDialog;

import static cc.asjks.bms.client.util.Config.ad;
import static java.lang.Thread.sleep;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private Banner banner;
    private GridView menuGrid;
    private HttpUtil httpUtil;
    private  Menu_HomeAdpager homeAdpager;
    private List<MenuBean> menuBeans;
    static Ad adInfo;
    UserInfo userInfo;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        menuGrid=view.findViewById(R.id.index_gv);
        banner=view.findViewById(R.id.banner);

        userInfo=Config.userInfo;
        adInfo=ad;
        context=getContext();
        httpUtil=new HttpUtil(context);
        getData();
        return view;
    }

    public void init(){
        initLBData();
        initMenu();
    }

    //设置轮播图片
    private void initLBData(){
        List images = new ArrayList();
        List title = new ArrayList();

        for(int i=0;i<(adInfo.getData().size());i++){
            //byte[] bitmap=Base64.decode(adInfo.getData().get(i).getImg().split(",")[1], Base64.DEFAULT);
            byte[] bitmap=Base64.decode(adInfo.getData().get(i).getImg().split(",")[1],0);
            images.add(bitmap);
        }
        images.add(R.mipmap.v3);

        for(int i=0;i<(adInfo.getData().size());i++){
            title.add(adInfo.getData().get(i).getContent());
        }
        title.add("Android");
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setBannerAnimation(Transformer.Default);
        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerTitles(title);
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    //设置GridView图标
    private void initMenu(){
        menuBeans=new ArrayList<MenuBean>();
        menuBeans.add(new MenuBean(R.mipmap.zz,"转账"));
        menuBeans.add(new MenuBean(R.mipmap.cq,"存款/取款"));
        menuBeans.add(new MenuBean(R.mipmap.ye,"查询余额"));
        homeAdpager=new Menu_HomeAdpager(getActivity(),menuBeans);
        menuGrid.setAdapter(homeAdpager);
        menuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        getActivity().startActivity(new Intent(getActivity(), transferActivity.class));
                        break;
                    case 1:
                        getActivity().startActivity(new Intent(getActivity(), ioMoneyActivity.class));
                        break;
                    case 2:
                        getActivity().startActivity(new Intent(getActivity(),BalanceActivity.class));
                        break;
                }
            }
        });

    }
    private void getData(){
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
                httpUtil.httpPost("queryAllAdByTime", jsonObject, handler, Ad.class);
                Log.i("http",""+jsonObject.toString());
            }
        }, 1000);
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                adInfo=(Ad)msg.obj;
                init();
            } else if (msg.what == 1) {
                Toast.makeText(context, ""+msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
