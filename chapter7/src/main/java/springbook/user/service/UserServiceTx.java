package springbook.user.service;

import java.util.List;
import lombok.Setter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.domain.User;

public class UserServiceTx implements UserService {

    @Setter
    private UserService userService;
    @Setter
    private PlatformTransactionManager transactionManager;

    @Override
    public void upgradeLevels() {
        TransactionStatus status = transactionManager.getTransaction(
            new DefaultTransactionDefinition());

        try {
            this.userService.upgradeLevels();

            this.transactionManager.commit(status);
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }

    }

    @Override
    public void add(User user) {
        TransactionStatus status = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            this.userService.add(user);
            this.transactionManager.commit(status);
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public User get(String id) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setReadOnly(true);
        TransactionStatus status = this.transactionManager.getTransaction(
            definition);
        try {
            User result = get(id);
            this.transactionManager.commit(status);
            return result;
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public List<User> getAll() {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setReadOnly(true);
        TransactionStatus status = this.transactionManager.getTransaction(
            definition);
        try {
            List<User> result = this.userService.getAll();
            this.transactionManager.commit(status);
            return result;
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public int getCount() {
        return this.userService.getCount();
    }

    @Override
    public void deleteAll() {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = this.transactionManager.getTransaction(
            definition);
        try {
            this.userService.deleteAll();
            this.transactionManager.commit(status);
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public void update(User user) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = this.transactionManager.getTransaction(
            definition);
        try {
            this.userService.update(user);
            this.transactionManager.commit(status);
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}
