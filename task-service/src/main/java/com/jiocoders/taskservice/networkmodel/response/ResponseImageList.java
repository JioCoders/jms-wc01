package com.jiocoders.taskservice.networkmodel.response;

import com.jiocoders.taskservice.base.ResponseBase;
import com.jiocoders.taskservice.networkmodel.FileInfo;

import java.util.ArrayList;
import java.util.List;

public class ResponseImageList extends ResponseBase {

    public List<FileInfo> imageList = new ArrayList<>();
}
