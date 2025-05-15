package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.bean.BeanUtil;
import java.util.Date;
<#if table.serviceInterface>
import ${package.Service}.${table.serviceName};
</#if>
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>()<#if table.serviceInterface>, ${table.serviceName}</#if> {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}><#if table.serviceInterface> implements ${table.serviceName}</#if> {

    @Transactional(rollbackFor = Throwable.class)
	public void save${entity}(${entity} ${table.entityPath}) {

		${entity} entity = new ${entity}();
		BeanUtil.copyProperties(${table.entityPath}, entity);
		Date date = new Date();
		entity.setUpdateTime(date);
		entity.setCreateTime(date);
		entity.setVersion(0);
		save(entity);
	}

    public void update${entity}ById(${entity} ${table.entityPath}) {
        ${table.entityPath}.setUpdateTime(new Date());
        updateById(${table.entityPath});
    }
}
</#if>
