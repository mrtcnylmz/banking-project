package com.mrtcnylmz.bankingsystem.Repository;

import org.apache.ibatis.annotations.Mapper;

import com.mrtcnylmz.bankingsystem.Models.UserModel;

@Mapper
public interface UserRepository {
	public UserModel selectUserWithId(int id);
	public UserModel selectUserWithUsername(String username);
	public UserModel selectUserWithEmail(String email);
	public int selectUserIdWithId(int id);
	public boolean selectUserEnabledWithUsername(String username);
	public boolean selectUserEnabledWithId(int id);
	public void insertUser(UserModel userModel);
	public void updateUserEnabledWithId(int id, boolean enabled);

}
