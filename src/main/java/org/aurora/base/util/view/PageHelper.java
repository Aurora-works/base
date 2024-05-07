package org.aurora.base.util.view;

import lombok.*;
import org.aurora.base.util.dto.TableFormatter;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageHelper<T> {

    private long total;
    private List<T> rows;
    private List<TableFormatter> formatter;
}
