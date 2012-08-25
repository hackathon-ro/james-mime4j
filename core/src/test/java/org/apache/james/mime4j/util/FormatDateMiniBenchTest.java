/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mime4j.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class FormatDateMiniBenchTest {

    private final int TOTAL_DATES = 100;
    private final int RUN_COUNT = 10;
    private final Date date = new Date();
    private final DateTime dtime = new DateTime(date);
    private final int nThreads = 2;
    private final ExecutorService exec = Executors.newFixedThreadPool(nThreads);

    @Test
    public void testFormattingResult() throws Exception {
        String originalFormatDate = MimeUtil.formatDate(new Date(), TimeZone.getTimeZone("UTC"));
        String jodaTimeFormat = MimeUtil.formatDate(new DateTime(), DateTimeZone.UTC);
        String fastDateFormat = MimeUtil.formatDate2(new Date(), TimeZone.getTimeZone("UTC"));
        assertEquals(jodaTimeFormat, originalFormatDate);
        assertEquals(fastDateFormat, originalFormatDate);
    }

    @Test
    public void testFormatDate() throws Exception {
        for (int j = 0; j <= RUN_COUNT; j++) {
            exec.execute(new Runnable() {
                public void run() {
                    long start = System.currentTimeMillis();
                    for (int i = 0; i <= TOTAL_DATES; i++) {
                        MimeUtil.formatDate(date, TimeZone.getTimeZone("UTC"));
                    }
                    long end = System.currentTimeMillis() - start;
                    System.out.printf("Conversion took %d milis \n", end);
                }
            });
        }
        exec.awaitTermination(2, TimeUnit.SECONDS);
    }

    @Test
    public void testFormatDate2() throws Exception {
        for (int j = 0; j <= RUN_COUNT; j++) {
            exec.execute(new Runnable() {
                public void run() {
                    long start = System.currentTimeMillis();
                    for (int i = 0; i <= TOTAL_DATES; i++) {
                        MimeUtil.formatDate(dtime, DateTimeZone.UTC);
                    }
                    long end = System.currentTimeMillis() - start;
                    System.out.printf("Conversion took %d milis \n", end);
                }
            });
        }
        exec.awaitTermination(2, TimeUnit.SECONDS);
    }

    @Test
    public void testFormatDate3() throws Exception {
        for (int j = 0; j <= RUN_COUNT; j++) {
            exec.execute(new Runnable() {
                public void run() {
                    long start = System.currentTimeMillis();
                    for (int i = 0; i <= TOTAL_DATES; i++) {
                        MimeUtil.formatDate2(date, TimeZone.getTimeZone("UTC"));
                    }
                    long end = System.currentTimeMillis() - start;
                    System.out.printf("Conversion took %d milis \n", end);
                }
            });
        }
        exec.awaitTermination(2, TimeUnit.SECONDS);
    }
}

