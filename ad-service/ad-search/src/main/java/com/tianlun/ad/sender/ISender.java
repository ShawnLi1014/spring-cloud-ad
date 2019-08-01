package com.tianlun.ad.sender;

import com.tianlun.ad.mysql.dto.MySqlRowData;

public interface ISender {

    void sender(MySqlRowData rowData);
}
