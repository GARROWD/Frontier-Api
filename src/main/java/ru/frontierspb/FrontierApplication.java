package ru.frontierspb;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.AbstractConverter;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import ru.frontierspb.dto.responses.BookingResponse;
import ru.frontierspb.dto.responses.CustomerSessionResponse;
import ru.frontierspb.models.Booking;
import ru.frontierspb.models.Customer;
import ru.frontierspb.models.Session;

import java.util.Optional;

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
        /* TODO Эта огромная настройка вместо дефолтной позволяет даункастить customer до customerId, и еще
            надо бы нормальное логирование уже завести */
        modelMapper.createTypeMap(Session.class, CustomerSessionResponse.class)
                   .addMappings(mapper -> mapper.using(
                           new AbstractConverter<Customer, Long>() {
                               @Override
                               protected Long convert(Customer customer) {
                                   return Optional.ofNullable(customer).map(Customer::getId).orElse(0L);
                               }
                           }).map(Session::getCustomer, CustomerSessionResponse::setCustomerId));

        modelMapper.createTypeMap(Booking.class, BookingResponse.class)
                   .addMappings(mapper -> mapper.using(
                           new AbstractConverter<Customer, Long>() {
                               @Override
                               protected Long convert(Customer customer) {
                                   return Optional.ofNullable(customer).map(Customer::getId).orElse(0L);
                               }
                           }).map(Booking::getCustomer, BookingResponse::setCustomerId));

        return modelMapper;
    }
}
