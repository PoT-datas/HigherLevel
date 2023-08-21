package api.pot.hl.vp;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import api.pot.gl.xiv.XImageView;
import api.pot.hl.R;
import api.pot.system.Log;
import api.pot.system.XApp;
import api.pot.text.xtv.XTextView;

public class VPChoiceAdapter extends RecyclerView.Adapter<VPChoiceAdapter.ViewHolder>{
    private ValueParamListener valueParamListener;

    Context mContext;
    List<VPChoice> items;
    private VPChoice item;

    public void setListener(ValueParamListener valueParamListener) {
        this.valueParamListener = valueParamListener;
    }

    public VPChoiceAdapter(Context mContext, List<VPChoice> mData) {
        this.mContext = mContext;
        this.items = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_choice_vp,viewGroup,false);
        return new ViewHolder(layout);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        item = items.get(position);

        final int pos = position;

        holder.label.setText(item.label);
        holder.checker.setChecked(valueParam!=null&&valueParam.checked(item));

        holder.checker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {///-------------------------
                if(valueParam.onCheckedChanged(items, pos, b)) {
                    if(valueParamListener!=null) {
                        ValueParamAdapter adapter = valueParamListener.onParamChange(valueParam);
                        try {
                            adapter.notifyDataSetChanged();
                        }catch (Exception e){
                            try {
                                if(adapter!=null) adapter.notifyDataSetChanged();
                            }catch (Exception e2){
                                XApp.run(30, new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    ValueParam valueParam;
    public VPChoiceAdapter with(ValueParam valueParam) {
        this.valueParam = valueParam;
        return this;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        XTextView label;
        CheckBox checker;

        public ViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            checker = itemView.findViewById(R.id.checker);
        }
    }
}
