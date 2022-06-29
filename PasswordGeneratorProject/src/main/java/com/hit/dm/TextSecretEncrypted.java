package com.hit.dm;

public class TextSecretEncrypted extends AbstractSecretEncrypted {
    private byte[] content;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
