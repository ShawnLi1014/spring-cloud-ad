package com.tianlun.ad.sender;

import com.tianlun.ad.dto.MySqlRowData;

public interface ISender {

    void sender(MySqlRowData rowData);
}
