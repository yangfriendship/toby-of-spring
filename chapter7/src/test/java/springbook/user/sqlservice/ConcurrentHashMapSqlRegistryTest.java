package springbook.user.sqlservice;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest{

    @Override
    protected UpdatableSqlRegistry createSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}