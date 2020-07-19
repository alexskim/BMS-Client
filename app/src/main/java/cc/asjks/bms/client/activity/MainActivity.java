package cc.asjks.bms.client.activity;

import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cc.asjks.bms.client.R;
import cc.asjks.bms.client.fragment.DetailsFragment;
import cc.asjks.bms.client.fragment.HomeFragment;
import cc.asjks.bms.client.fragment.MineFragment;

public class MainActivity extends AppCompatActivity {
    private long mExitTime = 0;
    private BottomNavigationView mBottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        initView();
        initBottomNavigation();
        getFragmentManager().beginTransaction().replace(R.id.maincontent, new HomeFragment()).commit();
    }
    private   void initView(){

        mBottomNavigationView=findViewById(R.id.nav_view);
    }
    private void initBottomNavigation() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:{
                        getFragmentManager().beginTransaction().replace(R.id.maincontent, new HomeFragment()).commit();
                        break;
                    }
                    case R.id.navigation_details:{
                        getFragmentManager().beginTransaction().replace(R.id.maincontent, new DetailsFragment()).commit();
                        break;
                    }
                    case R.id.navigation_person:{
                        getFragmentManager().beginTransaction().replace(R.id.maincontent, new MineFragment()).commit();
                        break;
                    }
                    default:
                        break;
                }
                // 这里注意返回true,否则点击失效
                return true;
            }
        });
    }


    private boolean isExit=false;
    @Override
    public void onBackPressed() {
        if (isExit){
            super.onBackPressed();
        }else {
            isExit=true;
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit=false;
                }
            }, 3000);
        }
    }
}
