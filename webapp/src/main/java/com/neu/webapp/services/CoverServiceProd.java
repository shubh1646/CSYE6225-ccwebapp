package com.neu.webapp.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class CoverServiceProd implements CoverService {

}
