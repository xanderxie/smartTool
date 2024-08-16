package model.databse;

import lombok.Data;

/**
 * @author xiezhidong
 * @date 2024/8/16 16:26
 */
@Data
public class ColumInfo {

    private String columnName;

    private String type;

    private boolean primaryKey;
}
