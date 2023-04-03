package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.frontierspb.models.Option;
import ru.frontierspb.repositories.OptionsRepository;
import ru.frontierspb.util.exceptions.OptionNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OptionService {
    private final OptionsRepository optionsRepository;

    public void existsByName(String name)
            throws OptionNotFoundException {
        Map<String, String> errors = new HashMap<>();

        if(optionsRepository.findByName(name).isEmpty()){
            errors.put("message.option.notFound", name);
            throw new OptionNotFoundException(errors);
        }
    }

    @Cacheable(value = "option", key = "#name")
    public Option findByName(String name)
            throws OptionNotFoundException {
        Map<String, String> errors = new HashMap<>();

        Optional<Option> foundOption = optionsRepository.findByName(name);

        if(foundOption.isEmpty()){
            errors.put("message.option.notFound", name);
            throw new OptionNotFoundException(errors);
        }

        return foundOption.get();
    }

    @Cacheable(value = "options", key = "#root.methodName + '_' + #result")
    public List<Option> findAll(){
        return optionsRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = {"option", "options"}, allEntries = true)
    public void update(Option option)
            throws OptionNotFoundException {
        existsByName(option.getName());
        optionsRepository.save(option);
        log.info("Option with name {} has been updated with new value {}", option.getName(), option.getValue());
    }
}
