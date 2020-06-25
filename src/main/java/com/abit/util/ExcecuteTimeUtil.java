package com.abit.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;


public class ExcecuteTimeUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcecuteTimeUtil.class);
    public static void start() {
        Map<String,Long> map = new ConcurrentHashMap<>();
        Long startTime = System.currentTimeMillis();
        map.put("startTime", startTime);
        map.put("refreshTime", startTime);
        Long no = ThreadLocalRandom.current().nextLong();
        map.put("no", no);
        logger.error(no + " - 计时开始!");
        System.out.println(no + " - 计时开始!");
        LocalThreadParams.setParams(map);
    }


    public static void note() {
        Map<String,Long> params = (ConcurrentHashMap<String,Long>)LocalThreadParams.getParams();
        long currentTime = System.currentTimeMillis();
        logger.error(params.get("no") + " 间隔耗时 : " + (currentTime - params.get("refreshTime")) + " 毫秒 ");
        System.out.println(params.get("no") + " 间隔耗时 : " + (currentTime - params.get("refreshTime")) + " 毫秒 ");
        params.put("refreshTime",currentTime);
    }

    public static void end() {
        note();
        Map<String,Long> params = (ConcurrentHashMap<String,Long>)LocalThreadParams.getParams();
        logger.error(params.get("no") + " 总耗时 : " + (System.currentTimeMillis() - params.get("startTime")) + " 毫秒 ");
        System.out.println(params.get("no") + " 总耗时 : " + (System.currentTimeMillis() - params.get("startTime")) + " 毫秒 ");
        LocalThreadParams.clean();
    }
}
