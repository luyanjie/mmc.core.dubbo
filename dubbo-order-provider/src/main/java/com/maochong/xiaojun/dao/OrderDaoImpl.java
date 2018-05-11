package com.maochong.xiaojun.dao;

import com.maochong.xiaojun.beans.OrderResponse;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author jokin
 * */
@Repository
public class OrderDaoImpl {

    private static final String SELECTONE = "com.maochong.xiaojun.OrderMapper.selectOne";

    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    public OrderResponse getUser(Map<String, Object> paramMap)
    {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession();
        return session.selectOne(SELECTONE,paramMap);
    }
}
