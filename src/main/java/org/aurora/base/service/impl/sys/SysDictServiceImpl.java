package org.aurora.base.service.impl.sys;

import org.apache.commons.lang3.StringUtils;
import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysDictDao;
import org.aurora.base.entity.sys.SysDict;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysDictService;
import org.aurora.base.util.dto.TableFormatter;
import org.aurora.base.util.enums.Status;
import org.aurora.base.util.view.FilterRuleHelper;
import org.aurora.base.util.view.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDict> implements SysDictService {
    @Autowired
    public SysDictServiceImpl(SysDictDao dictDao) {
        this.dictDao = dictDao;
    }

    private final SysDictDao dictDao;

    @Override
    protected BaseDao<SysDict> getDao() {
        return dictDao;
    }

    @Override
    public PageHelper<SysDict> findAllInPage(int page, int size, String sort, String order, List<FilterRuleHelper> filterRules) {
        PageHelper<SysDict> allInPage = super.findAllInPage(page, size, sort, order, filterRules);
        List<SysDict> rows = allInPage.getRows();
        if (rows != null && !"dictCode".equals(sort)) {
            rows.sort(Comparator.comparing(SysDict::getDictCode));
        }
        return allInPage;
    }

    @Override
    public List<TableFormatter> findAllGroupByCode() {
        return dictDao.findAllGroupByCode();
    }

    @Override
    public List<SysDict> findByCode(String dictCode) {
        return dictDao.findByCode(dictCode).stream()
                .filter(dict -> Status.ENABLED.getKey().equals(dict.getStatus()))
                .toList();
    }

    @Override
    public void save(Map<String, List<SysDict>> changes) {
        List<SysDict> createList = changes.get("inserted");
        List<SysDict> updateList = changes.get("updated");
        List<SysDict> deleteList = changes.get("deleted");
        if (createList != null) {
            create(createList);
        }
        if (updateList != null) {
            update(updateList);
        }
        if (deleteList != null) {
            delete(deleteList);
        }
    }

    private void create(List<SysDict> dList) {
        Set<String> dictCodes = new HashSet<>();
        for (SysDict dict : dList) {
            if (dict.getId() != null || StringUtils.isBlank(dict.getDescription())) {
                throw new IllegalArgumentException();
            }
            dict.setDictCode(dict.getDictCode().toUpperCase());
            dictDao.create(dict);
            dictCodes.add(dict.getDictCode());
        }
        updateDescription(dList, dictCodes);
    }

    @Override
    public void create(SysDict dict) {
        dict.setDictCode(dict.getDictCode().toUpperCase());
        dictDao.create(dict);
        List<SysDict> list = dictDao.findByCode(dict.getDictCode());
        for (SysDict d : list) {
            d.setDescription(dict.getDescription());
        }
    }

    private void update(List<SysDict> dList) {
        Set<String> dictCodes = new HashSet<>();
        for (SysDict dict : dList) {
            if (dict.getId() == null || StringUtils.isBlank(dict.getDescription())) {
                throw new IllegalArgumentException();
            }
            dict.setDictCode(dict.getDictCode().toUpperCase());
            dictDao.update(dict);
            dictCodes.add(dict.getDictCode());
        }
        updateDescription(dList, dictCodes);
    }

    private void updateDescription(List<SysDict> dList, Set<String> dictCodes) {
        List<SysDict> list = dictDao.findByCodes(dictCodes.toArray(new String[0]));
        for (SysDict dict : list) {
            dList.forEach(item -> {
                if (dict.getDictCode().equals(item.getDictCode())) {
                    dict.setDescription(item.getDescription());
                }
            });
        }
    }

    private void delete(List<SysDict> dList) {
        for (SysDict dict : dList) {
            if (dict.getId() == null) {
                throw new IllegalArgumentException();
            }
            dictDao.delete(dict.getId());
        }
    }
}
