package model.databse;

import lombok.Data;

import java.util.List;

/**
 * @author xiezhidong
 * @date 2024/8/16 16:26
 */
@Data
public class TableInfo {

    private String tableName;

    private ColumInfo pkColumInfo;

    private List<ColumInfo> columInfoList;

}
