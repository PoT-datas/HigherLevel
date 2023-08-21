package api.pot.hl.vp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.util.List;

import api.pot.gl.xiv.XImageView;
import api.pot.gl.xiv.tools.Forgrounder;
import api.pot.hl.R;
import api.pot.system.Log;
import api.pot.system.XCast;
import api.pot.system.XDisplay;
import api.pot.text.xtv.XTextView;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class ValueParamAdapter extends RecyclerView.Adapter<VPViewHolder> {
    public ValueParamListener valueParamListener;
    private VPContext vpContext=VPContext.MO;

    Context mContext;
    List<ValueParam> items;
    private ValueParam item;

    public void setVpContext(VPContext vpContext) {
        this.vpContext = vpContext;
    }

    public void setListener(ValueParamListener valueParamListener) {
        this.valueParamListener = valueParamListener;
    }

    public ValueParamAdapter(Context mContext, List<ValueParam> mData) {
        this.mContext = mContext;
        this.items = mData;
    }

    @NonNull
    @Override
    public VPViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_value_param,viewGroup,false);
        return new VPViewHolder(layout).adapter(this).listener(valueParamListener).vpContext(vpContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull final VPViewHolder holder, int position) {
        item = items.get(position);

        holder.onBindViewHolder(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

