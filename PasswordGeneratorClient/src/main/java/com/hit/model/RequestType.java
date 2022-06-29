package com.hit.model;

public enum RequestType {
    Authenticate,
    Register,
    Unregister,
    ChangePassword,
    AddCardSecret,
    AddIdentitySecret,
    AddLoginSecret,
    AddTextSecret,
    UpdateCardSecret,
    UpdateIdentitySecret,
    UpdateLoginSecret,
    UpdateTextSecret,
    GetSecrets,
    RemoveSecret
}
