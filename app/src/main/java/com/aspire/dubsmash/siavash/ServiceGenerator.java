package com.aspire.dubsmash.siavash;

import retrofit.RestAdapter;
import retrofit.client.UrlConnectionClient;

/**
 * Created by sia on 10/1/15.
 */
public class ServiceGenerator {
    // No need to instantiate this class.
    private ServiceGenerator() {
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        return new RestAdapter.Builder().setEndpoint(baseUrl).setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new UrlConnectionClient()).build().create(serviceClass);
    }
}
