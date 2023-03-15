package api.pot.hl.xactivity.xnav;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import api.pot.hl.R;
import api.pot.hl.xactivity.Model.XNAHelpItem;
import api.pot.hl.xactivity.Model.XNavigation;
import api.pot.hl.xactivity.adapter.XNItemAdapter;

public class XNAList {

    public static List<XNItemAdapter> getNavigationAdapter(Context context, XNavigation navigation) {

        List<XNItemAdapter> mList = new ArrayList<>();
        if (navigation.nameItem == null || navigation.nameItem.size() == 0) {
            throw new RuntimeException(context.getString(R.string.list_null_or_empty));
        }

        int icon;
        int count;
        boolean isHeader;

        for (int i = 0; i < navigation.nameItem.size(); i++) {

            String title = navigation.nameItem.get(i);

            XNItemAdapter mItemAdapter;

            icon = (navigation.iconItem != null ? navigation.iconItem.get(i) : 0);
            isHeader = (navigation.headerItem != null && navigation.headerItem.contains(i));
            count = (navigation.countItem != null ? navigation.countItem.get(i, -1) : -1);

            boolean isVisible = false;
            if(navigation.hideItem != null){
                isVisible = navigation.hideItem.contains(i);
            }

            if (isHeader && icon > 0){
                throw new RuntimeException(context.getString(R.string.value_icon_should_be_0));
            }

            if (!isHeader) {
                if (title == null) {
                    throw new RuntimeException(context.getString(R.string.enter_item_name_position) + i);
                }

                if (title.trim().equals("")) {
                    throw new RuntimeException(context.getString(R.string.enter_item_name_position) + i);
                }
            }else{
                if (title == null) {
                    title = "";
                }

                if (title.trim().equals("")) {
                    title = "";
                }
            }

            mItemAdapter = new XNItemAdapter(title, icon, isHeader, count, navigation.colorSelected, navigation.removeSelector, !isVisible);
            mList.add(mItemAdapter);
        }
        return mList;
    }

    public static List<XNItemAdapter> getNavigationAdapter(Context context, List<XNAHelpItem> listHelpItem, int colorSelected, boolean removeSelector) {

        List<XNItemAdapter> mList = new ArrayList<>();
        if (listHelpItem == null || listHelpItem.size() == 0) {
            throw new RuntimeException(context.getString(R.string.list_null_or_empty));
        }

        int icon;
        int count;
        boolean isHeader;

        for (int i = 0; i < listHelpItem.size(); i++) {

            String title = listHelpItem.get(i).getName();

            XNItemAdapter mItemAdapter;

            icon = listHelpItem.get(i).getIcon();
            isHeader = listHelpItem.get(i).isHeader();
            count = listHelpItem.get(i).getCounter();

            boolean isVisible = listHelpItem.get(i).isHide();

            if (isHeader && icon > 0){
                throw new RuntimeException(context.getString(R.string.value_icon_should_be_0));
            }

            if (!isHeader) {
                if (title == null) {
                    throw new RuntimeException(context.getString(R.string.enter_item_name_position) + i);
                }

                if (title.trim().equals("")) {
                    throw new RuntimeException(context.getString(R.string.enter_item_name_position) + i);
                }
            }else{
                if (title == null) {
                    title = "";
                }

                if (title.trim().equals("")) {
                    title = "";
                }
            }

            mItemAdapter = new XNItemAdapter(title, icon, isHeader, count, colorSelected, removeSelector, !isVisible);

            mItemAdapter.offsetLeft = listHelpItem.get(i).getOffsetLeft();

            mList.add(mItemAdapter);
        }
        return mList;
    }
}
