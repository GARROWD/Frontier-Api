package ru.frontierspb.util.cache;

import com.google.common.cache.CacheBuilder;
import ru.frontierspb.models.Customer;

import java.util.concurrent.TimeUnit;

public class Cache {
    /* TODO Почему это не сделано в FrontierApplication как @Bean (как ModelMapper)? Потому что я думаю, что мне
        понадобиться и другие типы кешей, поэтому есть отдельный класс, распределяющий кеши */

    private final static com.google.common.cache.Cache<String, Customer> stringCustomerCache =
            CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();

    public static com.google.common.cache.Cache<String, Customer> getStringCustomerCache() {
        return stringCustomerCache;
    }
}
