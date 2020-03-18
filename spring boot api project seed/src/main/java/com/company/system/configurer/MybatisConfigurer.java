package com.company.system.configurer;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;

import java.util.Properties;

import static com.company.system.core.ProjectConstant.*;


/**
 * @Descript Mybatis 组件基本配置
 * @Date 2020年3月10日 11点42分
 * @author lipanye
 */
@Configuration
public class MybatisConfigurer {
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage(MODEL_PACKAGE);

        //配置分页插件，其他配置请查阅官方文档
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        //分页尺寸为0时查询所有记录不再执行分页
        properties.setProperty("pageSizeZero","true");
        //页码<=0 查询第一页，页码>=总页数查询最后一页
        properties.setProperty("reasonable","true");
        //支持通过Mapper接口参数来传递分页参数
        properties.setProperty("supportMethodsArguments","true");
        pageHelper.setProperties(properties);

        //添加插件
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});

        //添加xml映射文件路径
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources("classpath:mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(MAPPER_PACKAGE);

        //配置通用mapper，详情请查阅官方文档
        Properties properties = new Properties();
        //配置mapper全限定名
        properties.setProperty("mappers",MAPPER_INTERFACE_REFERENCE);
        //insert、update是否判断字符串类型 !=''，即test="str != null"表达式内是否追加 and str !=''
        properties.setProperty("notEmpty","false");
        properties.setProperty("IDENTITY","MYSQL");
        mapperScannerConfigurer.setProperties(properties);
        return  mapperScannerConfigurer;
    }



}
