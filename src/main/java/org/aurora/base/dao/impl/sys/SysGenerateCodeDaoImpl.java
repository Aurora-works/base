package org.aurora.base.dao.impl.sys;

import org.aurora.base.dao.impl.BaseDaoImpl;
import org.aurora.base.dao.sys.SysGenerateCodeDao;
import org.aurora.base.entity.sys.SysGenerateCode;
import org.springframework.stereotype.Repository;

@Repository
public class SysGenerateCodeDaoImpl extends BaseDaoImpl<SysGenerateCode> implements SysGenerateCodeDao {

    @Override
    public SysGenerateCode findByIdWithFetchGraph2(Long id) {
        var graph = getSession().createEntityGraph(SysGenerateCode.class);
        graph.addSubgraph("menu");
        graph.addSubgraph("table").addSubgraph("columns");
        return getSession().byId(SysGenerateCode.class).withFetchGraph(graph).load(id);
    }
}
