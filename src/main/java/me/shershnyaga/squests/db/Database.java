package me.shershnyaga.squests.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.shershnyaga.squests.SQuests;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Database {

    private Dao<User, Long> dao;

    public Database() {
        String databaseUrl = "jdbc:sqlite:./plugins/SQuests/database.db";

        Bukkit.getScheduler().runTaskAsynchronously(SQuests.getPlugin(), () -> {
            JdbcConnectionSource connectionSource = null;
            try {
                connectionSource = new JdbcConnectionSource(databaseUrl);
                TableUtils.createTableIfNotExists(connectionSource, User.class);
                dao = DaoManager.createDao(connectionSource, User.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void createUser(String nickname, int count) {
        Bukkit.getScheduler().runTaskAsynchronously(SQuests.getPlugin(), () -> {
            try {
                User user = dao
                        .queryForFirst(dao.queryBuilder().where()
                                .eq("nickname", nickname).prepare());

                if (user == null)
                    dao.createIfNotExists(new User(nickname, count));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void increaseQuestCount(String nickname) {
        Bukkit.getScheduler().runTaskAsynchronously(SQuests.getPlugin(), () -> {
            try {
                User user = dao
                        .queryForFirst(dao.queryBuilder().where()
                                .eq("nickname", nickname).prepare());
                user.setQuestsCount(user.getQuestsCount() + 1);
                dao.createOrUpdate(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void printQuestsCount(Player player, String nickname) {
        Bukkit.getScheduler().runTaskAsynchronously(SQuests.getPlugin(), () -> {
            try {
                User user = dao
                        .queryForFirst(dao.queryBuilder().where()
                                .eq("nickname", nickname).prepare());

                player.sendMessage(Component.text("Кол-во выполненных квестов у " + nickname + ": " +
                    ChatColor.AQUA + (user.getQuestsCount() - 1)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}
