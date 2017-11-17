package com.example.bisma.calendar_analyzer.db.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 2016-12-28 14:51.
 *
 * @author M.Allaudin
 */
class DbUtils {

    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void run(Runnable runnable) {
        executorService.submit(runnable);
    }

} // DbUtils
