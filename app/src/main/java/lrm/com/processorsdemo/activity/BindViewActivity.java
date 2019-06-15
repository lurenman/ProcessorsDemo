package lrm.com.processorsdemo.activity;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import lrm.com.annotations.BindView;
import lrm.com.api.LrmJector;
import lrm.com.processorsdemo.R;

/**
 * 创建日期：2019/6/15
 * 作者:baiyang
 */
public class BindViewActivity extends BaseActivity {
    @BindView(R.id.btn_test)
    Button btn_test;
    @BindView(R.id.btn_click)
    Button btn_click;

    @NonNull
    @Override
    protected String getActionBarTitle() {
        return "bindView";
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bindview;
    }

    @Override
    protected void initView() {
        LrmJector.bind(this);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initListenter() {
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "btn_test已经初始化绑定成功", Toast.LENGTH_SHORT).show();
            }
        });
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "btn_click已经初始化绑定成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
