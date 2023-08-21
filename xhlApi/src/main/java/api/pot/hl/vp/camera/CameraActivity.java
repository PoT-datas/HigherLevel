package api.pot.hl.vp.camera;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import api.pot.gl.xiv.XImageView;
import api.pot.gl.xiv.tools.Forgrounder;
import api.pot.hl.R;
import api.pot.system.Log;
import api.pot.system.XApp;
import api.pot.system.XtatutBar;
import api.pot.system.permissions.XPermission;
import api.pot.system.permissions.XPermissionCallback;

public class CameraActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private RelativeLayout controller;
    private XImageView done, shoot, clear;

    public static XCamera xCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        XtatutBar.transparentStatutBar(this);
        XtatutBar.transparentNagivationBar(this);

        surfaceView = findViewById(R.id.surfaceView);
        controller = findViewById(R.id.controller);
        done = findViewById(R.id.done);
        shoot = findViewById(R.id.shoot);
        clear = findViewById(R.id.clear);

        xCamera.surfaceView(surfaceView);

        shoot.setOnFgClickListener(new Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                xCamera.takePicture();
            }
        });
        done.setOnFgClickListener(new Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        clear.setOnFgClickListener(new Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopCamera();
                XApp.run(100, new Runnable() {
                    @Override
                    public void run() {
                        initCamera();
                    }
                });
            }
        });
    }

    private void initCamera() {
        controller.setVisibility(View.VISIBLE);

        xCamera.openCamera(CameraActivity.this);
    }

    private void stopCamera() {
        controller.setVisibility(View.GONE);

        xCamera.closeCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(XPermission.isAccessCamera(this))
            initCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCamera();
    }
}
