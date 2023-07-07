//package com.zsy.window;
//
//import cn.hutool.core.date.DateUtil;
//
//import java.sql.Timestamp;
//import java.time.LocalTime;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @auth: zsy
// * @date: 2023/1/5 14:18
// */
//public class Test1 {
//    public static void main(String[] args) {
//
//
//        System.out.println(LocalTime.MIN);
//        System.out.println(Timestamp.valueOf(DateUtil.parseLocalDateTime("2023-01-01","yyyy-MM-dd").with(LocalTime.MIN)));
//
//        System.out.println(LocalTime.MAX);
////        StringUtils
//
//        Map<String, Object> para = new HashMap<>();
//
//        para.put("startTime",Timestamp.valueOf(DateUtil.parseLocalDateTime("2023-01-01","yyyy-MM-dd").with(LocalTime.MIN)));
//        para.put("endTime",Timestamp.valueOf(DateUtil.parseLocalDateTime("2023-01-01","yyyy-MM-dd").with(LocalTime.MAX)));
//
//        System.out.println(para);
//
//    }
//}
