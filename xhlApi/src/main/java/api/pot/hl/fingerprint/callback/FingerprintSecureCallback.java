package api.pot.hl.fingerprint.callback;

import api.pot.hl.fingerprint.utils.FingerprintToken;

/**
 * Created by Omar on 02/07/2017.
 */

public interface FingerprintSecureCallback {
    void onAuthenticationSucceeded();
    void onAuthenticationFailed();
    void onNewFingerprintEnrolled(FingerprintToken token);
    void onAuthenticationError(int errorCode, String error);
}
