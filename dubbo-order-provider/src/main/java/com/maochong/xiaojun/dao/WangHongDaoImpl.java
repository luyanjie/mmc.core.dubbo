package com.maochong.xiaojun.dao;

import com.maochong.xiaojun.beans.OrderResponse;
import com.maochong.xiaojun.beans.UserResponse;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author jokin
 * 为了测试多数据源
 * */
@Repository
public class WangHongDaoImpl {

    private static final String SELECTONE = "com.maochong.xiaojun.WanghongMapper.selectOne";

    @Autowired
    @Qualifier("sqlSessionTemplate_c")
    private SqlSessionTemplate sqlSessionTemplate;

    public UserResponse getUser(Map<String, Object> paramMap)
    {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession();
        return session.selectOne(SELECTONE,paramMap);
    }
}
