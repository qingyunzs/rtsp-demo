package com.zrg.rtspdemo.controller;

import com.zrg.rtspdemo.config.CameraRtspConfig;
import com.zrg.rtspdemo.ws.BaseResponse;
import com.zrg.rtspdemo.ws.ConvertVideoPakcet;
import com.zrg.rtspdemo.ws.WsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/camera")
public class CameraController {
    @Autowired
    private WsHandler wsHandler;

    @Autowired
    private CameraRtspConfig cameraRtspConfig;

    /**
     * 推送摄像头信息（地址1）
     *
     * @param request
     * @param cameraType
     * @param receiveUrl
     * @param uid
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/push_sxt_rtsp")
    @ResponseBody
    public BaseResponse pushSxtRtsp(HttpServletRequest request, String cameraType, String ip, String port, String receiveUrl, String uid) {
        // 主要是这段，开启数据流推送，调用系统命令开始推送数据到地址2
        ConvertVideoPakcet doffmpeg = new ConvertVideoPakcet();
        String sessionId = request.getSession().getId();
        System.out.println("推送摄像头信息sessionId：" + sessionId);
        String rtspUrl = this.getRtspUrl(cameraType, ip, port);
        doffmpeg.pushVideoAsRTSP(rtspUrl, receiveUrl + "?id=" + uid, uid);

        BaseResponse base = new BaseResponse();
        base.setCode("0");
        base.setData(null);
        base.setMessage("查询数据");
        return base;
    }

    /**
     * 获取RTSP视频流URL
     *
     * @param cameraType
     * @param ip
     * @param port
     * @return
     */
    private String getRtspUrl(String cameraType, String ip, String port) {
        // 这里用主码流url
        String mainUrlFormat = cameraRtspConfig.getRtspMainUrlFormatByCamera(cameraType);
        return String.format(mainUrlFormat, ip, port);
    }
}
