package org.aurora.base.util.dto;

// @JsonIgnoreProperties(ignoreUnknown = true)
public record SysAuthMenu(
        Long id,
        String menuName,
        Long parentId,
        String orderBy,
        String css,
        String readOp,
        String createOp,
        String updateOp,
        String deleteOp,
        Boolean isParent) {
}
