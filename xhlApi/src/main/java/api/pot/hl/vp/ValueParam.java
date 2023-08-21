package api.pot.hl.vp;

import java.util.ArrayList;
import java.util.List;

import api.pot.hl.R;

public class ValueParam {
    public int id;
    public boolean access = false;
    public boolean control = false;
    public boolean edit = true;
    public String label;
    public String value;

    public int subId;
    public boolean autoEdit = false;

    public int grade = 0;

    public VPType type = VPType.OTHERS;

    public int cols = 1;

    public int maxChoices = -1;


    public int icone = -1;
    public int iconeRight = R.drawable.ic_check_pink_down;
    public boolean usingLine = false;

    public boolean editionMode = true;

    public boolean inputCamera = true;
    public boolean inputGalery = true;

    private VPListener listener;

    public ValueParam icone(int icone) {
        this.icone = icone;
        return this;
    }

    public ValueParam usingLine(boolean usingLine) {
        this.usingLine = usingLine;
        return this;
    }

    public ValueParam iconeRight(int iconeRight) {
        this.iconeRight = iconeRight;
        return this;
    }

    public ValueParam inputs(boolean camera, boolean galery) {
        this.inputCamera = camera;
        this.inputGalery = galery;
        return this;
    }

    public ValueParam inputCamera(boolean inputCamera) {
        this.inputCamera = inputCamera;
        return this;
    }

    public ValueParam inputGalery(boolean inputGalery) {
        this.inputGalery = inputGalery;
        return this;
    }

    public ValueParam editionMode(boolean editionMode) {
        this.editionMode = editionMode;
        return this;
    }

    public ValueParam listener(VPListener vpListener) {
        this.listener = vpListener;
        return this;
    }

    public ValueParam type(VPType vpType){
        this.type = vpType;
        return this;
    }

    public ValueParam type(VPType vpType, int cols){
        this.cols = cols;
        this.type = vpType;
        return this;
    }

    public ValueParam type(VPType vpType, int cols, int maxChoices){
        this.maxChoices = maxChoices;
        this.cols = cols;
        this.type = vpType;
        return this;
    }

    public ValueParam(String label, String value, boolean... ace) {
        this(-1, label, value, ace);
    }

    public ValueParam(int id, String label, String value, boolean... ace) {
        this.id = id;
        this.label = label;
        this.value = value;
        if(ace!=null){
            if(ace.length>0) this.access = ace[0];
            if(ace.length>1) this.control = ace[1];
            if(ace.length>2) this.edit = ace[2];
        }
    }

    public ValueParam autoEdit(boolean autoEdit){
        this.autoEdit = autoEdit;
        return this;
    }

    public ValueParam subId(int subId) {
        this.subId = subId;
        return this;
    }

    public ValueParam grade(int grade) {
        this.grade = grade;
        return this;
    }

    public List<VPChoice> items = new ArrayList<>();
    public ValueParam datas(List<VPChoice> choices) {
        this.items = choices;
        return this;
    }
    public ValueParam datas(VPChoice... choices) {
        if(choices!=null&&choices.length>0){
            List<VPChoice> items = new ArrayList<>();
            for(VPChoice choice : choices)
                items.add(choice);
            datas(items);
        }
        return this;
    }

    public List<String> selections = new ArrayList<>();
    public ValueParam selections(List<String> keys) {
        this.selections = maxChoices>0&&keys.size()>maxChoices?keys.subList(0, maxChoices):keys;
        return this;
    }
    public ValueParam selections(String... keys) {
        if(keys!=null&&keys.length>0){
            List<String> items = new ArrayList<>();
            for(String key : keys)
                items.add(key);
            selections(items);
        }
        return this;
    }

    public boolean checked(VPChoice choice) {
        if(selections!=null&&selections.size()>0){
            for(String e : selections){
                if(e.equals(choice.key))
                    return true;
            }
        }
        return false;
    }

    public boolean onCheckedChanged(List<VPChoice> items, final int pos, boolean checked) {
        VPChoice item = items.get(pos);
        if(checked){
            return addSelection(item);
        }else {
            return removeSelection(item);
        }
    }

    private boolean removeSelection(VPChoice item) {
        for(int i=0;i<selections.size();i++){
            if(selections.get(i).equals(item.key)) {
                selections.remove(i);
                if(listener!=null) listener.onValueChange(items, selections);
                return true;
            }
        }
        return false;
    }

    private boolean addSelection(VPChoice item) {
        for(int i=0;i<selections.size();i++){
            if(selections.get(i).equals(item.key))
                return false;
        }
        selections.add(item.key);
        if(maxChoices>0){
            if(selections.size()>maxChoices)
                selections.remove(0);
        }
        if(listener!=null) listener.onValueChange(items, selections);
        return true;
    }

    public String description() {
        String ret = "";
        for(String e : selections){
            try {
                ret+=items.get(Integer.parseInt(e)).label+" | ";
            }catch (Exception ex){}
        }
        ret += "(Total = "+items.size()+")";
        return ret;
    }
}
