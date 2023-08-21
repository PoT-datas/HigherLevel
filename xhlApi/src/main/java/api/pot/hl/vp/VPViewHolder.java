package api.pot.hl.vp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import api.pot.gl.xiv.XImageView;
import api.pot.gl.xiv.tools.Forgrounder;
import api.pot.gl.xiv.tools.StarSystemMng;
import api.pot.gl.xiv.tools.XivCallback;
import api.pot.gl.xiv.tools.stars.OnStarNomberChangeListener;
import api.pot.hl.R;
import api.pot.hl.fingerprint.callback.FailAuthCounterCallback;
import api.pot.hl.fingerprint.callback.FingerprintCallback;
import api.pot.hl.fingerprint.callback.FingerprintSecureCallback;
import api.pot.hl.fingerprint.utils.CipherHelper;
import api.pot.hl.fingerprint.utils.FingerprintToken;
import api.pot.hl.vp.activity.DateActivity;
import api.pot.hl.vp.activity.SignActivity;
import api.pot.hl.vp.camera.CameraActivity;
import api.pot.hl.vp.camera.CameraCallback;
import api.pot.hl.vp.camera.XCamera;
import api.pot.hl.xiv.GeoForm;
import api.pot.system.Log;
import api.pot.system.XCast;
import api.pot.system.permissions.XPermission;
import api.pot.system.permissions.XPermissionCallback;
import api.pot.text.xet.EditTextCallback;
import api.pot.text.xtv.XTextView;

import static api.pot.gl.xiv.tools.stars.StarSystem.STAR_SYSTEM_NORMAL;

@SuppressLint("NewApi")
public class VPViewHolder extends RecyclerView.ViewHolder {

    public Context context;
    public ValueParamAdapter valueParamAdapter;
    ///
    public HText hText;
    public HCheckbox hCheckbox;
    public HSign hSign;
    public HImage hImage;
    public HFingerPrint hFingerPrint;
    public HDate hDate;
    public HNotice hNotice;
    public HOthers hOthers;

    public VPViewHolder(View itemView) {
        super(itemView);
        
        context = itemView.getContext();

        hText = new HText(itemView);
        hDate = new HDate(itemView);
        hCheckbox = new HCheckbox(itemView);
        hSign = new HSign(itemView);
        hImage = new HImage(itemView);
        hFingerPrint = new HFingerPrint(itemView);
        hNotice = new HNotice(itemView);
        hOthers = new HOthers(itemView);
    }

    public void setRightSwitchColor(Switch access,boolean b){

        if(b) {
            access.getTrackDrawable().setColorFilter(context.getResources().getColor(R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN);
            access.getThumbDrawable().setColorFilter(context.getResources().getColor(R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN);
        } else {
            access.getTrackDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.getThumbDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
        }
    }

    public void onBindViewHolder(ValueParam item) {
        init();
        switch (item.type){
            case TEXT:
                hText.show();
                hText.onBindViewHolder(item);
                break;
            case DATE:
                hDate.show();
                hDate.onBindViewHolder(item);
                break;
            case CHECKBOX:
                hCheckbox.show();
                hCheckbox.onBindViewHolder(item);
                break;
            case SIGN:
                hSign.show();
                hSign.onBindViewHolder(item);
                break;
            case IMAGE:
                hImage.show();
                hImage.onBindViewHolder(item);
                break;
            case NOTICE:
                hNotice.show();
                hNotice.onBindViewHolder(item);
                break;
            case FINGERPRINT:
                hFingerPrint.show();
                hFingerPrint.onBindViewHolder(item);
                break;
            case OTHERS:
                hOthers.show();
                hOthers.onBindViewHolder(item);
                break;
        }
    }

    private void init() {
        hText.init();
        hDate.init();
        hCheckbox.init();
        hSign.init();
        hImage.init();
        hFingerPrint.init();
        hNotice.init();
        hOthers.init();
    }

    public ValueParamListener valueParamListener;
    public VPViewHolder listener(ValueParamListener valueParamListener) {
        this.valueParamListener = valueParamListener;
        return this;
    }

    public VPViewHolder adapter(ValueParamAdapter valueParamAdapter) {
        this.valueParamAdapter = valueParamAdapter;
        return this;
    }

    public VPContext vpContext;
    public VPViewHolder vpContext(VPContext vpContext) {
        this.vpContext = vpContext;
        return this;
    }

    class HDate{
        RelativeLayout container;
        Switch access;
        XTextView label;
        XTextView value;
        XImageView edit;
        EditText inputText;

        XImageView sign1;
        XImageView sign2;
        XImageView sign3;

        XImageView icone;
        View line;

        private boolean editMode = false;

        View layout;
        public void init() {
            layout.setVisibility(View.GONE);
        }

        public void show() {
            layout.setVisibility(View.VISIBLE);
        }

        public HDate(View itemView) {
            layout = itemView.findViewById(R.id.item_date_value_param);

            container = layout.findViewById(R.id.container);
            access = layout.findViewById(R.id.access);
            label = layout.findViewById(R.id.label);
            value = layout.findViewById(R.id.value);
            edit = layout.findViewById(R.id.edit);
            inputText = layout.findViewById(R.id.inputText);

            icone = layout.findViewById(R.id.icone);
            line = layout.findViewById(R.id.line);

            sign1 = layout.findViewById(R.id.sign1);
            sign2 = layout.findViewById(R.id.sign2);
            sign3 = layout.findViewById(R.id.sign3);

            access.getTrackDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.getThumbDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    setRightSwitchColor(access, b);
                }
            });

            inputText.setVisibility(View.GONE);
            value.setVisibility(View.VISIBLE);
            edit.setImageResource(R.drawable.ic_check_pink_down);
        }

