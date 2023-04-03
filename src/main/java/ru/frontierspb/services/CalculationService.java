package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.frontierspb.models.Option;
import ru.frontierspb.util.enums.Extras;
import ru.frontierspb.util.exceptions.OptionNotFoundException;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CalculationService {
    private final OptionService optionService;

    public float calculatePrice(Extras options)
            throws OptionNotFoundException {
        return optionService.findByName(options.name()).getValue();
    }

    public float calculatePrice(LocalDateTime inDate, LocalDateTime outDate)
            throws OptionNotFoundException {
        float price = optionService.findByName("PRICE_PER_MINUTE").getValue() *
                      Duration.between(inDate, outDate).toMinutes();

        float maxPriceLimit = optionService.findByName("MAX_PRICE_LIMIT").getValue();
        float minPriceLimit = optionService.findByName("MIN_PRICE_LIMIT").getValue();

        price = Math.max(price, minPriceLimit);
        price = Math.min(price, maxPriceLimit);

        return price;
    }

    public int calculatePointsSpent(float price, int points)
            throws OptionNotFoundException {
        float minPriceLimit = optionService.findByName("MIN_PRICE_LIMIT").getValue();
        return price - points < minPriceLimit ? (int) (price - minPriceLimit) : points;
    }

    @Scheduled(cron = "0 0 * * ?")
    private void calculatePriceLimit()
            throws OptionNotFoundException {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        Option option = optionService.findByName("MAX_PRICE_LIMIT");

        if(dayOfWeek.equals(DayOfWeek.SUNDAY) || dayOfWeek.equals(DayOfWeek.SATURDAY)) {
            option.setValue(optionService.findByName("WEEKEND_PRICE").getValue());
        }
        else {
            option.setValue(optionService.findByName("WEEKDAYS_PRICE").getValue());
        }

        optionService.update(option);
    }
}
