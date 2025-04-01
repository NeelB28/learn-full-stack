package com.dailycodework.dreamshops.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public class ShowConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
