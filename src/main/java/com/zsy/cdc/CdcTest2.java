package com.zsy.cdc;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class CdcTest2 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        StreamTableEnvironment tabEnv = StreamTableEnvironment.create(env);

        tabEnv.executeSql("CREATE TABLE user_info(" +
                " id INT," +
                " name STRING," +
                "phone_num STRING" +
                ")WITH(" +
                " 'connector' = 'mysql-cdc', " +
                " 'hostname' = 'hadoop102', " +
                " 'port' = '3306', " +
                " 'username' = 'root', " +
                " 'password' = '000000', " +
                " 'database-name' = 'gmall-flink', " +
                " 'table-name' = 'zsy' " +
                ")"
        );
        tabEnv.executeSql("select * from user_info").print();

        env.execute();

    }
}
