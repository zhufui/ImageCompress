package com.example.zfutils.aes;

import java.security.Provider;

/**
 * author: zhufu
 * email : zhufui@sina.com
 * time  : 2018/03/29
 * desc  :
 * version : 1.0
 */

public final class CryptoProvider extends Provider {
    /**
     * Creates a Provider and puts parameters
     */
    public CryptoProvider() {
        super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
        put("SecureRandom.SHA1PRNG",
                "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
        put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
    }
}
