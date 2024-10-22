package com.margo.iPopUp.dao;

import com.margo.iPopUp.domain.Danmu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DanmuDao {

    Long addDanmu(Danmu danmu);

    List<Danmu> getDanmus(Map<String,Object> params);
}
