package api.pot.hl.vp;

import api.pot.hl.R;

public class ValueParam {
    public int id;
    public boolean access = false;
    public boolean control = false;
    public boolean edit = true;
    public String label;
    public String value;

    public boolean autoEdit = false;

    public int icone = -1;
    public int iconeRight = R.drawable.main_edit;
    public boolean usingLine = false;

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

    public ValueParam(int id, String label, String value, boolean usingLine, int icone, boolean... ace) {
        this.id = id;
        this.label = label;
        this.value = value;
        ///
        this.usingLine = usingLine;
        this.icone = icone;
        ///
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

    public ValueParam iconeRight(int iconeRight) {
        this.iconeRight = iconeRight;
        return this;
    }
}
