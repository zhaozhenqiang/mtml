package com.mutoumulao.expo.redwood.base;

import javax.net.ssl.X509TrustManager;

public class TrustAllTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

    }

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}