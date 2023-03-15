package api.pot.hl.xactivity.interfaces;

import android.view.Menu;

public interface OnPrepareOptionsMenu {
    /**
     * Prepare options menu navigation (onPrepareOptionsMenu(Menu menu))
     * @param menu menu.
     * @param position last position of the item that was clicked.
     * @param visible use to hide the menu when the navigation is open.
     */
    public void onPrepareOptionsMenu(Menu menu, int position, boolean visible);

}
