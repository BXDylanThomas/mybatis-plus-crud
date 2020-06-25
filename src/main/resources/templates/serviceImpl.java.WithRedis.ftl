package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${cfg.basePackageName}.redis.impl.ServiceWithRedisImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ServiceWithRedisImpl<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ServiceWithRedisImpl<${table.mapperName}, ${entity}> implements ${table.serviceName} {

}
</#if>
