package com;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: Wangmh
 * @date: 2019/3/8
 * @time: 11:17
 */
@MapperScan(value = "com.plus.mapper")
@SpringBootApplication
public class App {
    private static final Logger logger= LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
        System.out.println(" /$$      /$$           /$$$$$$$              /$$     /$$           /$$$$$$$  /$$                    \n" +
                "| $$$    /$$$          | $$__  $$            | $$    |__/          | $$__  $$| $$                    \n" +
                "| $$$$  /$$$$ /$$   /$$| $$  \\ $$  /$$$$$$  /$$$$$$   /$$  /$$$$$$$| $$  \\ $$| $$ /$$   /$$  /$$$$$$$\n" +
                "| $$ $$/$$ $$| $$  | $$| $$$$$$$  |____  $$|_  $$_/  | $$ /$$_____/| $$$$$$$/| $$| $$  | $$ /$$_____/\n" +
                "| $$  $$$| $$| $$  | $$| $$__  $$  /$$$$$$$  | $$    | $$|  $$$$$$ | $$____/ | $$| $$  | $$|  $$$$$$ \n" +
                "| $$\\  $ | $$| $$  | $$| $$  \\ $$ /$$__  $$  | $$ /$$| $$ \\____  $$| $$      | $$| $$  | $$ \\____  $$\n" +
                "| $$ \\/  | $$|  $$$$$$$| $$$$$$$/|  $$$$$$$  |  $$$$/| $$ /$$$$$$$/| $$      | $$|  $$$$$$/ /$$$$$$$/\n" +
                "|__/     |__/ \\____  $$|_______/  \\_______/   \\___/  |__/|_______/ |__/      |__/ \\______/ |_______/ \n" +
                "              /$$  | $$                                                                              \n" +
                "             |  $$$$$$/                                                                              \n" +
                "              \\______/                                                                               ");
        logger.info("========================启动完毕========================");

    }
}
