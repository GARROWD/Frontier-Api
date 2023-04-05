package ru.frontierspb;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import ru.frontierspb.dto.responses.ReferralResponse;
import ru.frontierspb.dto.responses.ReferrerResponse;
import ru.frontierspb.models.Referent;

@SpringBootApplication
@OpenAPIDefinition
@EnableCaching
@Slf4j
public class FrontierApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrontierApplication.class, args);
    }

    @Bean
    public org.modelmapper.ModelMapper modelMapper() {
        org.modelmapper.ModelMapper modelMapper = new org.modelmapper.ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        log.info("Model Mapper created");
        // TODO Эта огромная настройка вместо дефолтной позволяет даункастить чет там куда-то там
        modelMapper.createTypeMap(Referent.class, ReferralResponse.class).addMappings(
                mapper -> mapper.map(src -> src.getReferral().getUsername(), ReferralResponse::setUsername));
        modelMapper.createTypeMap(Referent.class, ReferrerResponse.class).addMappings(
                mapper -> mapper.map(src -> src.getReferrer().getUsername(), ReferrerResponse::setUsername));

        /*
        modelMapper.createTypeMap(Booking.class, BookingResponse.class)
                   .addMappings(mapper -> mapper.using(
                           new AbstractConverter<Customer, Long>() {
                               @Override
                               protected Long convert(Customer customer) {
                                   return Optional.ofNullable(customer).map(Customer::getId).orElse(0L);
                               }
                           }).map(Booking::getCustomer, BookingResponse::setCustomerId));
        */

        return modelMapper;
    }
}
