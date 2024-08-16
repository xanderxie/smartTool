package tool;

import model.databse.ColumInfo;
import model.databse.TableInfo;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xiezhidong
 * @date as
 */
public class DatabaseUtils {


    private final static String DATABASE_NAME = "web3";


    private final static String TABLE_CREATE_DDL_TEMPLATE = "CREATE TABLE `%s`.`%s`  (\n" +
            "%s\n"
            + ");";

    private final static String PRIMARY_KEY_TEMPLATE = "\tPRIMARY KEY (`%s`)";

    private final static String COLUMN_TEMPLATE = "\t`%s` %s";

    public static void mysqlXmlMapper2TableDdl(String mapperDicPath) {
        List<File> files = FileUtils.dicFiles(mapperDicPath, (name) -> name != null && name.endsWith("Mapper.xml"));
        if (files == null || files.isEmpty()) {
            return;
        }

        files.stream().map(DatabaseUtils::mysqlXmlMapper2TableInfo)
                .filter(Objects::nonNull)
                .map(DatabaseUtils::tableInfo2ddl)
                .map(s -> s + "\n")
                .forEach(System.out::println);

    }


    public static TableInfo mysqlXmlMapper2TableInfo(File xmlFile) {
        Document document = FileUtils.readXml2document(xmlFile);
        if (document == null) {
            return null;
        }
        Element rootElement = document.getRootElement();
        if (rootElement == null) {
            return null;
        }

        TableInfo tableInfo = new TableInfo();

        Optional.ofNullable(rootElement.element("insert"))
                .map(Branch::content)
                .map(list -> list.get(0))
                .map(Node::getText)
                .map(text -> text.substring(text.indexOf(" into ") + 6).split(" ")[0])
                .ifPresent(tableInfo::setTableName);


        Optional.ofNullable(rootElement.element("resultMap"))
                .map(Element::elements)
                .map(elements -> elements.stream().map(e -> {
                    ColumInfo columInfo = new ColumInfo();
                    columInfo.setColumnName(e.attribute("column").getValue());
                    columInfo.setType(e.attribute("jdbcType").getValue());
                    columInfo.setPrimaryKey("id".equals(e.getName()));
                    if (columInfo.isPrimaryKey()) {
                        tableInfo.setPkColumInfo(columInfo);
                        return null;
                    }
                    return columInfo;
                }).filter(Objects::nonNull).collect(Collectors.toList()))
                .ifPresent(tableInfo::setColumInfoList);
        return tableInfo;
    }

    public static String tableInfo2ddl(TableInfo tableInfo) {
        StringBuilder sb = new StringBuilder();
        ColumInfo pkColumInfo = tableInfo.getPkColumInfo();
        String pk = null;
        if (pkColumInfo != null) {
            sb.append(columInfo2ddl(pkColumInfo))
                    .append(",\n");
            pk = String.format(PRIMARY_KEY_TEMPLATE, pkColumInfo.getColumnName());
        }
        List<String> column = tableInfo.getColumInfoList()
                .stream()
                .map(DatabaseUtils::columInfo2ddl)
                .collect(Collectors.toList());
        if (pk != null) {
            column.add(pk);
        }
        sb.append(String.join(",\n", column));
        return String.format(TABLE_CREATE_DDL_TEMPLATE, DATABASE_NAME, tableInfo.getTableName(), sb);
    }

    public static String columInfo2ddl(ColumInfo columInfo) {
        return String.format(COLUMN_TEMPLATE, columInfo.getColumnName(), columInfo2columnType(columInfo));
    }


    public static String columInfo2columnType(ColumInfo columInfo) {
        switch (columInfo.getType()) {
            case "INTEGER":
                if (columInfo.isPrimaryKey()) {
                    return "bigint NOT NULL AUTO_INCREMENT";
                }
                return "int NULL";
            case "VARCHAR":
                if (columInfo.isPrimaryKey()) {
                    return "varchar(255) NOT NULL";
                }
                return "varchar(255) NULL";
            case "BIGINT":
                if (columInfo.isPrimaryKey()) {
                    return "bigint NOT NULL AUTO_INCREMENT";
                }
                return "bigint NULL";
            case "DECIMAL":
                return "decimal(10, 2) NULL";
            case "DOUBLE":
                return "double NULL";
            case "TIMESTAMP":
                return "timestamp NULL";
            default:
                return null;
        }
    }


}
