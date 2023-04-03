package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.frontierspb.models.Customer;
import ru.frontierspb.util.cache.Cache;
import ru.frontierspb.util.exceptions.VerificationException;
import ru.frontierspb.util.messages.ExceptionsMessages;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerVerificationService {
    private final SmsServiceTMP smsServiceTMP;
    private final com.google.common.cache.Cache<String, Customer> cache = Cache.getStringCustomerCache();
    // TODO Другой тип кеша, не спринговый. Нормально ли?

    public void sendCode(Customer customer) {
        String code = generateCode();
        cache.put(code, customer);
        smsServiceTMP.sendCode(customer.getPhoneNumber(), code);
    }

    public void verifyCode(String code)
            throws VerificationException {
        Map<String, String> errors = new HashMap<>();

        if(Optional.ofNullable(cache.getIfPresent(code)).isEmpty()) {
            errors.put("message.verificationCode.notMatch",
                       ExceptionsMessages.getMessage("message.verificationCode.notMatch"));
            throw new VerificationException(errors);
        }
    }

    public Customer getCustomerAndDeleteCache(String code) {
        Customer customer = cache.getIfPresent(code);
        cache.invalidate(code);
        return customer;
    }

    private String generateCode() {
        return String.format("%04d", 1234); // TODO String.format("%04d", new Random().nextInt(9999));
    }
}
