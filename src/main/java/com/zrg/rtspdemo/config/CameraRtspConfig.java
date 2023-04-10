package com.zrg.rtspdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 获取RTSP视频流config
 */
@Configuration
public class CameraRtspConfig {
    /**
     * 华夏
     */
    public static final String CAMERA_HUAXIA = "HUA_XIA";
    /**
     * 臻识
     */
    public static final String CAMERA_ZHENSHI = "ZHEN_SHI";

    /**
     * 臻识-主码流
     */
    @Value("${rtsp.zhenshi.main.url.format}")
    private String rtspZhenshiMainUrlFormat;

    /**
     * 臻识-子码流
     */
    @Value("${rtsp.zhenshi.sub.url.format}")
    private String rtspZhenshiSubUrlFormat;

    /**
     * 华夏-主码流
     */
    @Value("${rtsp.huaxia.main.url.format}")
    private String rtspHuaxiaMainUrlFormat;

    /**
     * 华夏-子码流
     */
    @Value("${rtsp.huaxia.sub.url.format}")
    private String rtspHuaxiaSubUrlFormat;

    /**
     * Get RTSP Main URL Format by Camera.
     *
     * @param cameraType
     * @return
     */
    public String getRtspMainUrlFormatByCamera(String cameraType) {
        String mainUrlFormat = "";
        switch (cameraType) {
            case CAMERA_ZHENSHI:
                mainUrlFormat = rtspZhenshiMainUrlFormat;
                break;
            case CAMERA_HUAXIA:
                mainUrlFormat = rtspHuaxiaMainUrlFormat;
                break;
            default:
                break;
        }
        return mainUrlFormat;
    }

    /**
     * Get RTSP Sub URL Format by Camera.
     *
     * @param cameraType
     * @return
     */
    public String getRtspSubUrlFormatByCamera(String cameraType) {
        String subUrlFormat = "";
        switch (cameraType) {
            case CAMERA_ZHENSHI:
                subUrlFormat = rtspZhenshiSubUrlFormat;
                break;
            case CAMERA_HUAXIA:
                subUrlFormat = rtspHuaxiaSubUrlFormat;
                break;
            default:
                break;
        }
        return subUrlFormat;
    }
}
