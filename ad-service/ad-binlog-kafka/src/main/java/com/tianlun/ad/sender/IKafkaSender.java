package com.tianlun.ad.sender;

import com.tianlun.ad.dto.MySqlRowData;

public interface IKafkaSender {

    void sender(MySqlRowData rowData);
}
