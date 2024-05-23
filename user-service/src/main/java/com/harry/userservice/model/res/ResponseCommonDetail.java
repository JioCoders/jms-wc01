package com.harry.userservice.model.res;

import com.harry.userservice.base.ResponseBase;
import com.harry.userservice.model.res.data.CommonData;

public class ResponseCommonDetail extends ResponseBase {

    CommonData data = new CommonData();

    public CommonData getData() {
        return data;
    }

    public void setData(CommonData data) {
        this.data = data;
    }
}
