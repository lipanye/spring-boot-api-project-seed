package com.company.project.core;

import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * @Author Lipanye_Arthur@163.com
 * @Date 2020/3/16 0016 14:10
 * @Decription Service 层基础接口，其他Service接口 请继承该接口
 */
public interface Service<T> {
    /**
     * 进行持久化
     * @param model
     */
    void save(T model);

    /**
     * 批量持久化
     * @param models
     */
    void save(List<T> models);

    /**
     *通过主键删除
     * @param id
     */
    void deleteById(Integer id);

    /**
     *批量通过id删除
     * @param ids eg:ids ->"1,2,3,4"
     */
    void deleteByIds(String ids);

    /**
     *更新
     * @param model
     */
    void update(T model);

    /**
     *通过ID查找
     * @param id
     * @return
     */
    T findById(Integer id);

    /**
     *通过model中某个成员变量变成查找（非数据表中的column的名称），value需要符合uniqu约束
     * @param fieldName
     * @param value
     * @return
     * @throws TooManyResultsException
     */
    T findBy(String fieldName,Object value) throws TooManyResultsException;

    /**
     *通过多个id查找
     * @param ids eg: id ->1,2,3,4"
     * @return
     */
    List<T>findByIds(String ids);

    /**
     *根据条件查找
     * @param condition
     * @return
     */
    List<T>findByCondition(Condition condition);

    /**
     * 查找所有数据
     * @return
     */
    List<T> findAll();

}
