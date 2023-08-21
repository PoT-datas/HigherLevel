package api.pot.hl.vp;

import java.util.List;

public interface VPListener {
    void onValueChange(String value);
    void onValueChange(List<VPChoice> choices, List<String> selections);
}
