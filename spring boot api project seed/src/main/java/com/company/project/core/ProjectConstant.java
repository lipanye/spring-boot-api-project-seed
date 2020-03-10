package com.company.project.core;

public class ProjectConstant {
    /**
     * 生成代码s所在的基础包的路径，可根据自己公司的项目修改
     * 修改之后，要手动的修改src目录项目的默认的包路径
     */
    public static final String BASE_PACKAGE="com.company.project";
    /**
     * 生成的model路径
     */
    public static final String MODEL_PACKAGE = BASE_PACKAGE+".model";
    /**
     * 生成的mapper路径
     */
    public static final String MAPPER_PACKAGE = BASE_PACKAGE+".dao";
    /**
     * 生成的service路径
     */
    public static final String SERVICE_PAKAGE = BASE_PACKAGE+".service";
    /**
     * 生成的service接口实现类的路径
     */
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PAKAGE+".impl";
    /**
     * 生成的controller路径
     */
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE+".web";
    /**
     * Mapper插件基础接口d的完全限定名
     */
    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE+".core.Mapper";


}
