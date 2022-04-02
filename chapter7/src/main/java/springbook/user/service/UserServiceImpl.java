package springbook.user.service;

import java.util.List;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static Level DEFAULT_LEVEL = Level.BASIC;

    @Setter
    @Autowired
    private UserDao userDao;
    @Setter
    @Autowired
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

    @Override
    public User get(String id) {
        return this.userDao.get(id);
    }

    @Override
    public List<User> getAll() {
        return this.userDao.getAll();
    }

    @Override
    public int getCount() {
        return this.userDao.getCount();
    }

    @Override
    public void deleteAll() {
        this.userDao.deleteAll();
    }

    @Override
    public void update(User user) {
        this.userDao.update(user);
    }
}
