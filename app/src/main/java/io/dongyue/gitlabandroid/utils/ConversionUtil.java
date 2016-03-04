package io.dongyue.gitlabandroid.utils;

import android.net.Uri;


import java.util.Date;

import io.dongyue.gitlabandroid.model.Account;

public final class ConversionUtil {
    private ConversionUtil() {}

    public static String fromUri(Uri uri) {
        if (uri == null) {
            return null;
        }

        return uri.toString();
    }

    public static Uri toUri(Account account, String uriString) {
        if (uriString == null) {
            return null;
        }
        if (uriString.isEmpty()) {
            return Uri.EMPTY;
        }

        Uri uri = Uri.parse(uriString);
        if (!uri.isRelative()) {
            return uri;
        }

        if (account == null) {
            return uri;
        }

        Uri.Builder builder = account.getServerUrl()
                .buildUpon()
                .encodedQuery(uri.getEncodedQuery())
                .encodedFragment(uri.getEncodedFragment());

        if (uri.getPath().startsWith("/")) {
            builder.encodedPath(uri.getEncodedPath());
        } else {
            builder.appendEncodedPath(uri.getEncodedPath());
        }

        return builder.build();
    }
}
