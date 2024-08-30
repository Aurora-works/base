package org.aurora.base.common.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComboTreeHelper {

    private String id;
    private String text;
    private String iconCls;
    private List<ComboTreeHelper> children;
}
