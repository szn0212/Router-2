package com.chenenyu.router.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chenenyu.router.RouteCallBack;
import com.chenenyu.router.RouteTable;
import com.chenenyu.router.Router;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editRoute;
    private String uri;
    private Button btn0, btn1, btn2, btn3, btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editRoute = (EditText) findViewById(R.id.edit_route);
        btn0 = (Button) findViewById(R.id.btn0);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        editRoute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                uri = s.toString();
                btn0.setText(getString(R.string.go_to, s));
            }
        });

        // 动态添加路由表
        Router.addRouteTable(new RouteTable() {
            @Override
            public void handleActivityTable(Map<String, Class<? extends Activity>> map) {
                map.put("dynamic", DynamicActivity.class);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btn0) {
            Router.build(uri).callback(new RouteCallBack() { // 添加结果回调
                @Override
                public void succeed(Uri uri) {
                    Toast.makeText(MainActivity.this, "succeed: " + uri.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void error(Uri uri, String message) {
                    Toast.makeText(MainActivity.this, "error: " + uri + ", " + message, Toast.LENGTH_SHORT).show();
                }
            }).go(this);
        } else if (v == btn1) {
            Router.build(btn1.getText().toString()).go(this);
        } else if (v == btn2) {
            Router.build("dynamic").go(this);
        } else if (v == btn3) {
            Bundle bundle = new Bundle();
            bundle.putString("extra", "Bundle from MainActivity.");
            Router.build("result").requestCode(0).extras(bundle).go(this);
        } else if (v == btn4) {
            Router.build("test")
                    .anim(android.R.anim.fade_in, android.R.anim.fade_out).go(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String result = data.getStringExtra("extra");
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
