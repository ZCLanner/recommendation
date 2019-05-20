package me.lanner.liz.paper.recommend;

import java.util.Objects;

public class User {

    private final Integer userId;

    public User(Integer userId, Integer categoryId) {
        this.userId = userId;
        this.categoryId = categoryId;
    }

    private final Integer categoryId;

    public Integer getUserId() {
        return userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }
}
