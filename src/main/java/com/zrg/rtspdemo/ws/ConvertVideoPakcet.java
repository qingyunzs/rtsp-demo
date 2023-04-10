package com.zrg.rtspdemo.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ConvertVideoPakcet implements ApplicationRunner {
    private static Map<String, Process> processMap = new ConcurrentHashMap<>();

    public static Map<String, Process> getProcessMap() {
        return processMap;
    }

    public Process process;

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

    public Integer pushVideoAsRTSP(String id, String fileName, String token) {

        // 输出ffmpeg推流日志
        try {
            String command = "";
            command += "ffmpeg -rtsp_transport tcp"; // ffmpeg开头，-re代表按照帧率发送，在推流时必须有
            command += " -i \"" + id + "\""; // 指定要推送的视频
            command += " -r 30 -q 0 -f mpegts -codec:v mpeg1video -s 700x400 " + fileName; // 指定推送服务器，-f：指定格式   1280  720
            log.info("ffmpeg推流命令：{}", command);
            process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
            // 这里是为了区分不同的流，需求会打开多个摄像头，根据token
            // 后面可以对不需要的流关闭
            processMap.put(token, process);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 1;
    }
}
