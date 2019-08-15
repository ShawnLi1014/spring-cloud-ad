package com.tianlun.ad.mysql.listener;

import com.tianlun.ad.dto.BinlogRowData;

public interface Ilistener {

    void register();

    void onEvent(BinlogRowData eventData);
}
