package org.aurora.base.service.impl.sys;

import org.apache.commons.lang3.StringUtils;
import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysParamDao;
import org.aurora.base.dao.sys.SysRoleMenuDao;
import org.aurora.base.dao.sys.SysUserDao;
import org.aurora.base.dao.sys.SysUserRoleDao;
import org.aurora.base.entity.sys.*;
import org.aurora.base.exception.BusinessException;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysUserService;
import org.aurora.base.shiro.ShiroUtils;
import org.aurora.base.util.dto.PersonalData;
import org.aurora.base.util.enums.Status;
import org.aurora.base.util.enums.SysParam;
import org.aurora.base.util.enums.UserType;
import org.aurora.base.util.enums.YesOrNo;
import org.aurora.base.util.view.FilterRuleHelper;
import org.aurora.base.util.view.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService {
    @Autowired
    public SysUserServiceImpl(SysUserDao userDao, SysUserRoleDao userRoleDao, SysRoleMenuDao roleMenuDao, SysParamDao paramDao) {
        this.userDao = userDao;
        this.userRoleDao = userRoleDao;
        this.roleMenuDao = roleMenuDao;
        this.paramDao = paramDao;
    }

    private final SysUserDao userDao;
    private final SysUserRoleDao userRoleDao;
    private final SysRoleMenuDao roleMenuDao;
    private final SysParamDao paramDao;

    @Override
    protected BaseDao<SysUser> getDao() {
        return userDao;
    }

    @Override
    public void create(SysUser user) {
        String defaultPwd = paramDao.findByCode(SysParam.SYS_DEFAULT_PASSWORD.getCode()).getParamValue();
        user.setUserType(UserType.ADMIN.getKey());
        user.setIsDeleted(YesOrNo.NO.getKey());
        user.setPassword(defaultPwd);
        ShiroUtils.encryptPassword(user);
        userDao.create(user);
    }

    @Override
    public void update(SysUser user) {
        SysUser updateUser = userDao.findById(user.getId());
        if (updateUser == null || YesOrNo.YES.getKey().equals(updateUser.getIsDeleted()) || !UserType.ADMIN.getKey().equals(updateUser.getUserType())) {
            throw new IllegalArgumentException();
        }
        user.setUsername(updateUser.getUsername());
        user.setPassword(updateUser.getPassword());
        user.setSalt(updateUser.getSalt());
        user.setUserType(updateUser.getUserType());
        user.setIsDeleted(updateUser.getIsDeleted());
        userDao.update(user);
    }

    @Override
    public void delete(Long[] ids) {
        List<SysUser> users = getDao().findByIds(ids);
        for (SysUser user : users) {
            if (!UserType.ADMIN.getKey().equals(user.getUserType())) {
                throw new IllegalArgumentException();
            }
            user.setIsDeleted(YesOrNo.YES.getKey());
            userDao.update(user); // SoftDelete
        }
        userRoleDao.deleteByUserIds(ids);
    }

    @Override
    protected List<FilterRuleHelper> addBusinessFilterRules(List<FilterRuleHelper> filterRules) {
        filterRules = super.addBusinessFilterRules(filterRules);
        FilterRuleHelper filterRule_0 = FilterRuleHelper.builder()
                .field("userType")
                .op(FilterRuleHelper.EQUAL)
                .value(UserType.ADMIN.getKey())
                .build();
        FilterRuleHelper filterRule_1 = FilterRuleHelper.builder()
                .field("isDeleted")
                .op(FilterRuleHelper.EQUAL)
                .value(YesOrNo.NO.getKey())
                .build();
        List<FilterRuleHelper> list = List.of(filterRule_0, filterRule_1);
        if (filterRules == null) {
            filterRules = new ArrayList<>();
        }
        filterRules.addAll(list);
        return filterRules;
    }

    @Override
    protected void checkExportColumns(List<SysTableColumn> columns) {
        super.checkExportColumns(columns);
        columns.removeIf(column -> {
            String entityName = column.getEntityName();
            return entityName.equals("password")
                    || entityName.equals("salt")
                    || entityName.equals("avatarImagePath")
                    || entityName.equals("userType")
                    || entityName.equals("isDeleted");
        });
    }

    @Override
    public SysUser findByUsername(String username) {
        SysUser user = userDao.findByUsername(username);
        if (user != null && YesOrNo.NO.getKey().equals(user.getIsDeleted())) {
            return user;
        }
        return null;
    }

    @Override
    public SysUser findByMobile(String mobilePhoneNumber) {
        SysUser user = userDao.findByMobile(mobilePhoneNumber);
        if (user != null && YesOrNo.NO.getKey().equals(user.getIsDeleted())) {
            return user;
        }
        return null;
    }

    @Override
    public Map<String, Set<String>> getAuthorizationInfo(Long id) {

        Map<String, Set<String>> resultMap = new HashMap<>();
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        Set<Long> roleIds = new HashSet<>();

        List<SysUserRole> userRoles = userRoleDao.findByUserIdWithFetchGraph(id);
        userRoles.stream()
                .map(SysUserRole::getRole)
                .filter(r -> Status.ENABLED.getKey().equals(r.getStatus()))
                .forEach(r -> {
                    roles.add(r.getRoleCode());
                    roleIds.add(r.getId());
                });
        List<SysRoleMenu> roleMenus = roleMenuDao.findByRoleIdWithFetchGraph(roleIds.toArray(new Long[0]));
        roleMenus.stream()
                .filter(rm -> checkMenuStatus(rm.getMenu()))
                .forEach(rm -> {
                    String menuCode = rm.getMenu().getMenuCode();
                    if (Status.ENABLED.getKey().equals(rm.getCreateOp())) {
                        permissions.add(menuCode + ":create");
                    }
                    if (Status.ENABLED.getKey().equals(rm.getUpdateOp())) {
                        permissions.add(menuCode + ":update");
                    }
                    if (Status.ENABLED.getKey().equals(rm.getDeleteOp())) {
                        permissions.add(menuCode + ":delete");
                    }
                    if (Status.ENABLED.getKey().equals(rm.getReadOp())) {
                        permissions.add(menuCode + ":read");
                    }
                });
        resultMap.put("roles", roles);
        resultMap.put("permissions", permissions);
        return resultMap;
    }

    @Override
    public TreeSet<SysMenu> getMenuTree(Long id) {

        TreeSet<SysMenu> menus = new TreeSet<>(Comparator.comparing(SysMenu::getOrderBy));

        List<SysUserRole> userRoles = userRoleDao.findByUserIdWithFetchGraph(id);
        Set<Long> roleIds = new HashSet<>();
        userRoles.stream()
                .map(SysUserRole::getRole)
                .filter(r -> Status.ENABLED.getKey().equals(r.getStatus()))
                .forEach(r -> roleIds.add(r.getId()));
        List<SysRoleMenu> roleMenus = roleMenuDao.findByRoleIdWithFetchGraph(roleIds.toArray(new Long[0]));
        roleMenus.stream()
                .filter(rm -> checkMenuStatus(rm.getMenu()))
                .forEach(rm -> {
                    if (Status.ENABLED.getKey().equals(rm.getReadOp())) {
                        addMenu(menus, rm.getMenu());
                    }
                });
        return menus;
    }

    @Override
    public void updatePersonalData(PersonalData data) {

        SysUser user = userDao.findById(ShiroUtils.getCurrentUserId());

        if (StringUtils.isNotBlank(data.oldPassword()) && StringUtils.isNotBlank(data.newPassword())) {
            if (user.getPassword().equals(ShiroUtils.generatePassword(data.oldPassword(), user.getSalt()))) {
                user.setPassword(data.newPassword());
                ShiroUtils.encryptPassword(user);
            } else {
                throw new BusinessException("信息更新失败, 您输入了错误的 [旧密码]");
            }
        }

        user.setNickname(data.nickname());
        user.setSex(data.sex());
        user.setEmail(data.email());
        user.setMobilePhoneNumber(data.mobilePhoneNumber());
        user.setDescription(data.description());
    }

    @Override
    public void resetPwd(Long[] ids) {
        String defaultPwd = paramDao.findByCode(SysParam.SYS_DEFAULT_PASSWORD.getCode()).getParamValue();
        List<SysUser> users = userDao.findByIds(ids);
        for (SysUser user : users) {
            user.setPassword(defaultPwd);
            ShiroUtils.encryptPassword(user);
            userDao.update(user);
        }
    }

    @Override
    public PageHelper<SysUser> findByRoleId(int page, int size, String sort, String order, Long roleId) {
        checkFields(sort, order, null);
        return new PageHelper<>(
                userRoleDao.getTotalByRoleId(roleId),
                userRoleDao.findUserListByRoleId(page, size, sort, order, roleId),
                getFormatters());
    }

    private void addMenu(TreeSet<SysMenu> menus, SysMenu menu) {
        menus.add(menu);
        if (menu.getParentId() != null) {
            addMenu(menus, menu.getParentMenu());
        }
    }

    private boolean checkMenuStatus(SysMenu menu) {
        boolean status = Status.ENABLED.getKey().equals(menu.getStatus());
        if (status && menu.getParentId() == null) {
            return true;
        }
        if (status) {
            return checkMenuStatus(menu.getParentMenu());
        }
        return false;
    }
}
