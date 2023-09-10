import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.Token;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.List;

/**
 * ClassName: iii
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/9/4 - 19:12
 * @Version: v1.0
 */
public class JSqlParserTest {

    public static void main(String[] args) throws Exception {
        String sql = "create table table1(id int(11,2,3),name varchar(36,0))";
        getCreateTableInfo(sql);
    }

    /**
     * 根据SQL语句获取表名和建表参数
     *
     * @param sql sql语句
     * @return
     * @throws JSQLParserException
     */
    private static void getCreateTableInfo(String sql) throws JSQLParserException {

        CreateTable stmt = (CreateTable) CCJSqlParserUtil.parse(sql);
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        System.out.println("表名:" + tablesNamesFinder.getTableList(stmt));
        List<ColumnDefinition> list = stmt.getColumnDefinitions();
        for (ColumnDefinition d : list) {
            System.out.println("列名:" + d.getColumnName() + "  ,列类型:" + d.getColDataType().getDataType() + "  长度:"
                    + d.getColDataType().getArgumentsStringList());
        }
    }
}
