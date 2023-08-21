package api.pot.hl.fingerprint.callback;

import api.pot.hl.fingerprint.utils.FingerprintToken;

/**
 * Created by Omar on 08/01/2018.
 */

public interface FingerprintDialogSecureCallback {
    void onAuthenticationSucceeded();
    void onAuthenticationCancel();
    void onNewFingerprintEnrolled(FingerprintToken token);
}
