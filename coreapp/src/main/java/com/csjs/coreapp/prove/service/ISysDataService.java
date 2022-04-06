package com.csjs.coreapp.prove.service;

import java.util.List;
import java.util.Map;

public interface ISysDataService {

    Map getUserInfo(String sql);

    List<Map<String, Object>> getSmUserList();

    List<Map<String, Object>> getUserList(String checkUserId);

    List<String> getSqlField(String sql);

    String getMobileByUserId(String userId);

    Map<String, Object> getShouldSalary(String userId);
}
