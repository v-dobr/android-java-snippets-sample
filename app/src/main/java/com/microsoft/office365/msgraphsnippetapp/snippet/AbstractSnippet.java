/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license.
 * See LICENSE in the project root for license information.
 */
package com.microsoft.office365.msgraphsnippetapp.snippet;

import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.DefaultClientConfig;
import com.microsoft.graph.core.IClientConfig;
import com.microsoft.graph.extensions.GraphServiceClient;
import com.microsoft.graph.extensions.IGraphServiceClient;
import com.microsoft.office365.msgraphsnippetapp.application.SnippetApp;

import retrofit.Callback;

import static com.microsoft.office365.msgraphsnippetapp.R.string.beta;

public abstract class AbstractSnippet<Service, Result> {

    private static final int mNameIndex = 0;
    private static final int mDescIndex = 1;
    private static final int mUrlIndex = 2;
    private static final int mO365VersionIndex = 3;
    private static final int mIsAdminRequiredIndex = 4;
    private static final int mCodeSnippetIndex = 5;

    public final Service mService;
    boolean mIsAdminRequired;
    private String mName, mDesc, mUrl, mO365Version, mCodeSnippet;

    /**
     * Snippet constructor
     *
     * @param category         Snippet category as corresponds to UI displayed sections (organization, me, groups, etc...)
     * @param descriptionArray The String array for the specified snippet
     */
    public AbstractSnippet(
            SnippetCategory<Service> category,
            Integer descriptionArray) {
        //Get snippet configuration information from the
        //XML configuration for the snippet
        getSnippetArrayContent(category, descriptionArray);

        mService = category.mService;
    }


    /**
     * Gets the items from the specified snippet XML string array and stores the values
     * in private class fields
     *
     * @param category         Snippet category as corresponds to UI displayed sections (organization, me, groups, etc...)
     * @param descriptionArray The String array for the specified snippet
     */
    private void getSnippetArrayContent(SnippetCategory<Service> category, Integer descriptionArray) {
        if (null != descriptionArray) {
            String[] params = SnippetApp.getApp().getResources().getStringArray(descriptionArray);

            try {
                mName = params[mNameIndex];
                mDesc = params[mDescIndex];
                mUrl = params[mUrlIndex];
                mO365Version = params[mO365VersionIndex];
                mCodeSnippet = params[mCodeSnippetIndex];
                String isAdminRequired = params[mIsAdminRequiredIndex];
                mIsAdminRequired = isAdminRequired.equalsIgnoreCase("true");
            } catch (IndexOutOfBoundsException ex) {
                throw new RuntimeException(
                        "Invalid array in "
                                + category.mSection
                                + " snippet XML file"
                        , ex);
            }
        } else {
            mName = category.mSection;
            mDesc = mUrl = null;
            mO365Version = null;

        }
    }

    /**
     * Returns the version segment of the endpoint url with input from
     * XML snippet description
     *
     * @return Which version of the endpoint to use (beta, v1, etc...)
     */
    protected String getVersion() {
        return mO365Version;
    }

    public boolean isBeta() {
        String betaString = SnippetApp.getApp().getString(beta);
        return mO365Version.equalsIgnoreCase(betaString);
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDesc;
    }

    public String getCodeSnippet() { return mCodeSnippet; }

    public String getUrl() {
        return mUrl;
    }

    public boolean getIsAdminRequired() {
        return mIsAdminRequired;
    }

    public abstract void request(Service service, Callback<Result> callback);
}