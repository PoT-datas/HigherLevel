package api.pot.hl.vp;

import android.view.View;
import android.widget.CompoundButton;

public interface ValueParamListener {
    void onPrinterUp(ValueParam valueParam);
    void onPrinterDown(ValueParam valueParam);
    ValueParamAdapter onParamChange(ValueParam valueParam);
    void onParamEditClick(View view);
    void onParamCheckedChanged(CompoundButton compoundButton, boolean isChecked);
    void onParamClick(View view);
    void onParamLongClick(View view);
}
