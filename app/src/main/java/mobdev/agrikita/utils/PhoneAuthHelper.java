package mobdev.agrikita.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthHelper {
    private final FirebaseAuth firebaseAuth;
    private String storedVerificationID;

    public PhoneAuthHelper() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void sendVerificationCode(String phoneNum, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNum)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    public void verifyPhoneNumberWithCode(String verificationId, String code, final PhoneAuthCallback callback) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(task.getResult().getUser());
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }
    public interface PhoneAuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }
}
