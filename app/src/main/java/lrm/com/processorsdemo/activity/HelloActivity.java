package lrm.com.processorsdemo.activity;

import android.support.annotation.NonNull;

import lrm.com.annotations.HelloWordAtion;
import lrm.com.processorsdemo.R;

/**
 * 创建日期：2019/6/11
 * 作者:baiyang
 */
@HelloWordAtion("Hello初探")
public class HelloActivity extends BaseActivity {
    @NonNull
    @Override
    protected String getActionBarTitle() {
        return "hello";
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initVariables() {

    }
}
