package api.pot.hl.xactivity.Model;

import android.util.SparseIntArray;

import java.util.List;

public class XNavigation {
    public List<String> nameItem;
    public List<Integer> iconItem;
    public List<Integer> headerItem;
    public List<Integer> hideItem;
    public SparseIntArray countItem;
    public int colorSelected = 0;
    public boolean removeSelector = false;

    public static final int THEME_DARK = 0;
    public static final int THEME_LIGHT = 1;
}
