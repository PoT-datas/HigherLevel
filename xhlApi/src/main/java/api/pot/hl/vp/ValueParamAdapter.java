package api.pot.hl.vp;

import android.annotation.SuppressLint;
import android.content.Context;
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
import api.pot.text.xtv.XTextView;

@SuppressLint("NewApi")
public class ValueParamAdapter extends RecyclerView.Adapter<ValueParamAdapter.ViewHolder> {
    private ValueParamListener valueParamListener;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_value_param,viewGroup,false);
        return new ViewHolder(layout);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        item = items.get(position);

        if(vpContext==VPContext.MO){
            holder.access.setVisibility(item.control?View.VISIBLE:View.GONE);
            holder.edit.setVisibility(item.access&&!item.autoEdit?View.VISIBLE:View.GONE);
        }else if(vpContext==VPContext.MODEL){
            holder.edit.setEnabled(true);
            holder.access.setEnabled(item.control);
            if(!item.control) holder.access.setAlpha(0.5f);
            holder.access.setChecked(item.access);
            holder.edit.setVisibility(item.edit?View.VISIBLE:View.GONE);
        }else if(vpContext==VPContext.USER){
            holder.access.setChecked(item.access);
            holder.access.setVisibility(item.control?View.VISIBLE:View.GONE);
            holder.edit.setVisibility(item.edit?View.VISIBLE:View.GONE);
        }

        holder.label.setText(item.label);
        holder.value.setText(item.value);

        if(item.icone!=-1) holder.icone.setImageResource(item.icone);
        else holder.icone.setVisibility(View.GONE);
        if(item.iconeRight!=-1) holder.edit.setImageResource(item.iconeRight);
        else holder.edit.setVisibility(View.GONE);
        if(item.usingLine) holder.line.setVisibility(View.VISIBLE);
        else holder.line.setVisibility(View.GONE);

        //listeners
        holder.edit.setOnFgClickListener(new Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valueParamListener!=null) valueParamListener.onParamEditClick(view);
            }
        });
        holder.access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                setRightSwitchColor(holder.access, isChecked);
                if(valueParamListener!=null) valueParamListener.onParamCheckedChanged(compoundButton, isChecked);
            }
        });
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valueParamListener!=null) valueParamListener.onParamClick(view);
            }
        });
        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(valueParamListener!=null) valueParamListener.onParamLongClick(view);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        Switch access;
        XTextView label;
        XTextView value;
        XImageView edit;
        XImageView icone;
        View line;

        public ViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            access = itemView.findViewById(R.id.access);
            label = itemView.findViewById(R.id.label);
            value = itemView.findViewById(R.id.value);
            edit = itemView.findViewById(R.id.edit);
            icone = itemView.findViewById(R.id.icone);
            line = itemView.findViewById(R.id.line);

            access.getTrackDrawable().setColorFilter(mContext.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.getThumbDrawable().setColorFilter(mContext.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    setRightSwitchColor(access, b);
                }
            });
        }
    }

    private void setRightSwitchColor(Switch access,boolean b){

        if(b) {
            access.getTrackDrawable().setColorFilter(mContext.getResources().getColor(R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN);
            access.getThumbDrawable().setColorFilter(mContext.getResources().getColor(R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN);
        } else {
            access.getTrackDrawable().setColorFilter(mContext.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.getThumbDrawable().setColorFilter(mContext.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
        }
    }
}

