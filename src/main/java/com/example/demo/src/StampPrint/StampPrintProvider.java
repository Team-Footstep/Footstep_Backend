package com.example.demo.src.StampPrint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StampPrintProvider {
    private final StampPrintDao stampPrintDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    public StampPrintProvider(StampPrintDao stampPrintDao) {
        this.stampPrintDao = stampPrintDao;
    }


}
