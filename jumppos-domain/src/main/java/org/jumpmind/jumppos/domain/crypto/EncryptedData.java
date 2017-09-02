package org.jumpmind.jumppos.domain.crypto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class EncryptedData implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private byte[] plainData;

    private byte[] encryptedData;

    private String keyId;

    private boolean encrypted;

    public EncryptedData() {
    }

    public EncryptedData(byte[] plainData) {
        this.plainData = plainData;
    }

    public EncryptedData(String plainText) {
        this.plainData = plainText.getBytes();
    }

    public EncryptedData(byte[] plainData, byte[] encryptedData, String keyId) {
        this.plainData = plainData;
        this.encryptedData = encryptedData;
        this.keyId = keyId;
        this.encrypted = true;
    }

    public Object clone() {
        EncryptedData e = new EncryptedData();
        e.setPlainData(plainData);
        e.setEncryptedData(encryptedData);
        e.setKeyId(keyId);
        e.setEncrypted(encrypted);
        return e;
    }

    public int hashCode() {
        if (encrypted) {
            return encryptedData.hashCode();
        } else {
            return plainData.hashCode();
        }
    }

    public boolean equals(Object o) {
        if (o instanceof EncryptedData) {
            EncryptedData e = (EncryptedData) o;
            if (encrypted) {
                return encryptedData.equals(e.encryptedData);
            } else {
                return plainData.equals(e.plainData);
            }
        }
        return false;
    }

    public String getPlainText() {
        try {
            return new String(plainData, "UTF8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(byte[] encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public byte[] getPlainData() {
        return plainData;
    }

    public void setPlainData(byte[] plainData) {
        this.plainData = plainData;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
}
