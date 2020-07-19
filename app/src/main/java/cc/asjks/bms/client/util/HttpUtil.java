package cc.asjks.bms.client.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络工具类==Volley框架
 */
public class HttpUtil {
    private Context context;
    private RequestQueue queue;
    private static final String TAG_ = "llh_HttpUtil";
    private Gson gson = new Gson();

    public HttpUtil(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }
    public void httpPost(String url, final JSONObject parameter, final Handler handler, final Class clazz) {
        String Url = "http://"+Config.IP+":"+Config.PORT+"/bms/api/" + url;
        Log.i(TAG_, "httpPost: "+Url);
        Log.i(TAG_, parameter.toString());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, Url, parameter, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Message message = new Message();
                Log.i(TAG_, jsonObject.toString());
                if (jsonObject.opt("result").equals("1")) {
                    message.what = 0;
                   if(clazz!=null){
                       //gson解析
                       try{
                           message.obj = gson.fromJson(jsonObject.toString(), clazz);
                       }catch (Exception e){
                           try {
                               message.obj = gson.fromJson(jsonObject.getJSONArray("data").toString(), clazz);
                           } catch (Exception ex) {
                               ex.printStackTrace();
                           }

                       }

                   }
                }else{
                    message.what = 1;
                    message.obj=jsonObject.opt("msgerr").toString();
                }
                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG_, "httpPost: "+volleyError.toString());
                Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jor);
    }
}
