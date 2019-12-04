package com.wmh.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * @author: Wangmh
 * @date: 2019/3/8
 * @time: 10:36
 */
public class MpGenerator {
    public static void main(String[] args) {
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());

        //全局配置
        GlobalConfig config = new GlobalConfig();
        config.setAuthor("wmh");
        config.setOutputDir("D://ideas//MyBatisPlus//MyBatisPlus//src//main//java");
        config.setFileOverride(false);//是否覆盖同名文件，默认false
        config.setActiveRecord(true);//不需要ActiveRecord特性的请改为false
        config.setEnableCache(false);//XML 二级缓存
        config.setBaseResultMap(true); //XML ResultMap
        config.setBaseColumnList(false);//XML columList

        //自定义文件命名 注意%s会自动填充表实体属性
//        config.setMapperName("%sDao");
//        config.setXmlName("%sDao");
//        config.setServiceName("%sService");
//        config.setServiceImplName("%sServiceImpl");
//        config.setControllerName("%sController");

        autoGenerator.setGlobalConfig(config);

        //数据源配置
        DataSourceConfig sourceConfig = new DataSourceConfig();
        sourceConfig.setDbType(DbType.MYSQL);
        sourceConfig.setTypeConvert(new MySqlTypeConvert() {
            //自定义数据库表字段类型转换 可选
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型" + fieldType);
                // 注意！！processTypeConvert 存在默认类型转换，
                // 如果不是你要的效果请自定义返回、非如下直接返回。
                return super.processTypeConvert(fieldType);
            }
        });

        sourceConfig.setDriverName("com.mysql.jdbc.Driver");
        sourceConfig.setUsername("root");
        sourceConfig.setPassword("");
        sourceConfig.setUrl("jdbc:mysql://localhost:3306/my?useUnicode=true&characterEncoding=utf8");
        autoGenerator.setDataSource(sourceConfig);

        //策略配置
        StrategyConfig strategy = new StrategyConfig();
//        strategy.setCapitalMode(true);//全局大写命名 ORACLE 注意
        strategy.setTablePrefix(new String[]{"user_"});//此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.nochange);//表名生成策略
        strategy.setInclude(new String[]{"user"});//需要生成的表
//        strategy.setExclude();//排除生成的表


//        自定义实体父类
//        strategy.setSuperEntityClass("com.pojo.base");
//        //自定义实体，公共字段
//        strategy.setSuperEntityColumns(new String[]{"test_id"});
//        //自定义mapper父类
//        strategy.setSuperMapperClass("com.mapper.base");
//        //自定义service父类
//        strategy.setSuperServiceClass("com.service.base");
//        //自定义service实现类父类
//        strategy.setSuperServiceImplClass("com.service.impl.base");
//        //自定义controller父类
//        strategy.setSuperControllerClass("com.controller.base");
//        //【实体】是否生成字段常量（默认 false）
//         public static final String ID = "test_id";
//         strategy.setEntityColumnConstant(true);
//        //【实体】是否为构建者模型（默认 false）
//         public User setName(String name) {this.name = name; return this;}
//         strategy.setEntityBuilderModel(true);
        autoGenerator.setStrategy(strategy);

        //包配置
        PackageConfig packageConfig=new PackageConfig();
//        packageConfig.setParent("com.plus");
//        packageConfig.setModuleName("test");
        packageConfig.setController("com.plus.controller");
        packageConfig.setEntity("com.plus.pojo");
        packageConfig.setService("com.plus.service");
        packageConfig.setServiceImpl("com.plus.service.impl");
        packageConfig.setMapper("com.plus.mapper");
        packageConfig.setXml("com.plus.mapper");
        autoGenerator.setPackageInfo(packageConfig);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        // InjectionConfig cfg = new InjectionConfig() {
        // @Override
        // public void initMap() {
        // Map<String, Object> map = new HashMap<String, Object>();
        // map.put("abc", this.getConfig().getGlobalConfig().getAuthor() +
        // "-mp");
        // this.setMap(map);
        // }
        // };
        //
        // // 自定义 xxList.jsp 生成
        // List<FileOutConfig> focList = new ArrayList<>();
        // focList.add(new FileOutConfig("/template/list.jsp.vm") {
        // @Override
        // public String outputFile(TableInfo tableInfo) {
        // // 自定义输入文件名称
        // return "D://my_" + tableInfo.getEntityName() + ".jsp";
        // }
        // });
        // cfg.setFileOutConfigList(focList);
        // mpg.setCfg(cfg);
        //
        // // 调整 xml 生成目录演示
        // focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
        // @Override
        // public String outputFile(TableInfo tableInfo) {
        // return "/develop/code/xml/" + tableInfo.getEntityName() + ".xml";
        // }
        // });
        // cfg.setFileOutConfigList(focList);
        // mpg.setCfg(cfg);
        //
        // // 关闭默认 xml 生成，调整生成 至 根目录
        // TemplateConfig tc = new TemplateConfig();
        // tc.setXml(null);
        // mpg.setTemplate(tc);

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        // TemplateConfig tc = new TemplateConfig();
        // tc.setController("...");
        // tc.setEntity("...");
        // tc.setMapper("...");
        // tc.setXml("...");
        // tc.setService("...");
        // tc.setServiceImpl("...");
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        // mpg.setTemplate(tc);

        // 执行生成
        autoGenerator.execute();


    }
}
