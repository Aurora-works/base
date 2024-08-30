package org.aurora.base.common.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aurora.base.common.dto.TableFormatter;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageHelper<T> {

    private long total;
    private List<T> rows;
    private List<TableFormatter> formatter;
}
