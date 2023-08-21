package api.pot.hl.fingerprint.callback;

import api.pot.hl.fingerprint.Fingerprint;

/**
 * Created by Omar on 10/07/2017.
 */

public interface FailAuthCounterCallback {
    void onTryLimitReached(Fingerprint fingerprint);
}
