import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

/**
 * @author You
 * @create 2019-06-10 22:48
 */
public class JdbcUtils {


    // 用于操作数据库的客户端
    private JDBCClient dbClient;

    public JdbcUtils(Vertx vertx) {

        // 构造数据库的连接信息
        JsonObject dbConfig = new JsonObject();
        dbConfig.put("url", "jdbc:mysql://localhost:3306/a");
        dbConfig.put("driver_class", "com.mysql.jdbc.Driver");
        dbConfig.put("user", "yzy");
        dbConfig.put("password", "19970806");

        // 创建客户端
        JDBCClient jdbcClient = JDBCClient.createShared(vertx, dbConfig);
        dbClient = JDBCClient.createShared(vertx, dbConfig);
    }

    // 提供一个公共方法来获取客户端
    public JDBCClient getDbClient() {
        return dbClient;
    }

}
