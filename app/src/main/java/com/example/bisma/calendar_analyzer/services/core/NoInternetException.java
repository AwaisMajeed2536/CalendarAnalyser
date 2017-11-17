package com.example.bisma.calendar_analyzer.services.core;


import com.example.bisma.calendar_analyzer.helpers.Constants;

import java.io.IOException;

public class NoInternetException extends IOException {
    @Override
    public String getMessage() {
        return Constants.ERROR_INTERNET_CONNECTION;
    }

}