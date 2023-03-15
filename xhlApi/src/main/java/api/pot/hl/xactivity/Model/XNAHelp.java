package api.pot.hl.xactivity.Model;

import java.util.ArrayList;
import java.util.List;

public class XNAHelp {

    private XNAHelpItem helpItem;
    private List<XNAHelpItem> listHelp;

    public final int NO_ICON = 0;
    public final boolean IS_HIDE = true;
    public final boolean IS_CHECK = true;
    public final boolean IS_HEADER = true;
    public final String ONLY_SEPARATOR = "";

    public XNAHelp(){
        this.listHelp = new ArrayList<>();
    }

    private void newHelpItem(){
        this.helpItem = new XNAHelpItem();
    }

    public void add(String name){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setCheck(IS_CHECK);
        this.listHelp.add(helpItem);
    }

    public void add(int offset, String name){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setCheck(IS_CHECK);
        this.helpItem.setOffsetLeft(offset);
        this.listHelp.add(helpItem);
    }

    public void addNoCheck(String name){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setCheck(false);
        this.listHelp.add(helpItem);
    }

    public void addHide(String name){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setHide(IS_HIDE);
        this.helpItem.setCheck(IS_CHECK);
        this.listHelp.add(helpItem);
    }

    public void addHideNoCheck(String name){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setHide(IS_HIDE);
        this.helpItem.setCheck(false);
        this.listHelp.add(helpItem);
    }

    public void add(String name, int icon){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setIcon(icon);
        this.helpItem.setCheck(IS_CHECK);
        this.listHelp.add(helpItem);
    }

    public void addNoCheck(String name, int icon){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setIcon(icon);
        this.helpItem.setCheck(false);
        this.listHelp.add(helpItem);
    }

    public void addHide(String name, int icon){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setIcon(icon);
        this.helpItem.setHide(IS_HIDE);
        this.helpItem.setCheck(IS_CHECK);
        this.listHelp.add(helpItem);
    }

    public void addHideNoCheck(String name, int icon){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setIcon(icon);
        this.helpItem.setHide(IS_HIDE);
        this.helpItem.setCheck(false);
        this.listHelp.add(helpItem);
    }

    public void add(String name, int icon, int counter){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setIcon(icon);
        this.helpItem.setCheck(IS_CHECK);
        this.helpItem.setCounter(counter);
        this.listHelp.add(helpItem);
    }

    public void addNoCheck(String name, int icon, int counter){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setIcon(icon);
        this.helpItem.setCheck(false);
        this.helpItem.setCounter(counter);
        this.listHelp.add(helpItem);
    }

    public void addHide(String name, int icon, int counter){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setIcon(icon);
        this.helpItem.setCounter(counter);
        this.helpItem.setHide(IS_HIDE);
        this.helpItem.setCheck(IS_CHECK);
        this.listHelp.add(helpItem);
    }

    public void addHideNoCheck(String name, int icon, int counter){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setIcon(icon);
        this.helpItem.setCounter(counter);
        this.helpItem.setHide(IS_HIDE);
        this.helpItem.setCheck(false);
        this.listHelp.add(helpItem);
    }

    public void addSubHeader(String name){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setIcon(NO_ICON);
        this.helpItem.setHeader(IS_HEADER);
        this.helpItem.setCheck(false);
        this.listHelp.add(helpItem);
    }

    public void addSubHeader(String name, int counter){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setCounter(counter);
        this.helpItem.setIcon(NO_ICON);
        this.helpItem.setCheck(false);
        this.helpItem.setHeader(IS_HEADER);
        this.listHelp.add(helpItem);
    }

    public void addSeparator(){
        this.newHelpItem();
        this.helpItem.setName(ONLY_SEPARATOR);
        this.helpItem.setIcon(NO_ICON);
        this.helpItem.setHeader(IS_HEADER);
        this.helpItem.setCheck(false);
        this.listHelp.add(helpItem);
    }

    public void addSubHeaderHide(String name, int icon){
        this.newHelpItem();
        this.helpItem.setName(name);
        this.helpItem.setIcon(icon);
        this.helpItem.setHeader(IS_HEADER);
        this.helpItem.setHide(IS_HIDE);
        this.helpItem.setCheck(false);
        this.listHelp.add(helpItem);
    }

    public XNAHelpItem get(int position){
        return this.listHelp.get(position);
    }

    public List<XNAHelpItem> getHelp(){
        return this.listHelp;
    }

    public int getCount(){
        return this.listHelp.size();
    }
}
