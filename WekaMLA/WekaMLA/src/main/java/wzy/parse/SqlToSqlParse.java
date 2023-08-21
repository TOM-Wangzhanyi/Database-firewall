package wzy.parse;

import com.alibaba.druid.sql.parser.*;
import weka.core.Instance;

/**
 * ClassName: SqlToSqlParse
 * Package: wzy.parse
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/20 - 11:44
 * @Version: v1.0
 */

//针对instance进行sql解析替换
public class SqlToSqlParse {
    public static boolean sqlparse (Instance instance)throws Exception {
        String sql = instance.stringValue(0);
        sql = updateDruidSqlParse(sql);
        instance.setValue(0, sql);
        return true ;
    }

    //解析sql语句
    public static String druidSqlParse(String sql) throws Exception{
        StringBuffer sb = new StringBuffer() ;
        // 实例化词法解析器
        try {
            Lexer lexer = new Lexer(sql);
            for (; ; ) {
                // 解析下一个token
                lexer.nextToken();
                // 获得解析完的token，Token是一个枚举
                Token tok = lexer.token();
                if (tok == Token.IDENTIFIER) {
                    sb.append("<" + tok.name + lexer.stringVal() + ">");
                    //   System.out.println(tok.name() + "\t\t" + lexer.stringVal());
                } else if (tok == Token.LITERAL_INT) {
                    sb.append("<" + tok.name + lexer.numberString() + ">");
                    //  System.out.println(tok.name() + "\t\t" + lexer.numberString());
                } else {
                    sb.append("<" + tok.name + tok.name + ">");
                    // System.out.println(tok.name() + "\t\t\t" + tok.name);
                }

                if (tok == Token.WHERE) {
                    sb.append("<" + "where pos :" + lexer.pos() + ">");
                    //    System.out.println("where pos : " + lexer.pos());
                }
                if (tok == Token.EOF) {
                    break;
                }
            }
        }
        catch (ParserException e){
            return new String("解析失败了") ;
        }
        return sb.toString() + "解析成功了";
    }

    //另一种解析sql的方法
    public static String updateDruidSqlParse(String sql) throws Exception{
        StringBuffer sb= new StringBuffer() ;
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser("sql" ,"mysql");
        Lexer lexer = parser.getLexer();
        Token token = lexer.token();
        while (token != Token.EOF) {
            // 打印词法单元的信息
            // System.out.println(token.name() + " " + token.toString());
            sb.append("<"+token.name+token.toString()+">"+" ");
            lexer.nextToken();
            token = lexer.token() ;
        }
        return sb.toString() ;
    }
}

