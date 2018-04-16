package uk.co.maboughey.moqreq.utils;

import org.slf4j.Logger;

public class Log {
    private Logger log;

    public Log(Logger logger) {
        log = logger;
    }
    public void info(String message){
        log.info(message);
    }
    public void warn(String message){
        log.warn(message);
    }
    public void error(String message){
        log.error(message);
    }
}
