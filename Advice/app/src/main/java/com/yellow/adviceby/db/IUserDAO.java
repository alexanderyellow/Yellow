package com.yellow.adviceby.db;

import com.yellow.adviceby.model.User;

/**
 * Created by SheykinAV on 21.09.2015.
 */
public interface IUserDAO {
    public void create(User user);
    public User read();
    public void update(User user);
    public void delete(User user);
}
