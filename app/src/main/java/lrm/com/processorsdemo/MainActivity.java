package lrm.com.processorsdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import lrm.com.processorsdemo.activity.BindViewActivity;
import lrm.com.processorsdemo.activity.HelloActivity;

/**
 * 参考https://blog.csdn.net/qq_20521573/article/details/82321755
 * https://www.race604.com/annotation-processing/
 * https://blog.csdn.net/u013045971/article/details/53509237
 * https://blog.csdn.net/wzgiceman/article/details/54580745  主要参考文章
 * https://blog.csdn.net/wzgiceman/article/details/54375109
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private Button btn_hello;
    private Button btn_bindview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        btn_hello = (Button) findViewById(R.id.btn_hello);
        btn_bindview = (Button) findViewById(R.id.btn_bindview);
        btn_hello.setOnClickListener(this);
        btn_bindview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_hello:
                startActivity(new Intent(mContext, HelloActivity.class));
                break;
            case R.id.btn_bindview:
                startActivity(new Intent(mContext, BindViewActivity.class));
                break;
        }
    }
}
