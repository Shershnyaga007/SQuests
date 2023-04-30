package me.shershnyaga.squests.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

@DatabaseTable(tableName = "users")
public class User {

    public User() {

    }

    public User(String nickname, int questsCount) {
        this.nickname = nickname;
        this.questsCount = questsCount;
    }

    @DatabaseField(generatedId = true)
    @Getter
    private Long id;

    @DatabaseField(unique = true, canBeNull = false)
    @Getter
    @Setter
    private String nickname;

    @DatabaseField(defaultValue = "0", columnName = "quests_count")
    @Getter
    @Setter
    private int questsCount;
}
