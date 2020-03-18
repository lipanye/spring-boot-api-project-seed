package com.company.system.core;

import com.company.system.exception.ServiceException;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @Author Lipanye_Arthur@163.com
 * @Date 2020/03/16 16:38
 * @Decription
 */
public abstract class AbstractService<T> implements Service<T> {
    @Autowired
    protected Mapper<T> mapper;
    /**
     * 当前泛型真实类型的class
     */
    private Class<T> modelClass;

    /**
     * ParameterizedType  getGenericSuperclass，用来获取继承该类的父类
     */
    public AbstractService() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) type.getActualTypeArguments()[0];

    }

    @Override
    public void save(T model) {
        mapper.insertSelective(model);
    }

    @Override
    public void save(List<T> models) {
        mapper.insertList(models);
    }

    @Override
    public void deleteById(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByIds(String ids) {
        mapper.deleteByIds(ids);
    }

    @Override
    public void update(T model) {
        mapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public T findById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public T findBy(String fieldName, Object value) throws TooManyResultsException {
        try {
            /**
             * modelClass.newInstance()，通过反射，调用类的无参构造器，来构造对象
             */
            T model =modelClass.newInstance();
            /**
             * Field 在反射中Field描述的是类的属性信息
             * 功能包括：
             * 1-获取当前对象的成员变量类型
             * 2-给成员变量重新设值
             * 如何获取Field对象，有四种方法
             * 1-Class.getFields()：获取类中的public属性，返回一个包含某些Field对象的数组，该数组包含此Class对象所表示
             * 的类或接口所有可访问公共字段
             * 2-getDeclaredField()：获取类中的所有属性(public、private、default、protected)，但不包括继承的属性，
             * 返回Field对象的一个数组
             * 3-getField(String fieldName)：获取类特定的方法，fieldName指定了属性的名称
             * 4-getDeclaredField(fieldName)：获取类特定的方法，fieldName指定了属性的名称
             */
            Field field =modelClass.getDeclaredField(fieldName);
            /**
             * field.setAccessible(true),不管成员变量是private、protected、default、public，isAccessible()方法都返回false
             * 但是f访问(f.get(obj))private 修饰的成员变量，需要setAccessible(true)，否则h会抛出IllegalAccessException,
             * e而其他访问权限xi修饰的成员变量可以直接被访问。
             */
            field.setAccessible(true);
            /**
             * 设置属性
             */
            field.set(model,value);
            return mapper.selectOne(model);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    @Override
    public List<T> findByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }
}
