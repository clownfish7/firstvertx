import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

/**
 * @author You
 * @create 2019-06-10 22:45
 */
public class VertxTest extends AbstractVerticle {

    private JDBCClient dbClient;

    @Override
    public void start() throws Exception {

        // 构造数据库的连接信息
        JsonObject dbConfig = new JsonObject();
        dbConfig.put("url", "jdbc:mysql://localhost:3306/a");
        dbConfig.put("driver_class", "com.mysql.jdbc.Driver");
        dbConfig.put("user", "yzy");
        dbConfig.put("password", "19970806");


//        // 获取到数据库连接的客户端
//        JDBCClient jdbcClient = new JdbcUtils(vertx).getDbClient();

        JDBCClient jdbcClient = JDBCClient.createShared(vertx, dbConfig);

        String sql = "select * from a";
        jdbcClient.getConnection(con -> {
            if (con.succeeded()) {
                System.out.println("获取到数据库连接");
//                获取到的连接对象
                SQLConnection connection = con.result();
//                执行查询操作
                connection.query("select * from a", rs -> {
//                    处理查询结果
                    if (rs.succeeded()) {
                        System.out.println(rs.result().getRows());
                    }
                });

                connection.setAutoCommit(false, a ->{

                });

                connection.update("insert into a values ('我','11','11','11')", up -> {
                    if (up.succeeded()) {
                        connection.commit(rx -> {
                            if (rx.succeeded()) {
                                //  事务提交成功
                                System.out.println("succ");
                            }
                        });
                    } else {
                        connection.rollback(rb -> {
                            if (rb.succeeded()) {
                                //  事务回滚成功
                            }
                        });
                    }
                });

                //  开启事务
                connection.setAutoCommit(false, v -> {
                    if (v.succeeded()) {
                        //  事务开启成功 执行crud操作
                        System.out.println("开启");
                        connection.update("insert into a values ('我8','11','11','11')", up -> {
                            System.out.println(up.succeeded());
                            if (up.succeeded()) {
                                connection.commit(rx -> {
                                    if (rx.succeeded()) {
                                        System.out.println(rx.succeeded());
                                        //  事务提交成功
                                        System.out.println("succ");
                                    }
                                });
                            } else {
                                connection.rollback(rb -> {
                                    if (rb.succeeded()) {
                                        //  事务回滚成功
                                    }
                                });
                            }
                        });
                        connection.close();
                    } else {
                        System.out.println("开启事务失败");
                    }
                });
            } else {
                System.out.println("获取数据库连接失败");
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new VertxTest());
    }

}
