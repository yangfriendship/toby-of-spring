package springbook.user.service;

import java.util.List;
import lombok.Setter;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserServiceImpl implements UserService {

    private static Level DEFAULT_LEVEL = Level.BASIC;

    @Setter
    private UserDao userDao;
    @Setter
    private PlatformTransactionManager transactionManager;
    @Setter
    private MailSender mailSender;

    @Override
    public void upgradeLevels() {
        upgradeLevelsInternal();
    }

    private void upgradeLevelsInternal() {
        final List<User> users = userDao.getAll();
        for (final User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        this.userDao.update(user);
        sendUpgradeEmail(user);
    }

    private void sendUpgradeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("youzheng@youzheng.me");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다.");

        this.mailSender.send(mailMessage);
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: {
                return user.getLogin() >= 50;
            }
            case SILVER: {
                return user.getRecommend() >= 30;
            }
            case GOLD: {
                return false;
            }
            default:
                throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }


    @Override
    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(DEFAULT_LEVEL);
        }
        this.userDao.add(user);
    }
}
