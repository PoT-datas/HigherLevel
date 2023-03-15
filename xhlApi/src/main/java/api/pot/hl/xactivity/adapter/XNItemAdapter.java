package api.pot.hl.xactivity.adapter;

public class XNItemAdapter {

    public String title;
    public int counter;
    public int icon;
    public boolean isHeader;
    public int colorSelected = 0;
    public boolean isVisible = true;
    public boolean checked = false;
    public boolean removeSelector = false;

    public int offsetLeft = 100;

    public XNItemAdapter(String title, int icon, boolean header, int counter, int colorSelected, boolean removeSelector, boolean isVisible) {
        this.title = title;
        this.icon = icon;
        this.isHeader = header;
        this.counter = counter;
        this.isVisible = isVisible;
        this.colorSelected = colorSelected;
        this.removeSelector = removeSelector;
    }

    public XNItemAdapter(String title, int icon, boolean header, int counter) {
        this.title = title;
        this.icon = icon;
        this.isHeader = header;
        this.counter = counter;
        this.isVisible = true;
    }
}
