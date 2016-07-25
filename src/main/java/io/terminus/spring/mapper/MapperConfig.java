package io.terminus.spring.mapper;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.boot.env.EnumerableCompositePropertySource;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author xgs.
 * @Email xgs@terminus.io
 * @Date 16/7/24
 */

@Configuration
public class MapperConfig implements EnvironmentAware{
    private static final Pattern PATTERN_PACKAGE = Pattern.compile("([a-zA-Z\\.]*)\\(([a-zA-Z\\.\\|]*)\\)([a-zA-Z\\.]*)");
    private static final Joiner JOINER_PACKAGE = Joiner.on(",").skipNulls();
    private static final Splitter SPLITTER_COMMA = Splitter.on(",").trimResults().omitEmptyStrings();
    private static final Splitter SPLITTER_VERTICAL = Splitter.on("|").trimResults().omitEmptyStrings();
    private static final String CONFIGURATION_PROPERTY_SOURCES_NAME =
            "org.springframework.boot.context.config.ConfigFileApplicationListener$ConfigurationPropertySources";

    private String basePackage;
    private Properties properties = new Properties();

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        if(Strings.isNullOrEmpty(basePackage)){
            throw new RuntimeException("property mapper.basePackage is not exists");
        }
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(getBasePackages(basePackage));
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }

    @Override
    public void setEnvironment(Environment environment) {
        MetaObject envMeta = SystemMetaObject.forObject(environment);
        PropertySources propertySources = (PropertySources)envMeta.getValue("propertySources");
        for(PropertySource propertySource : propertySources) {
            if (Objects.equal(CONFIGURATION_PROPERTY_SOURCES_NAME, propertySource.getClass().getName())) {
                for(PropertySource propertySourceTemp : (Collection<PropertySource>)propertySource.getSource()){
                    if(propertySourceTemp instanceof EnumerableCompositePropertySource){
                        EnumerableCompositePropertySource configProperySource = (EnumerableCompositePropertySource) propertySourceTemp;
                        for(String key : configProperySource.getPropertyNames()){
                            if(key.startsWith("map") && !properties.contains(key)){
                                properties.put(key, configProperySource.getProperty(key));
                                properties.put(key.replace("map.", ""), configProperySource.getProperty(key));
                            }
                        }
                    }
                }
            }
        }
        basePackage = properties.getProperty("mapper.basePackage");
    }


    /**
     * 解析packageName拼接成mybatis可分解的packagenames
     * @param packageName 需要被处理的packagename字符串,
     *                    如typeAliasesPackage, basePackage
     * @return
     */
    private static String getBasePackages(String packageName){
        List<String> strs = SPLITTER_COMMA.splitToList(packageName);
        ArrayList<String> packageNames = Lists.newArrayList();
        for(String str : strs){
            if(str.contains("(")) {
                Matcher matcher = PATTERN_PACKAGE.matcher(str);
                if(matcher.find()){
                    String beforeStr = matcher.group(1);
                    List<String> centralList = SPLITTER_VERTICAL.splitToList(matcher.group(2));
                    String afterStr = matcher.group(3);
                    for(String centraStr:centralList) {
                        packageNames.add(beforeStr + centraStr + afterStr);
                    }
                }
            }else{
                packageNames.add(str);
            }
        }
        return JOINER_PACKAGE.join(packageNames);
    }
}
