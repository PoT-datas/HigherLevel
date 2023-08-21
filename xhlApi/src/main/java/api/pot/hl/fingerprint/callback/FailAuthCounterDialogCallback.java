package api.pot.hl.fingerprint.callback;

import api.pot.hl.fingerprint.FingerprintDialog;

public interface FailAuthCounterDialogCallback {
    void onTryLimitReached(FingerprintDialog fingerprintDialog);
}
