package JDBC;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with Intellij IFEA
 * Description:
 * User : 花朝
 * Date : 2020-11-18
 * Time : 9:45
 */
public class Main2 {
    public static void main(String[] args) {
        //使用preparedstatement 可以防止sql注入；

        query("许仙");
    }
    public static void query (String queryName)  {
        // 加载JDBC驱动程序：反射，这样调用初始化com.mysql.jdbc.Driver类，即将该类加载到JVM方法
        //区，并执行该类的静态方法块、静态属性。
        Connection connection = null;
        PreparedStatement statement = null;//sql包
        ResultSet resultSet = null ;
        try {
            //创建数据库连接池
            DataSource dataSource = new MysqlDataSource();
            ((MysqlDataSource)dataSource).setUrl("jdbc:mysql://localhost:3306/chong?user=root&password=WOaini91154576@qq.com&useUnicode=true&useSSL=false&characterEncoding=UTF-8");
            // 创建数据库连接
            connection = dataSource.getConnection();
            System.out.println(connection);
            //创建操作命令；
            //statement = connection.createStatement();
            String sql = " select id,sn,name,qq_mail,classes_id from student where name like ?";
            //创建操作命令对象；（带占位符的sql在数据库预编译，可以提高效率，占位符的方式，可以防止sql注入）
            statement = connection.prepareStatement(sql);
            statement.setString(1,"%" + queryName + "%");
            System.out.println(statement);
            //执行sql
            resultSet = statement.executeQuery();

            //处理结果集；ResultSet (类似于List<Map<String,字段类型>>)；
            while(resultSet.next()){//遍历每一行数据
                int id = resultSet.getInt("id");
                int sn = resultSet.getInt("sn");
                String name = resultSet.getString("name");
                String qq_mail = resultSet.getString("qq_mail");
                int classes_id = resultSet.getInt("classes_id");
                System.out.println(String.format("Student:id = %s,sn = %s,name = %s,qq_mail = %s,classes_id = %d",id,
                        sn,name,qq_mail,classes_id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //释放资源；反向操作；
            try {
                if(resultSet != null)
                    resultSet.close();
                if(statement != null)
                    statement.close();
                if(connection != null)
                    connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
