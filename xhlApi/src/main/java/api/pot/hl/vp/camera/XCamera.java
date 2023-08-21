package api.pot.hl.vp.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.FileOutputStream;
import java.util.List;

import api.pot.hl.R;
import api.pot.system.Log;
import api.pot.system.permissions.XPermission;
import api.pot.system.permissions.XPermissionCallback;

public class XCamera {
    private static XCamera xCamera;

    Camera camera;
    CamHolder camHolder;
    SurfaceView surfaceView;
    boolean camOpen = false;
    private Context context;

    private String path;
    private CameraListener listener;

    public XCamera(Context context) {
        this.context = context;
    }

    public static XCamera with(final Context context){
        xCamera = new XCamera(context);
        CameraActivity.xCamera = xCamera;
        return xCamera;
    }

    public void load() {
        context.startActivity(new Intent(context, CameraActivity.class));
    }

    public XCamera path(String path) {
        this.path = path;
        return this;
    }

    public XCamera listener(CameraListener cameraListener) {
        this.listener = cameraListener;
        return this;
    }

    public XCamera surfaceView(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        return this;
    }

    public void closeCamera(){
        if (camera != null) {
            surfaceView.getHolder().removeCallback(camHolder);
            camera.stopPreview();
            camera.release();
            camera = null;
            surfaceView.setVisibility(View.INVISIBLE);
        }
    }

    public void openCamera(Activity activity){
        camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        //size
        params.setPreviewSize(surfaceView.getWidth(), surfaceView.getHeight());
        // Pour connaître les modes de flash supportés
        List<String> flashs = params.getSupportedFlashModes();
        // Pour connaître les tailles d'image supportées
        //List<Camera.Size> tailles = getSupportedPictureSizes();
        //set param
        //camera.setParameters(params);

        setCameraDisplayOrientation(activity, 0, camera);

        //takePicture(camera);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        camHolder = new CamHolder(camera, surfaceView);
        holder.addCallback(camHolder);

        surfaceView.setVisibility(View.VISIBLE);

        /**
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                takePicture(camera);
                return false;
            }
        });*/
    }

    public void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public void takePicture() {
        takePicture(camera);
    }

    private void takePicture(Camera camera) {
        Camera.ShutterCallback shutterCallback = new
                Camera.ShutterCallback() {
                    public void onShutter() {
                        MediaPlayer media = MediaPlayer.create(context.getApplicationContext(),
                                R.raw.cam);
                        media.start();
                        // Une fois la lecture terminée
                        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                // On libère le lecteur multimédia
                                mp.release();
                            }
                        });
                    }
                };
        // Sera lancée une fois l'image traitée, on enregistre l'image sur le support externe
        Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream stream = null;
                try {
                    String p = Environment.getExternalStorageDirectory() +
                            "/photo"+System.currentTimeMillis()+".jpg";
                    stream = new FileOutputStream(path!=null?path:p);
                    stream.write(data);
                    if(listener!=null) listener.onShoot(path!=null?path:p);
                } catch (Exception e) {
                } finally {
                    try { stream.close();} catch (Exception e) {}
                }
            }
        };
        camera.takePicture(shutterCallback, null, jpegCallback);
    }
}
