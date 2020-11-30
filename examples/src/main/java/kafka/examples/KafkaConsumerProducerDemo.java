/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kafka.examples;

import org.apache.kafka.common.errors.TimeoutException;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class KafkaConsumerProducerDemo {
    public static void main(String[] args) throws InterruptedException {
        long start = System.nanoTime();
        boolean isAsync = args.length == 0 || !args[0].trim().equalsIgnoreCase("sync");
        int numRecords = (args.length >= 2) ? Integer.parseInt(args[1].trim()) : 10000000; // default 10 million
        int recordSize = (args.length >= 3) ? Integer.parseInt(args[2].trim()) : 100;
        System.out.println(numRecords);
        CountDownLatch latch = new CountDownLatch(2);
        Producer producerThread = new Producer(KafkaProperties.TOPIC, isAsync, null, false, numRecords, -1, latch, recordSize);
        producerThread.start();

        Consumer consumerThread = new Consumer(KafkaProperties.TOPIC, "DemoConsumer", Optional.empty(), false, numRecords, latch);
        consumerThread.start();

        if (!latch.await(60, TimeUnit.MINUTES)) {
            throw new TimeoutException("Timeout after 60 minutes waiting for demo producer and consumer to finish");
        }

        consumerThread.shutdown();
        long end = System.nanoTime();
        long runtime = end - start;

        try {
            FileWriter fw = new FileWriter(new File("/home/cc/kafka/data/total_runtime.txt"));
            String outstr = runtime + "\n";
            fw.write(outstr);
            fw.flush();
            fw.close();
            System.out.println("Time spent appending: " + runtime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(runtime);
        System.out.println("All finished!");
    }
}