        public void sign(int grade) {
            sign1.setVisibility(View.GONE);
            sign2.setVisibility(View.GONE);
            sign3.setVisibility(View.GONE);
            if(grade>=3) sign3.setVisibility(View.VISIBLE);
            if(grade>=2) sign2.setVisibility(View.VISIBLE);
            if(grade>=1) sign1.setVisibility(View.VISIBLE);
        }

        public void onBindViewHolder(final ValueParam item) {
            if(vpContext==VPContext.MO){
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
                edit.setVisibility(item.access&&!item.autoEdit?View.VISIBLE:View.GONE);
            }else if(vpContext==VPContext.MODEL){
                edit.setEnabled(true);
                access.setEnabled(item.control);
                if(!item.control) access.setAlpha(0.5f);
                access.setChecked(item.access);
                edit.setVisibility(item.edit?View.VISIBLE:View.GONE);
            }else if(vpContext==VPContext.USER){
                access.setChecked(item.access);
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
                edit.setVisibility(item.edit?View.VISIBLE:View.GONE);
            }

            if(item.icone!=-1) icone.setImageResource(item.icone);
            else icone.setVisibility(View.GONE);
            if(item.iconeRight!=-1) edit.setImageResource(item.iconeRight);
            else edit.setVisibility(View.GONE);
            if(item.usingLine) line.setVisibility(View.VISIBLE);
            else line.setVisibility(View.GONE);

            label.setText(item.label);
            if(item.value!=null) {
                value.setText(item.value);
                inputText.setText(item.value);
                label.setTextColor(Color.parseColor("#202020"));
            }else {
                value.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                access.setVisibility(View.GONE);
                label.setTextColor(Color.parseColor("#000080"));
            }

            //edition mode
            if(item.editionMode){
                edit.setImageResource(R.drawable.ic_check_pink);
                inputText.setVisibility(View.VISIBLE);
                value.setVisibility(View.GONE);
            }

            //listeners
            inputText.addTextChangedListener(new EditTextCallback(){
                @Override
                public void afterTextChanged(Editable editable) {
                    item.value=inputText.getText().toString();
                    String s = inputText.getText().toString();
                    try {
                        if( (s.charAt(s.length()-1)=='\n') && valueParamListener!=null) {
                            ValueParamAdapter adapter = valueParamListener.onParamChange(item);
                            if(adapter!=null) adapter.notifyDataSetChanged();
                        }
                    }catch (Exception e){}
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, DateActivity.class));
                }
            });
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    setRightSwitchColor(access, isChecked);
                    if(valueParamListener!=null) valueParamListener.onParamCheckedChanged(compoundButton, isChecked);
                }
            });
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamClick(view);
                }
            });
            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamLongClick(view);
                    return true;
                }
            });

            sign(item.grade);
        }
    }

    class HNotice{
        RelativeLayout container;
        Switch access;
        XTextView label;
        XTextView value;
        XImageView edit;

        XImageView quote;

        XImageView sign1;
        XImageView sign2;
        XImageView sign3;

        XImageView icone;
        View line;

        private boolean editMode = false;

        View layout;
        public void init() {
            layout.setVisibility(View.GONE);
        }

        public void show() {
            layout.setVisibility(View.VISIBLE);
        }

        public HNotice(View itemView) {
            layout = itemView.findViewById(R.id.item_notice_value_param);

            container = layout.findViewById(R.id.container);
            access = layout.findViewById(R.id.access);
            label = layout.findViewById(R.id.label);
            value = layout.findViewById(R.id.value);
            edit = layout.findViewById(R.id.edit);
            quote = layout.findViewById(R.id.quote);

            icone = layout.findViewById(R.id.icone);
            line = layout.findViewById(R.id.line);

            sign1 = layout.findViewById(R.id.sign1);
            sign2 = layout.findViewById(R.id.sign2);
            sign3 = layout.findViewById(R.id.sign3);

            access.getTrackDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.getThumbDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    setRightSwitchColor(access, b);
                }
            });

            value.setVisibility(View.VISIBLE);
            edit.setImageResource(R.drawable.ic_check_pink_down);

            quote.setVisibility(View.GONE);
            quote.getStarSystem().setNbrStarSelected(2);
            quote.getStarSystem().setVisibility(STAR_SYSTEM_NORMAL);
            quote.getStarSystem().setEnabled(true);
            quote.getStarSystem().setOnStarNomberChangeListener(new OnStarNomberChangeListener() {
                @Override
                public void onStarNomberChange(int selectedStar) {
                    /**starSystemMng.addStarInfo(new StarSystemMng.StarInfo(quote.getStarSystem().getSelectedStar(), 1));
                    stat.getStarSystem().setStarInfos(starSystemMng);*/
                    //Toast.makeText(getContext(), "nbr selected: "+selectedStar, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void sign(int grade) {
            sign1.setVisibility(View.GONE);
            sign2.setVisibility(View.GONE);
            sign3.setVisibility(View.GONE);
            if(grade>=3) sign3.setVisibility(View.VISIBLE);
            if(grade>=2) sign2.setVisibility(View.VISIBLE);
            if(grade>=1) sign1.setVisibility(View.VISIBLE);
        }

        public void onBindViewHolder(final ValueParam item) {
            if(vpContext==VPContext.MO){
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
                edit.setVisibility(item.access&&!item.autoEdit?View.VISIBLE:View.GONE);
            }else if(vpContext==VPContext.MODEL){
                edit.setEnabled(true);
                access.setEnabled(item.control);
                if(!item.control) access.setAlpha(0.5f);
                access.setChecked(item.access);
                edit.setVisibility(item.edit?View.VISIBLE:View.GONE);
            }else if(vpContext==VPContext.USER){
                access.setChecked(item.access);
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
                edit.setVisibility(item.edit?View.VISIBLE:View.GONE);
            }

            if(item.icone!=-1) icone.setImageResource(item.icone);
            else icone.setVisibility(View.GONE);
            if(item.iconeRight!=-1) edit.setImageResource(item.iconeRight);
            else edit.setVisibility(View.GONE);
            if(item.usingLine) line.setVisibility(View.VISIBLE);
            else line.setVisibility(View.GONE);

            label.setText(item.label);
            if(item.value!=null) {
                value.setText(item.value);
                label.setTextColor(Color.parseColor("#202020"));
            }else {
                value.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                access.setVisibility(View.GONE);
                label.setTextColor(Color.parseColor("#000080"));
            }

            //edition mode
            if(item.editionMode){
                edit.setImageResource(R.drawable.ic_check_pink);
                quote.setVisibility(View.VISIBLE);
                value.setVisibility(View.GONE);
            }

            //listeners
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(editMode){
                        edit.setImageResource(R.drawable.ic_check_pink);
                        quote.setVisibility(View.VISIBLE);
                        value.setVisibility(View.GONE);
                    }else {
                        edit.setImageResource(R.drawable.ic_check_pink_down);
                        quote.setVisibility(View.GONE);
                        value.setVisibility(View.VISIBLE);
                    }
                    editMode=!editMode;
                    if(valueParamListener!=null) valueParamListener.onParamEditClick(view);
                }
            });
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    setRightSwitchColor(access, isChecked);
                    if(valueParamListener!=null) valueParamListener.onParamCheckedChanged(compoundButton, isChecked);
                }
            });
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamClick(view);
                }
            });
            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamLongClick(view);
                    return true;
                }
            });

            sign(item.grade);
        }
    }

    class HText{
        RelativeLayout container;
        Switch access;
        XTextView label;
        XTextView value;
        XImageView edit;
        EditText inputText;

        XImageView sign1;
        XImageView sign2;
        XImageView sign3;

        XImageView icone;
        View line;

        private boolean editMode = false;

        View layout;
        public void init() {
            layout.setVisibility(View.GONE);
        }

        public void show() {
            layout.setVisibility(View.VISIBLE);
        }

        public HText(View itemView) {
            layout = itemView.findViewById(R.id.item_text_value_param);

            container = layout.findViewById(R.id.container);
            access = layout.findViewById(R.id.access);
            label = layout.findViewById(R.id.label);
            value = layout.findViewById(R.id.value);
            edit = layout.findViewById(R.id.edit);
            inputText = layout.findViewById(R.id.inputText);


            sign1 = layout.findViewById(R.id.sign1);
            sign2 = layout.findViewById(R.id.sign2);
            sign3 = layout.findViewById(R.id.sign3);

            icone = layout.findViewById(R.id.icone);
            line = layout.findViewById(R.id.line);

            access.getTrackDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.getThumbDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    setRightSwitchColor(access, b);
                }
            });

            inputText.setVisibility(View.GONE);
            value.setVisibility(View.VISIBLE);
            edit.setImageResource(R.drawable.ic_check_pink_down);
        }

        public void sign(int grade) {
            sign1.setVisibility(View.GONE);
            sign2.setVisibility(View.GONE);
            sign3.setVisibility(View.GONE);
            if(grade>=3) sign3.setVisibility(View.VISIBLE);
            if(grade>=2) sign2.setVisibility(View.VISIBLE);
            if(grade>=1) sign1.setVisibility(View.VISIBLE);
        }

        public void onBindViewHolder(final ValueParam item) {
            if(vpContext==VPContext.MO){
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
                edit.setVisibility(item.access&&!item.autoEdit?View.VISIBLE:View.GONE);
            }else if(vpContext==VPContext.MODEL){
                edit.setEnabled(true);
                access.setEnabled(item.control);
                if(!item.control) access.setAlpha(0.5f);
                access.setChecked(item.access);
                edit.setVisibility(item.edit?View.VISIBLE:View.GONE);
            }else if(vpContext==VPContext.USER){
                access.setChecked(item.access);
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
                edit.setVisibility(item.edit?View.VISIBLE:View.GONE);
            }

            if(item.icone!=-1) icone.setImageResource(item.icone);
            else icone.setVisibility(View.GONE);
            if(item.iconeRight!=-1) edit.setImageResource(item.iconeRight);
            else edit.setVisibility(View.GONE);
            if(item.usingLine) line.setVisibility(View.VISIBLE);
            else line.setVisibility(View.GONE);

            label.setText(item.label);
            if(item.value!=null) {
                value.setText(item.value);
                inputText.setText(item.value);
                label.setTextColor(Color.parseColor("#202020"));
            }else {
                value.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                access.setVisibility(View.GONE);
                label.setTextColor(Color.parseColor("#000080"));
            }

            //edition mode
            if(item.editionMode){
                edit.setImageResource(R.drawable.ic_check_pink);
                inputText.setVisibility(View.VISIBLE);
                value.setVisibility(View.GONE);
            }

            //listeners
            inputText.addTextChangedListener(new EditTextCallback(){
                @Override
                public void afterTextChanged(Editable editable) {
                    item.value=inputText.getText().toString();
                    String s = inputText.getText().toString();
                    try {
                        if( (s.charAt(s.length()-1)=='\n') && valueParamListener!=null) {
                            ValueParamAdapter adapter = valueParamListener.onParamChange(item);
                            if(adapter!=null) adapter.notifyDataSetChanged();
                        }
                    }catch (Exception e){}
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(editMode){
                        edit.setImageResource(R.drawable.ic_check_pink);
                        inputText.setVisibility(View.VISIBLE);
                        value.setVisibility(View.GONE);
                    }else {
                        edit.setImageResource(R.drawable.ic_check_pink_down);
                        inputText.setVisibility(View.GONE);
                        value.setVisibility(View.VISIBLE);
                    }
                    editMode=!editMode;
                    if(valueParamListener!=null) valueParamListener.onParamEditClick(view);
                }
            });
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    setRightSwitchColor(access, isChecked);
                    if(valueParamListener!=null) valueParamListener.onParamCheckedChanged(compoundButton, isChecked);
                }
            });
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamClick(view);
                }
            });
            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamLongClick(view);
                    return true;
                }
            });

            sign(item.grade);
        }
    }

    class HCheckbox{
        RelativeLayout container;
        Switch access;
        XTextView label;
        XTextView value;
        XImageView edit;

        RecyclerView recyclerView;
        VPChoiceAdapter adapter;
        GridLayoutManager layoutManager;
        List<VPChoice> items = new ArrayList<>();


        XImageView sign1;
        XImageView sign2;
        XImageView sign3;

        XImageView icone;
        View line;

        private boolean editMode = false;

        View layout;
        public void init() {
            layout.setVisibility(View.GONE);
        }

        public void show() {
            layout.setVisibility(View.VISIBLE);
        }

        public HCheckbox(View itemView) {
            layout = itemView.findViewById(R.id.item_checkbox_value_param);

            container = layout.findViewById(R.id.container);
            access = layout.findViewById(R.id.access);
            label = layout.findViewById(R.id.label);
            value = layout.findViewById(R.id.value);
            edit = layout.findViewById(R.id.edit);
            recyclerView = layout.findViewById(R.id.recyclerView);

            icone = layout.findViewById(R.id.icone);
            line = layout.findViewById(R.id.line);

            sign1 = layout.findViewById(R.id.sign1);
            sign2 = layout.findViewById(R.id.sign2);
            sign3 = layout.findViewById(R.id.sign3);

            access.getTrackDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.getThumbDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    setRightSwitchColor(access, b);
                }
            });

            recyclerView.setVisibility(View.GONE);
            value.setVisibility(View.VISIBLE);
            edit.setImageResource(R.drawable.ic_check_pink_down);

            initChoices();
        }

        public void sign(int grade) {
            sign1.setVisibility(View.GONE);
            sign2.setVisibility(View.GONE);
            sign3.setVisibility(View.GONE);
            if(grade>=3) sign3.setVisibility(View.VISIBLE);
            if(grade>=2) sign2.setVisibility(View.VISIBLE);
            if(grade>=1) sign1.setVisibility(View.VISIBLE);
        }

        public void onBindViewHolder(final ValueParam item) {
            if(vpContext==VPContext.MO){
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
                edit.setVisibility(item.access&&!item.autoEdit?View.VISIBLE:View.GONE);
            }else if(vpContext==VPContext.MODEL){
                edit.setEnabled(true);
                access.setEnabled(item.control);
                if(!item.control) access.setAlpha(0.5f);
                access.setChecked(item.access);
                edit.setVisibility(item.edit?View.VISIBLE:View.GONE);
            }else if(vpContext==VPContext.USER){
                access.setChecked(item.access);
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
                edit.setVisibility(item.edit?View.VISIBLE:View.GONE);
            }

            if(item.icone!=-1) icone.setImageResource(item.icone);
            else icone.setVisibility(View.GONE);
            if(item.iconeRight!=-1) edit.setImageResource(item.iconeRight);
            else edit.setVisibility(View.GONE);
            if(item.usingLine) line.setVisibility(View.VISIBLE);
            else line.setVisibility(View.GONE);

            label.setText(item.label);
            if(item.items!=null) {
                value.setText(item.description());
                label.setTextColor(Color.parseColor("#202020"));
            }else {
                value.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                access.setVisibility(View.GONE);
                label.setTextColor(Color.parseColor("#000080"));
            }

            //edition mode
            if(item.editionMode){
                edit.setImageResource(R.drawable.ic_check_pink);
                recyclerView.setVisibility(View.VISIBLE);
                value.setVisibility(View.GONE);
            }

            //listeners
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(editMode){
                        edit.setImageResource(R.drawable.ic_check_pink);
                        recyclerView.setVisibility(View.VISIBLE);
                        value.setVisibility(View.GONE);
                    }else {
                        edit.setImageResource(R.drawable.ic_check_pink_down);
                        recyclerView.setVisibility(View.GONE);
                        value.setVisibility(View.VISIBLE);
                    }
                    editMode=!editMode;
                    if(valueParamListener!=null) valueParamListener.onParamEditClick(view);
                }
            });
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    setRightSwitchColor(access, isChecked);
                    if(valueParamListener!=null) valueParamListener.onParamCheckedChanged(compoundButton, isChecked);
                }
            });
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamClick(view);
                }
            });
            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamLongClick(view);
                    return true;
                }
            });

            sign(item.grade);
            
            buildChoices(item);
        }

        private void buildChoices(ValueParam item) {
            items.clear();
            items.addAll(item.items);
            layoutManager.setSpanCount(item.cols);
            ///
            adapter.setListener(valueParamListener);
            adapter.with(item).notifyDataSetChanged();
        }

        private void initChoices() {
            adapter = new VPChoiceAdapter(context, items);
            adapter.setListener(valueParamListener);
            layoutManager = new GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

    class HSign{
        RelativeLayout container;
        Switch access;
        XTextView label;

        XImageView print;
        XImageView printer;
        ImageView printerBg;
        XImageView more;

        XImageView sign1;
        XImageView sign2;
        XImageView sign3;

        XImageView icone;
        View line;

        private boolean editMode = false;

        View layout;
        public void init() {
            layout.setVisibility(View.GONE);
        }

        public void show() {
            layout.setVisibility(View.VISIBLE);
        }

        public HSign(View itemView) {
            layout = itemView.findViewById(R.id.item_sign_value_param);

            container = layout.findViewById(R.id.container);
            access = layout.findViewById(R.id.access);
            label = layout.findViewById(R.id.label);

            print = layout.findViewById(R.id.print);

            icone = layout.findViewById(R.id.icone);
            line = layout.findViewById(R.id.line);

            printer = layout.findViewById(R.id.printer);
            printerBg = layout.findViewById(R.id.printerBg);
            more = layout.findViewById(R.id.more);

            sign1 = layout.findViewById(R.id.sign1);
            sign2 = layout.findViewById(R.id.sign2);
            sign3 = layout.findViewById(R.id.sign3);

            initPrinter();

            access.getTrackDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.getThumbDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    setRightSwitchColor(access, b);
                }
            });
        }

        GeoForm geoForm;
        private void initPrinter() {
            geoForm = new GeoForm();
            geoForm.setStyle(GeoForm.Style.PAINT);
            ///----
            geoForm.setStrockStyle(Paint.Style.STROKE);
            geoForm.setBorderColor(Color.WHITE);
            geoForm.setBorderWidth(XCast.dt2px(context, 5));
            geoForm.setSoftEdge(0);
            ///----
            printer.setListener(new XivCallback(){
                @Override
                public void onDrawContent(Canvas canvas) {
                    super.onDrawContent(canvas);
                    ///
                    if(geoForm.bound==null)
                        geoForm.setBound(new RectF(0,0,canvas.getWidth(), canvas.getHeight()));
                    ///
                    geoForm.onDraw(canvas);
                    ///---canvas.drawColor(Color.RED);
                }
            });
            ///----
            printer.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return super.onSingleTapUp(e);
                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        double h = Math.sqrt(Math.pow(Math.abs(distanceX), 2) + Math.pow(Math.abs(distanceY), 2));
                        if(distanceX<0 && distanceY>0) h=-h;

                        if(e2.getPointerCount()==2){
                            ///
                        }else {
                            if(geoForm!=null){
                                geoForm.onScroll(e2);
                                printer.invalidater();
                            }
                        }
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }

                    @Override
                    public boolean onDown(MotionEvent e) {
                        if(geoForm!=null){
                            geoForm.onDown(e);
                            printer.invalidater();
                            ////Log.i(context, geoForm.paths.size()+"|");
                            valueParamAdapter.valueParamListener.onPrinterDown(null);
                        }
                        //
                        return super.onDown(e);
                    }
                });

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gestureDetector.onTouchEvent(motionEvent);
                    //mDetector.onTouchEvent(motionEvent);
                    if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                        valueParamAdapter.valueParamListener.onPrinterUp(null);
                    return true;
                }
            });
        }

        public void onBindViewHolder(final ValueParam item) {
            if(vpContext==VPContext.MO){
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
            }else if(vpContext==VPContext.MODEL){
                access.setEnabled(item.control);
                if(!item.control) access.setAlpha(0.5f);
                access.setChecked(item.access);
            }else if(vpContext==VPContext.USER){
                access.setChecked(item.access);
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
            }

            if(item.icone!=-1) icone.setImageResource(item.icone);
            else icone.setVisibility(View.GONE);/**
            if(item.iconeRight!=-1) edit.setImageResource(item.iconeRight);
            else edit.setVisibility(View.GONE);*/
            if(item.usingLine) line.setVisibility(View.VISIBLE);
            else line.setVisibility(View.GONE);

            label.setText(item.label);
            if(item.value!=null) {
                label.setTextColor(Color.parseColor("#202020"));
            }else {
                access.setVisibility(View.GONE);
                label.setTextColor(Color.parseColor("#000080"));
            }

            print.setOnFgClickListener(new Forgrounder.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, SignActivity.class));
                }
            });

            /***if(item.inputCamera)
                camera.setVisibility(View.VISIBLE);
            else
                camera.setVisibility(View.GONE);
            if(item.inputGalery)
                galery.setVisibility(View.VISIBLE);
            else
                galery.setVisibility(View.GONE);

            camera.setOnFgClickListener(new Forgrounder.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.r(context, "Charger depuis la Camera");
                }
            });
            galery.setOnFgClickListener(new Forgrounder.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.r(context, "Charger depuis la Galerie");
                }
            });*/
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    setRightSwitchColor(access, isChecked);
                    if(valueParamListener!=null) valueParamListener.onParamCheckedChanged(compoundButton, isChecked);
                }
            });
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamClick(view);
                }
            });
            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamLongClick(view);
                    return true;
                }
            });

            sign(item.grade);
        }

        public void sign(int grade) {
            sign1.setVisibility(View.GONE);
            sign2.setVisibility(View.GONE);
            sign3.setVisibility(View.GONE);
            if(grade>=3) sign3.setVisibility(View.VISIBLE);
            if(grade>=2) sign2.setVisibility(View.VISIBLE);
            if(grade>=1) sign1.setVisibility(View.VISIBLE);
        }
    }

    class HFingerPrint{
        RelativeLayout container;
        Switch access;
        XTextView label;

        XImageView print, clear;
        ImageView image;

        XImageView sign1;
        XImageView sign2;
        XImageView sign3;

        XImageView icone;
        View line;

        private boolean editMode = false;

        View layout;
        public void init() {
            layout.setVisibility(View.GONE);
        }

        public void show() {
            layout.setVisibility(View.VISIBLE);
        }

        public HFingerPrint(View itemView) {
            layout = itemView.findViewById(R.id.item_fingerprint_value_param);

            container = layout.findViewById(R.id.container);
            access = layout.findViewById(R.id.access);
            label = layout.findViewById(R.id.label);

            icone = layout.findViewById(R.id.icone);
            line = layout.findViewById(R.id.line);

            image = layout.findViewById(R.id.image);
            clear = layout.findViewById(R.id.clear);
            print = layout.findViewById(R.id.print);

            sign1 = layout.findViewById(R.id.sign1);
            sign2 = layout.findViewById(R.id.sign2);
            sign3 = layout.findViewById(R.id.sign3);

            access.getTrackDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.getThumbDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    setRightSwitchColor(access, b);
                }
            });
        }

        public void sign(int grade) {
            sign1.setVisibility(View.GONE);
            sign2.setVisibility(View.GONE);
            sign3.setVisibility(View.GONE);
            if(grade>=3) sign3.setVisibility(View.VISIBLE);
            if(grade>=2) sign2.setVisibility(View.VISIBLE);
            if(grade>=1) sign1.setVisibility(View.VISIBLE);
        }

        public void onBindViewHolder(final ValueParam item) {
            if(vpContext==VPContext.MO){
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
            }else if(vpContext==VPContext.MODEL){
                access.setEnabled(item.control);
                if(!item.control) access.setAlpha(0.5f);
                access.setChecked(item.access);
            }else if(vpContext==VPContext.USER){
                access.setChecked(item.access);
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
            }

            if(item.icone!=-1) icone.setImageResource(item.icone);
            else icone.setVisibility(View.GONE);/**
            if(item.iconeRight!=-1) edit.setImageResource(item.iconeRight);
            else edit.setVisibility(View.GONE);*/
            if(item.usingLine) line.setVisibility(View.VISIBLE);
            else line.setVisibility(View.GONE);

            label.setText(item.label);
            if(item.value!=null) {
                label.setTextColor(Color.parseColor("#202020"));
            }else {
                access.setVisibility(View.GONE);
                label.setTextColor(Color.parseColor("#000080"));
            }

            print.setOnFgClickListener(new Forgrounder.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.r(context, "Scan Now");
                    fingerprintCallback = new FingerprintCallback() {
                        @Override
                        public void onAuthenticationSucceeded() {
                            Log.i(context, "onAuthenticationSucceeded");
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            Log.i(context, "onAuthenticationFailed");
                        }

                        @Override
                        public void onAuthenticationError(int errorCode, String error) {
                            Log.i(context, "onAuthenticationError");
                        }
                    };
                    ///authenticate();
                    //context is any Android.Content.Context instance, typically the Activity
                    /***FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.From(context);
                    fingerprintManager.Authenticate(FingerprintManager.CryptoObject crypto,
                    int flags,
                    CancellationSignal cancel,
                    FingerprintManagerCompat.AuthenticationCallback callback,
                    Handler handler
                               );*/
                }
            });
            clear.setOnFgClickListener(new Forgrounder.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.r(context, "Clear");
                }
            });
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    setRightSwitchColor(access, isChecked);
                    if(valueParamListener!=null) valueParamListener.onParamCheckedChanged(compoundButton, isChecked);
                }
            });
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamClick(view);
                }
            });
            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamLongClick(view);
                    return true;
                }
            });

            sign(item.grade);
        }

        public void authenticate(){
            if(fingerprintSecureCallback!=null){
                if(cryptoObject!=null){
                    throw new RuntimeException("If you specify a CryptoObject you have to use FingerprintCallback");
                }
                cryptoObject = cipherHelper.getEncryptionCryptoObject();
                if(cryptoObject==null) {
                    fingerprintSecureCallback.onNewFingerprintEnrolled(new FingerprintToken(cipherHelper));
                }
            }
            else if(fingerprintCallback==null){
                throw new RuntimeException("You must specify a callback.");
            }

            cancellationSignal = new CancellationSignal();
            if(fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
                fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        /**setStatus(R.drawable.fingerprint_error, fingerprintError, circleError);
                        handler.postDelayed(returnToScanning, delayAfterError);*/
                        if(fingerprintSecureCallback!=null){
                            fingerprintSecureCallback.onAuthenticationError(errorCode, errString.toString());
                        } else{
                            fingerprintCallback.onAuthenticationError(errorCode, errString.toString());
                        }
                    }

                    @Override
                    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                        super.onAuthenticationHelp(helpCode, helpString);
                        /**setStatus(R.drawable.fingerprint_error, fingerprintError, circleError);
                        handler.postDelayed(returnToScanning, delayAfterError);*/
                        if(fingerprintSecureCallback!=null){
                            fingerprintSecureCallback.onAuthenticationError(helpCode, helpString.toString());
                        } else{
                            fingerprintCallback.onAuthenticationError(helpCode, helpString.toString());
                        }
                    }

                    @Override
                    public void onAuthenticationSucceeded(final FingerprintManager.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        /**handler.removeCallbacks(returnToScanning);
                        setStatus(R.drawable.fingerprint_success, fingerprintSuccess, circleSuccess);*/
                        if(fingerprintSecureCallback!=null){
                            fingerprintSecureCallback.onAuthenticationSucceeded();
                        }
                        else{
                            fingerprintCallback.onAuthenticationSucceeded();
                        }
                        tryCounter = 0;
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        /**setStatus(R.drawable.fingerprint_error, fingerprintError, circleError);
                        handler.postDelayed(returnToScanning, delayAfterError);
                        handler.postDelayed(checkForLimit, delayAfterError);*/
                        if(fingerprintSecureCallback!=null){
                            fingerprintSecureCallback.onAuthenticationFailed();
                        }
                        else{
                            fingerprintCallback.onAuthenticationFailed();
                        }
                    }
                }, null);
            }
            else{
                Log.i(context, "Fingerprint scanner not detected or no fingerprint enrolled. Use FingerprintView#isAvailable(Context) before.");
            }
        }

        private void initView(Context context){
            this.fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            this.cipherHelper = null;
            this.handler = new Handler();
            this.fingerprintCallback = null;
            this.fingerprintSecureCallback = null;
            this.counterCallback = null;
            this.cryptoObject = null;
            this.tryCounter = 0;
            this.delayAfterError = DEFAULT_DELAY_AFTER_ERROR;

            int fingerprintSize = (int) (size*SCALE);
            int circleSize = size;/**

            fingerprintImageView = new AppCompatImageView(context);
            fingerprintImageView.setLayoutParams(new RelativeLayout.LayoutParams(fingerprintSize, fingerprintSize));
            fingerprintImageView.setBackgroundResource(R.drawable.fingerprint);
            ((RelativeLayout.LayoutParams)fingerprintImageView.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

            circleView = new View(context);
            circleView.setLayoutParams(new RelativeLayout.LayoutParams(circleSize, circleSize));
            circleView.setBackgroundResource(R.drawable.circle);
            ((RelativeLayout.LayoutParams)circleView.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);*/
        }

        private final static String TAG = "FingerprintView";

        private View fingerprintImageView;
        private View circleView;

        private FingerprintManager fingerprintManager;
        private CancellationSignal cancellationSignal;
        private FingerprintCallback fingerprintCallback;
        private FingerprintSecureCallback fingerprintSecureCallback;
        private FailAuthCounterCallback counterCallback;
        private FingerprintManager.CryptoObject cryptoObject;
        private CipherHelper cipherHelper;
        private Handler handler;

        private int fingerprintScanning, fingerprintSuccess, fingerprintError;
        private int circleScanning, circleSuccess, circleError;

        private int limit, tryCounter;
        private int delayAfterError;
        private int size;

        public final static int DEFAULT_DELAY_AFTER_ERROR = 1200;
        public final static int DEFAULT_CIRCLE_SIZE = 50;
        public final static int DEFAULT_FINGERPRINT_SIZE = 30;
        public final static float SCALE = (float) DEFAULT_FINGERPRINT_SIZE/DEFAULT_CIRCLE_SIZE;
    }

    class HImage{
        RelativeLayout container;
        Switch access;
        XTextView label;

        XImageView camera, galery;
        XImageView image;

        XImageView sign1;
        XImageView sign2;
        XImageView sign3;

        XImageView icone;
        View line;

        private boolean editMode = false;

        View layout;
        public void init() {
            layout.setVisibility(View.GONE);
        }

        public void show() {
            layout.setVisibility(View.VISIBLE);
        }

        public HImage(View itemView) {
            layout = itemView.findViewById(R.id.item_image_value_param);

            container = layout.findViewById(R.id.container);
            access = layout.findViewById(R.id.access);
            label = layout.findViewById(R.id.label);

            icone = layout.findViewById(R.id.icone);
            line = layout.findViewById(R.id.line);

            image = layout.findViewById(R.id.image);
            camera = layout.findViewById(R.id.camera);
            galery = layout.findViewById(R.id.galery);

            sign1 = layout.findViewById(R.id.sign1);
            sign2 = layout.findViewById(R.id.sign2);
            sign3 = layout.findViewById(R.id.sign3);

            access.getTrackDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.getThumbDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    setRightSwitchColor(access, b);
                }
            });
        }

        public void sign(int grade) {
            sign1.setVisibility(View.GONE);
            sign2.setVisibility(View.GONE);
            sign3.setVisibility(View.GONE);
            if(grade>=3) sign3.setVisibility(View.VISIBLE);
            if(grade>=2) sign2.setVisibility(View.VISIBLE);
            if(grade>=1) sign1.setVisibility(View.VISIBLE);
        }

        public void onBindViewHolder(final ValueParam item) {
            if(vpContext==VPContext.MO){
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
            }else if(vpContext==VPContext.MODEL){
                access.setEnabled(item.control);
                if(!item.control) access.setAlpha(0.5f);
                access.setChecked(item.access);
            }else if(vpContext==VPContext.USER){
                access.setChecked(item.access);
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
            }

            if(item.icone!=-1) icone.setImageResource(item.icone);
            else icone.setVisibility(View.GONE);/**
            if(item.iconeRight!=-1) edit.setImageResource(item.iconeRight);
            else edit.setVisibility(View.GONE);*/
            if(item.usingLine) line.setVisibility(View.VISIBLE);
            else line.setVisibility(View.GONE);

            label.setText(item.label);
            if(item.value!=null) {
                label.setTextColor(Color.parseColor("#202020"));
            }else {
                access.setVisibility(View.GONE);
                label.setTextColor(Color.parseColor("#000080"));
            }

            if(item.inputCamera)
                camera.setVisibility(View.VISIBLE);
            else
                camera.setVisibility(View.GONE);
            if(item.inputGalery)
                galery.setVisibility(View.VISIBLE);
            else
                galery.setVisibility(View.GONE);

            camera.setOnFgClickListener(new Forgrounder.OnClickListener() {
                @Override
                public void onClick(View view) {
                    XPermission.isAccessCamera(context, new XPermissionCallback(){
                        @Override
                        public void onPermissionGranted() {
                            Log.r(context, "Charger depuis la Camera");
                            XCamera.with(context)
                                    .path(Environment.getExternalStorageDirectory() + "/tgps/p"+System.currentTimeMillis()+".jpg")
                                    .listener(new CameraCallback(){
                                        @Override
                                        public void onShoot(String path) {
                                            Log.i(context, "saved at :\n"+path);
                                            image.setImagePath(path);
                                        }
                                    })
                                    .load();
                        }

                        @Override
                        public void onPermissionDenied() {
                            Log.i(context, "Need permission.!!!");
                        }
                    });
                }
            });
            galery.setOnFgClickListener(new Forgrounder.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.r(context, "Charger depuis la Galerie");
                }
            });
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    setRightSwitchColor(access, isChecked);
                    if(valueParamListener!=null) valueParamListener.onParamCheckedChanged(compoundButton, isChecked);
                }
            });
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamClick(view);
                }
            });
            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamLongClick(view);
                    return true;
                }
            });

            sign(item.grade);
        }
    }

    class HOthers{
        RelativeLayout container;
        Switch access;
        XTextView label;
        XTextView value;
        XImageView edit;

        XImageView sign1;
        XImageView sign2;
        XImageView sign3;

        XImageView icone;
        View line;

        View layout;
        public void init() {
            layout.setVisibility(View.GONE);
        }

        public void show() {
            layout.setVisibility(View.VISIBLE);
        }

        public HOthers(View itemView) {
            layout = itemView.findViewById(R.id.item_others_value_param);

            container = layout.findViewById(R.id.container);
            access = layout.findViewById(R.id.access);
            label = layout.findViewById(R.id.label);
            value = layout.findViewById(R.id.value);
            edit = layout.findViewById(R.id.edit);

            icone = layout.findViewById(R.id.icone);
            line = layout.findViewById(R.id.line);

            sign1 = layout.findViewById(R.id.sign1);
            sign2 = layout.findViewById(R.id.sign2);
            sign3 = layout.findViewById(R.id.sign3);

            access.getTrackDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.getThumbDrawable().setColorFilter(context.getResources().getColor(R.color.colorGray),
                    PorterDuff.Mode.SRC_IN);
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    setRightSwitchColor(access, b);
                }
            });
        }

        public void sign(int grade) {
            sign1.setVisibility(View.GONE);
            sign2.setVisibility(View.GONE);
            sign3.setVisibility(View.GONE);
            if(grade>=3) sign3.setVisibility(View.VISIBLE);
            if(grade>=2) sign2.setVisibility(View.VISIBLE);
            if(grade>=1) sign1.setVisibility(View.VISIBLE);
        }

        public void onBindViewHolder(ValueParam item) {
            if(vpContext==VPContext.MO){
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
                edit.setVisibility(item.access&&!item.autoEdit?View.VISIBLE:View.GONE);
            }else if(vpContext==VPContext.MODEL){
                edit.setEnabled(true);
                access.setEnabled(item.control);
                if(!item.control) access.setAlpha(0.5f);
                access.setChecked(item.access);
                edit.setVisibility(item.edit?View.VISIBLE:View.GONE);
            }else if(vpContext==VPContext.USER){
                access.setChecked(item.access);
                access.setVisibility(item.control?View.VISIBLE:View.GONE);
                edit.setVisibility(item.edit?View.VISIBLE:View.GONE);
            }

            if(item.icone!=-1) icone.setImageResource(item.icone);
            else icone.setVisibility(View.GONE);
            if(item.iconeRight!=-1) edit.setImageResource(item.iconeRight);
            else edit.setVisibility(View.GONE);
            if(item.usingLine) line.setVisibility(View.VISIBLE);
            else line.setVisibility(View.GONE);

            label.setText(item.label);
            if(item.value!=null) {
                value.setText(item.value);
                label.setTextColor(Color.parseColor("#202020"));
            }else {
                value.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                access.setVisibility(View.GONE);
                label.setTextColor(Color.parseColor("#000080"));
            }

            //listeners
            edit.setOnFgClickListener(new Forgrounder.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamEditClick(view);
                }
            });
            access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    setRightSwitchColor(access, isChecked);
                    if(valueParamListener!=null) valueParamListener.onParamCheckedChanged(compoundButton, isChecked);
                }
            });
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamClick(view);
                }
            });
            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(valueParamListener!=null) valueParamListener.onParamLongClick(view);
                    return true;
                }
            });

            sign(item.grade);
        }
    }
}
