package api.pot.hl.vp.camera;

import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CamHolder implements SurfaceHolder.Callback {
    Camera camera;
    SurfaceView surface_view;
    boolean mPreviewRunning = false;

    public CamHolder(Camera camera, SurfaceView surface_view_cam) {
        this.camera = camera;
        this.surface_view = surface_view_cam;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            //camera=Camera.open();
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        mPreviewRunning = false;
        camera.release();
    }

    public void surfaceChanged(final SurfaceHolder holder, int format, final int w, final int h) {
        if (mPreviewRunning) {
            camera.stopPreview();
        }
                /*Camera.Parameters p = camera.getParameters();
                p.setPreviewSize(w, h);
                camera.setParameters(p);*/
        ///
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

        // You need to choose the most appropriate previewSize for your app
        /*Camera.Size previewSize = previewSizes.get(0);// .... select one of previewSizes here

        parameters.setPreviewSize(previewSize.width, previewSize.height);
        camera.setParameters(parameters);*/
        //
        for(Camera.Size previewSize : previewSizes){
            try {
                parameters.setPreviewSize(previewSize.width, previewSize.height);
                camera.setParameters(parameters);
                break;
            }catch (Exception e){}
        }
        ///
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        mPreviewRunning = true;
    }

    protected void onResume() {
        camera = Camera.open();
    }

    protected void onPause() {
        camera.release();
    }

}
