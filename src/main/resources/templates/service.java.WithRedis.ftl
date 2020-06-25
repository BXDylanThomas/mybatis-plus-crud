package ${package.Service};

import ${cfg.basePackageName}.expand.MyLambdaQueryWrapper;
import ${cfg.basePackageName}.expand.MyPage;
import ${package.Entity}.${entity};
import ${superServiceClassPackage};

import java.io.Serializable;

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    ${entity} getById(Serializable id, boolean isUseCache);

    MyPage<${entity}> page(${entity} ${table.entityPath}, Integer page, Integer size, boolean isUseCache);

    MyPage<${entity}> page(${entity} ${table.entityPath}, Integer page, Integer size, boolean isUseCache, String dimension);

    MyPage<${entity}> page(MyLambdaQueryWrapper<${entity}> wrapper, Integer page, Integer size, boolean isUseCache);

    MyPage<${entity}> page(MyLambdaQueryWrapper<${entity}> wrapper, Integer page, Integer size, boolean isUseCache, String dimension);

    void delAllPageCache();

    void delDimensionPageCache(String dimension);
}
</#if>
