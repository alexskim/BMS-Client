package cc.asjks.bms.client.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cc.asjks.bms.client.R;
import cc.asjks.bms.client.model.MenuBean;


import java.util.List;

public  class Menu_HomeAdpager extends BaseAdapter {
    Context context;
    List<MenuBean> datas;

    public Menu_HomeAdpager(Context context, List<MenuBean> datas) {
        this.context = context;
        this.datas=datas;
        Log.d("test", datas.size()+"  ");
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        if(convertView ==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_home_menu,null);
            viewHolder.textView=convertView.findViewById(R.id.tv_home_menuitem);
            viewHolder.imageView=convertView.findViewById(R.id.img_home_menuitem);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        MenuBean bean=datas.get(position);
        viewHolder.textView.setText(bean.getTxt());
        Log.d("test", bean.getTxt());
        viewHolder.imageView.setImageResource((Integer) bean.getImg());
        return convertView;
    }
   static class  ViewHolder{
        TextView textView;
        ImageView imageView;
    }
}
