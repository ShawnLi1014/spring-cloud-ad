package com.tianlun.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    // 设备编码
    private String deviceCode;

    // 设备Mac地址
    private String mac;

    // ip
    private String ip;

    // 机型编码
    private String model;

    // 分辨率
    private String displaySize;

    // 屏幕尺寸
    private String screenSize;

    // 序列号
    private String serialName;
}
