import {User} from "oidc-client-ts";

export function getAuthorizationHeader() {
    const oidcStorage =sessionStorage.getItem(`oidc.user:${oidcConfig.authority}:${oidcConfig.client_id}`);
    if (!oidcStorage) {
        return null;
    }
    let user= User.fromStorageString(oidcStorage);
    console.log(`returning token for user ${user.profile.name}`);
    return user?`Bearer ${user.id_token}`:null;
}

export const oidcConfig = {
    authority: "https://accounts.google.com/",
    client_id: "749431863682-vkdjhd9q41jep2m210codmf8586uou7r.apps.googleusercontent.com",
    client_secret: "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", //There are plenty of discussions on the usage of client secret with Google OIDC provider and PKCE auth code flow (https://stackoverflow.com/q/60724690).
    redirect_uri: window.location.origin + "/",
    scope: "openid email profile",
    onSigninCallback: (_user: User | void): void => {
        window.history.replaceState(
            {},
            document.title,
            window.location.pathname
        )
    }
};
