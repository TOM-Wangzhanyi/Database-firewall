import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.collections4.CollectionUtils;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ClassName: TestSqlParse
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/21 - 11:24
 * @Version: v1.0
 */
public class JsqlparserUtil {
    /**
     * 基于Jsqlparser的sql解析功能，并获取表名和where后面的条件
     */
        //装载where后面的字段名称并去重
        private Set<String> set = new HashSet<>();
        //解析出来的单个条件名称
        private String columnName = null;

        /**
         * 获取SQL中的全部表名
         *
         * @param sql
         * @return
         */
        public static String getTableName(String sql) {
            try {
                Statement statement = CCJSqlParserUtil.parse(sql);
                TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                List<String> tableNameList = tablesNamesFinder.getTableList(statement);
                if (!CollectionUtils.isEmpty(tableNameList)) {
                    StringBuffer allTableNames = new StringBuffer();
                    tableNameList.forEach(tableName -> {
                        allTableNames.append(tableName + ",");
                    });
                    String allTableName = allTableNames.toString().substring(0, allTableNames.toString().length() - 1);
                    return allTableName;
                }
            } catch (JSQLParserException e) {

            }
            return null;
        }

        /**
         * 获取SQL中的where后面的条件名称
         *
         * @param sql
         * @return
         * @throws JSQLParserException
         */
        public String getCloumnNames(String sql) throws JSQLParserException {
            String columnNames = null;
            String allColumnNames = null;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(sql);
            Statement statement = CCJSqlParserUtil.parse(new StringReader(stringBuffer.toString()));
            if (statement instanceof Select) {
                Select istatement = (Select) statement;
                Expression where = ((PlainSelect) istatement.getSelectBody()).getWhere();
                if (null != where) {
                    Set<String> sets = getParser(where);
                    StringBuffer st = new StringBuffer();
                    sets.stream().forEach(set -> {
                        st.append(set + ",");
                    });
                    columnNames = st.toString();
                }
            }
            if (null != columnNames && columnNames != "" && !columnNames.equals("")) {
                allColumnNames = columnNames.substring(0, columnNames.length() - 1);
            }
            return allColumnNames;
        }

        private Set<String> getParser(Expression expression) {
            //初始化接受获得的字段信息
            if (expression instanceof BinaryExpression) {
                //获得左边表达式
                Expression leftExpression = ((BinaryExpression) expression).getLeftExpression();
                //获得左边表达式为Column对象，则直接获得列名
                if (leftExpression instanceof Column) {
                    columnName = ((Column) leftExpression).getColumnName();
                    set.add(columnName);
                } else if (leftExpression instanceof InExpression) {
                    this.parserInExpression(leftExpression);
                } else if (leftExpression instanceof IsNullExpression) {
                    this.parserIsNullExpression(leftExpression);
                } else if (leftExpression instanceof BinaryExpression) {//递归调用
                    getParser(leftExpression);
                } else if (expression instanceof Parenthesis) {//递归调用
                    Expression expression1 = ((Parenthesis) expression).getExpression();
                    getParser(expression1);
                }

                //获得右边表达式，并分解
                Expression rightExpression = ((BinaryExpression) expression).getRightExpression();
                if (rightExpression instanceof BinaryExpression) {
                    this.parserBinaryExpression(rightExpression);
                } else if (rightExpression instanceof InExpression) {
                    this.parserInExpression(rightExpression);
                } else if (rightExpression instanceof IsNullExpression) {
                    this.parserIsNullExpression(rightExpression);
                } else if (rightExpression instanceof Parenthesis) {//递归调用
                    Expression expression1 = ((Parenthesis) rightExpression).getExpression();
                    getParser(expression1);
                }
            } else if (expression instanceof InExpression) {
                this.parserInExpression(expression);
            } else if (expression instanceof IsNullExpression) {
                this.parserIsNullExpression(expression);
            } else if (expression instanceof Parenthesis) {//递归调用
                Expression expression1 = ((Parenthesis) expression).getExpression();
                getParser(expression1);
            }
            return set;
        }

        /**
         * 解析in关键字左边的条件
         *
         * @param expression
         */
        public void parserInExpression(Expression expression) {
            Expression leftExpression = ((InExpression) expression).getLeftExpression();
            if (leftExpression instanceof Column) {
                columnName = ((Column) leftExpression).getColumnName();
                set.add(columnName);
            }
        }

        /**
         * 解析is null 和 is not null关键字左边的条件
         *
         * @param expression
         */
        public void parserIsNullExpression(Expression expression) {
            Expression leftExpression = ((IsNullExpression) expression).getLeftExpression();
            if (leftExpression instanceof Column) {
                columnName = ((Column) leftExpression).getColumnName();
                set.add(columnName);
            }
        }

        public void parserBinaryExpression(Expression expression) {
            Expression leftExpression = ((BinaryExpression) expression).getLeftExpression();
            if (leftExpression instanceof Column) {
                columnName = ((Column) leftExpression).getColumnName();
                set.add(columnName);
            }
        }

        /**
         * 测试类
         *
         * @param args
         * @throws JSQLParserException
         */
        public static void main(String[] args) throws JSQLParserException {
            String sql = "select * from user where id = 1 and uuu = 9";
            String tableName = getTableName(sql);
            System.out.println("tableName:" + tableName);
            if (null != tableName) {
                JsqlparserUtil jsqlparserUtil = new JsqlparserUtil();
                String cloumnNames = jsqlparserUtil.getCloumnNames(sql);
                System.out.println("cloumnNames:" + cloumnNames);
            }
        }
    }

